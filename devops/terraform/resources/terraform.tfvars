aws_region = "us-east-1"

create_vpc             = true
vpc_name               = "ik-aline-financial"
vpc_availability_zones = ["us-east-1a", "us-east-1b"]

create_cluster          = true
cluster_name            = "ik-aline-financial"
eks_min_nodes           = 2
eks_max_nodes           = 6
eks_desired_nodes       = 2
eks_node_instance_types = ["t3.medium"]

create_db                 = true
db_name                   = "aline"
db_username               = "admin"
rds_name                  = "ik-aline-financial"
rds_allocated_storage     = 20
rds_max_allocated_storage = 20
rds_instance_class        = "db.t3.small"

autoscale           = false
loadbalancer        = true  
monitoring          = false 
install_application = false

route53_zone         = "Z0695888OWKTEQ5IMLUF"
eks_domain_name = "eks.alinefinancial.cloud"
