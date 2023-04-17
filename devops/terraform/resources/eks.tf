resource "aws_security_group" "istio_ingress" {
  name = "istio_ingress"
  description = "allow istio sidecar injections for pods"
  vpc_id = module.vpc.vpc_id

  ingress {
    from_port   = 0
    to_port     = 65535
    protocol    = "tcp"
    cidr_blocks = ["10.0.0.0/16"]
  }
  
}
module "eks" {

  source  = "terraform-aws-modules/eks/aws"
  version = "19.0.2"

  cluster_name    = var.cluster_name
  cluster_version = "1.25"

  create = var.create_cluster

  cluster_endpoint_private_access = true
  cluster_endpoint_public_access  = true

  vpc_id     = module.vpc.vpc_id
  subnet_ids = module.vpc.private_subnets

  enable_irsa = true

  eks_managed_node_groups = {
    ik-microservices = {
      desired_size = var.eks_desired_nodes
      min_size     = var.eks_min_nodes
      max_size     = var.eks_max_nodes

      labels = {
        role= "ik-microservices"
      }

      instance_types = var.eks_node_instance_types
      capacity_type  = "SPOT"

      vpc_security_group_ids= [aws_security_group.istio_ingress.id]

#      block_device_mappings = {
#      xvda = {
#        device_name = "/dev/xvda"
#        ebs = {
#          volume_size           = 100
#          volume_type           = "gp3"
#          iops                  = 3000
#          throughput            = 150
#          delete_on_termination = true
#        }
#      }
#    }
    }


  }

  tags = var.global_tags
}

data "aws_eks_cluster" "default" {
  name = module.eks.cluster_name
  depends_on = [
    module.eks
  ]
}
