resource "aws_iam_policy" "secrets_policy" {
  name        = "secret_access_policy"
  description = "EKS pods access to secrets"

  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "secretsmanager:GetSecretValue",
        "secretsmanager:DescribeSecret"
      ],
      "Resource": "${aws_secretsmanager_secret.db_password.arn}"
    }
  ]
}
  EOF
}

module "aws_secretsmanager_irsa" {
  source  = "terraform-aws-modules/iam/aws//modules/iam-role-for-service-accounts-eks"
  version = "5.9.1"

  role_name = "ik-secretsmanager-role"

  role_policy_arns = {
    secrets_policy = aws_iam_policy.secrets_policy.arn
  }

  oidc_providers = {
    secrets = {
      provider_arn               = module.eks.oidc_provider_arn
      namespace_service_accounts = ["microservices:aline-secrets-sa"]
    }
  }
}

module "aws_load_balancer_controller_irsa_role" {
  source  = "terraform-aws-modules/iam/aws//modules/iam-role-for-service-accounts-eks"
  version = "5.9.1"

  create_role = var.create_cluster 

  role_name = var.in_testing ? "ik-load-balancer-controller-testing" : "ik-load-balancer-controller"

  attach_load_balancer_controller_policy = true

  oidc_providers = {
    ex = {
      provider_arn               = module.eks.oidc_provider_arn
      namespace_service_accounts = ["kube-system:aws-load-balancer-controller"]
    }
  }
}

module "cluster_autoscaler_irsa_role" {

  create_role = var.create_cluster 

  source  = "terraform-aws-modules/iam/aws//modules/iam-role-for-service-accounts-eks"
  version = "5.9.1"

  role_name                        = var.in_testing ? "ik-cluster-autoscaler-testing" : "ik-cluster-autoscaler"
  attach_cluster_autoscaler_policy = true
  cluster_autoscaler_cluster_ids   = [module.eks.cluster_name]

  oidc_providers = {
    ex = {
      provider_arn               = module.eks.oidc_provider_arn
      namespace_service_accounts = ["kube-system:cluster-autoscaler"]
    }
  }
}
