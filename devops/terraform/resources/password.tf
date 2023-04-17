resource "random_password" "initial_password" {
  length           = 40
  special          = true
  min_special      = 5
  override_special = "!#$%^&*()-_=+[]{}<>:?"
}

resource "random_password" "jwt_secret_key" {
  length           = 74
  special          = true
  min_special      = 5
  override_special = "!#$%^&*()-_=+[]{}<>:?"
}

resource "random_password" "encrypt_secret_key" {
  length           = 24
  special          = true
  min_special      = 5
  override_special = "!#$%^&*()-_=+[]{}<>:?"
}

resource "random_id" "id" {
  byte_length = 8
}

data "aws_partition" "current" {}
data "aws_region" "current" {}

resource "aws_secretsmanager_secret" "db_password" {
  name = "db-password-${random_id.id.hex}"
}

resource "aws_secretsmanager_secret_version" "db_password_value" {
  secret_id = aws_secretsmanager_secret.db_password.id

  secret_string = jsonencode(
    {
      username           = module.db.db_instance_username
      password           = module.db.db_instance_password
      engine             = "mysql"
#      host               = module.db.db_instance_endpoint
      host               = module.db.db_instance_address
      port               = tostring(module.db.db_instance_port)
      name               = module.db.db_instance_name
      jwtsecretkey     = random_password.jwt_secret_key.result
      encryptsecretkey = random_password.encrypt_secret_key.result
    }
  )
}

output "port"{
  value = module.db.db_instance_port
}
data "aws_serverlessapplicationrepository_application" "rotator" {
  application_id = "arn:aws:serverlessrepo:us-east-1:297356227824:applications/SecretsManagerRDSMySQLRotationSingleUser"
}

resource "aws_serverlessapplicationrepository_cloudformation_stack" "rotate-stack" {
  name             = "rotate-${random_id.id.hex}"
  application_id   = data.aws_serverlessapplicationrepository_application.rotator.application_id
  semantic_version = data.aws_serverlessapplicationrepository_application.rotator.semantic_version
  capabilities     = data.aws_serverlessapplicationrepository_application.rotator.required_capabilities

  parameters = {
    endpoint            = "https://secretsmanager.${data.aws_region.current.name}.${data.aws_partition.current.dns_suffix}"
    functionName        = "rotator-${random_id.id.hex}"
    vpcSubnetIds        = module.vpc.database_subnets[0]
    vpcSecurityGroupIds = module.rds-security-group.security_group_id
  }
}

resource "aws_secretsmanager_secret_rotation" "rotation" {
  secret_id           = aws_secretsmanager_secret_version.db_password_value.secret_id
  rotation_lambda_arn = aws_serverlessapplicationrepository_cloudformation_stack.rotate-stack.outputs.RotationLambdaARN

  rotation_rules {
    automatically_after_days = 14
  }
}

#resource "kubectl_manifest" "namespace" {
#
#  count = var.create_cluster ? 1 : 0
#
#  yaml_body = <<-EOF
#    apiVersion: v1
#    kind: Namespace
#    metadata:
#      name: microservices
#EOF
#
#  depends_on = [
#    module.eks
#  ]
#}



#resource "kubectl_manifest" "secret_service_account" {
#
#
#  yaml_body = <<-EOF
#apiVersion: v1
#kind: ServiceAccount
#metadata:
#  name: aline-secrets-sa 
#  namespace: microservices 
#  annotations:
#    eks.amazonaws.com/role-arn: ${module.aws_secretsmanager_irsa.iam_role_arn}
#EOF
#
#  depends_on = [
#    module.eks,
#  ]
#}
#
#resource "helm_release" "csi-secrets-store" {
#
#  name       = "csi-secrets-store"
#  repository = "https://kubernetes-sigs.github.io/secrets-store-csi-driver/charts"
#  chart      = "secrets-store-csi-driver"
#  namespace  = "kube-system"
#
#  set {
#    name  = "syncSecret.enabled"
#    value = true
#  }
#  set {
#    name  = "enableSecretRotation"
#    value = true
#  }
#
#}
#
#resource "kubectl_manifest" "aws-csi-driver" {
#  yaml_body = file("./misc/aws-provider-installer.yaml")
#}
#
#
#resource "kubectl_manifest" "aws-secrets" {
#
#  yaml_body = <<-EOF
#apiVersion: secrets-store.csi.x-k8s.io/v1
#kind: SecretProviderClass
#metadata:
#  namespace: microservices
#  name: aws-secrets
#spec:
#  provider: aws 
#  parameters:
#    objects: |
#      - objectName: ${aws_secretsmanager_secret.db_password.arn}
#        objectType: secretsmanager
#        jmesPath: 
#          - path: username
#            objectAlias: dbusername
#          - path: password 
#            objectAlias: dbpassword
#          - path: name
#            objectAlias: dbname
#          - path: host 
#            objectAlias: dbhost
#          - path: port
#            objectAlias: dbport
#          - path: jwtsecretkey
#            objectAlias: jwtsecretkey
#          - path: encryptsecretkey
#            objectAlias: encryptsecretkey
#  
#  secretObjects:
#    - secretName: aline-secrets
#      type: Opaque
#      data:
#        - objectName: dbusername
#          key: dbusername
#        - objectName: dbpassword
#          key: dbpassword
#        - objectName: dbname 
#          key: dbname
#        - objectName: dbhost
#          key: dbhost
#        - objectName: dbport
#          key: dbport
#        - objectName: jwtsecretkey 
#          key: jwtsecretkey
#        - objectName: encryptsecretkey 
#          key: encryptsecretkey
#  EOF
#
#}
