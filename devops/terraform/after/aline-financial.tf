resource "kubectl_manifest" "namespace" {

  count = var.create_cluster && var.install_application ? 1 : 0

  yaml_body = <<-EOF
    apiVersion: v1
    kind: Namespace
    metadata:
      name: microservices
EOF

  depends_on = [
    module.eks
  ]
}

resource "null_resource" "update_secret" {
  count = var.create_cluster && var.install_application ? 1 : 0
  provisioner "local-exec" {
    command = "python misc/update_secrets.py ${module.db.db_instance_password}"
  }
}

resource "kubectl_manifest" "secrets" {

  count = var.create_cluster && var.install_application ? 1 : 0

  yaml_body = file("./misc/secrets.yaml")

  depends_on = [
    module.eks,
    module.db,
    null_resource.update_secret,
    kubectl_manifest.namespace
  ]
}

resource "helm_release" "aline-financial" {

  count = var.create_cluster && var.install_application ? 1 : 0

  name = "aline-financial"

  chart = "../../kubernetes/helm-chart/aline-financial"

  depends_on = [
    module.eks,
    module.db,
    kubectl_manifest.secrets,
    kubectl_manifest.namespace,
    helm_release.aws_load_balancer_controller
  ]
}

