output "eks_cluster_version" {
  value = module.eks.cluster_version
}

output "eks_cluster_status" {
  value = module.eks.cluster_status
}

output "eks_name" {
  value = module.eks.cluster_name
}

output "db_id" {
  value = module.db.db_instance_id
}

output "db_endpoint" {
  value = module.db.db_instance_endpoint
}

output "db_name" {
  value = module.db.db_instance_name
}

output "db_engine" {
  value = module.db.db_instance_engine
}

output "db_username" {
  value     = module.db.db_instance_username
  sensitive = true
}

output "db_password" {
  value     = module.db.db_instance_password
  sensitive = true
}

output "vpc_id" {
  value = module.vpc.vpc_id
}

output "vpc_cidr_block" {
  value = module.vpc.vpc_cidr_block
}

output "database_subnets_cidr" {
  value = module.vpc.database_subnets_cidr_blocks
}

output "public_subnets_cidr" {
  value = module.vpc.public_subnets_cidr_blocks
}

output "private_subnets_cidr" {
  value = module.vpc.private_subnets_cidr_blocks
}

# IAM outputs 
output "aline_secrets_role_arn" {
  value = module.aws_secretsmanager_irsa.iam_role_arn
}

output "aline_secrets_arn" {
  value = aws_secretsmanager_secret.db_password.arn 
}

output "load_balancer_role_arn" {
  value = module.aws_load_balancer_controller_irsa_role.iam_role_arn
}

output "autoscaler_role_arn" {
  value = module.cluster_autoscaler_irsa_role.iam_role_arn
}

output "alb_certificate_arn" {
  value = aws_acm_certificate.alb_certificate.arn
}
