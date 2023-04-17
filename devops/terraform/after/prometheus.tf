#resource "helm_release" "prometheus_deployment" {
#  name = "prometheus"
#
#  count = var.create_cluster && var.monitoring ? 1 : 0 
#
#  repository = "prometheus-community"
#  chart      = "kube-prometheus-stack"
#  version = "16.10.0"
#
#  values = [
#    <<-EOF
#        ---
#        defaultRules:
#          rules:
#            etcd: false
#            kubeScheduler: false
#        kubeControllerManager:
#          enabled: false
#        kubeEtcd:
#          enabled: false
#        kubeScheduler:
#          enabled: false
#        prometheus:
#          prometheusSpec:
#            serviceMonitorSelector:
#              matchLabels:
#                prometheus: devops
#        commonLabels:
#          prometheus: devops
#        grafana:
#          adminPassword: ${var.grafana_password}
#    EOF
#  ]  
#
#
#  namespace  = "monitoring"
#  create_namespace = true
#
#  wait = true
#
#  depends_on = [
#    module.eks,
#    helm_release.aline-financial
#  ]
#}
