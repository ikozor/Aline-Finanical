module "vpc" {
  source  = "terraform-aws-modules/vpc/aws"
  version = "3.18.1"

  create_vpc = var.create_vpc

  name = var.vpc_name
  cidr = "10.0.0.0/16"

  azs              = var.vpc_availability_zones
  private_subnets  = ["10.0.0.0/19", "10.0.32.0/19"]
  public_subnets   = ["10.0.64.0/19", "10.0.96.0/19"]
  database_subnets = ["10.0.128.0/19", "10.0.160.0/19"]

  enable_nat_gateway     = true
  single_nat_gateway     = true
  one_nat_gateway_per_az = false

  enable_dns_hostnames = true
  enable_dns_support   = true

  public_subnet_tags = merge(var.global_tags, {
    "kubernetes.io/role/elb" = "1"
  })

  private_subnet_tags = merge(var.global_tags, {
    "kubernetes.io/role/internal-elb" = "1"
  })

  tags = var.global_tags
}

resource "aws_vpc_endpoint" "secretsmanager" {
  vpc_id              = module.vpc.vpc_id
  service_name        = "com.amazonaws.${var.aws_region}.secretsmanager"
  vpc_endpoint_type   = "Interface"
  private_dns_enabled = true
  subnet_ids          = module.vpc.database_subnets
  security_group_ids  = [module.rds-security-group.security_group_id, module.eks.cluster_security_group_id, module.eks.node_security_group_id]

  depends_on = [
    module.eks,
    module.db
  ]
}

