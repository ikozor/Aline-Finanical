module "rds-security-group" {
  source  = "terraform-aws-modules/security-group/aws//modules/mysql"
  version = "4.16.2"

  name = "ik-aline-financial-db-access"

  vpc_id              = module.vpc.vpc_id
  ingress_cidr_blocks = [module.vpc.vpc_cidr_block]
}

module "db" {
  source  = "terraform-aws-modules/rds/aws"
  version = "5.2.0"

  identifier = var.rds_name

  create_db_instance = var.create_db
  apply_immediately  = true

  engine                = "mysql"
  engine_version        = "8.0.28"
  instance_class        = var.rds_instance_class
  allocated_storage     = var.rds_allocated_storage
  max_allocated_storage = var.rds_max_allocated_storage

  db_name  = var.db_name
  username = var.db_username
  password = random_password.initial_password.result

  availability_zone      = module.vpc.azs[0]
  create_db_subnet_group = true
  subnet_ids             = module.vpc.database_subnets
  vpc_security_group_ids = [module.rds-security-group.security_group_id]

  maintenance_window = "Mon:00:00-Mon:03:00"
  backup_window      = "03:00-06:00"

  family               = "mysql8.0"
  major_engine_version = "8.0"

  skip_final_snapshot = true

  tags = var.global_tags

  depends_on = [
    module.vpc
  ]
}


