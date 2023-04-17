provider "aws" {
  region = var.aws_region
}

terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.0"
    }
    kubernetes = {
      source  = "hashicorp/kubernetes"
      version = ">= 2.0"
    }
    kubectl = {
      source  = "gavinbunney/kubectl"
      version = ">= 1.14.0"
    }
    helm = {
      source  = "hashicorp/helm"
      version = ">= 2.7.0"
    }
    null = {
      source  = "hashicorp/null"
      version = ">= 3.2.1"
    }
    random = {
      source  = "hashicorp/random"
      version = ">= 3.4.3"
    }
  }

  backend "s3" {
    bucket = "ik-aline-terraform"
    region = "us-east-1"
    key    = "ik-aline-financial.tfstate"
  }
  required_version = "~> 1.0"
}

#provider "helm" {
#  kubernetes {
#    host                   = data.aws_eks_cluster.default.endpoint
#    cluster_ca_certificate = base64decode(data.aws_eks_cluster.default.certificate_authority[0].data)
#    exec {
#      api_version = "client.authentication.k8s.io/v1beta1"
#      args        = ["eks", "get-token", "--cluster-name", data.aws_eks_cluster.default.id]
#      command     = "aws"
#    }
#  }
#}

provider "kubectl" {
  host                   = data.aws_eks_cluster.default.endpoint
  cluster_ca_certificate = base64decode(data.aws_eks_cluster.default.certificate_authority[0].data)
  load_config_file       = false

  exec {
    api_version = "client.authentication.k8s.io/v1beta1"
    args        = ["eks", "get-token", "--cluster-name", data.aws_eks_cluster.default.id]
    command     = "aws"
  }
}
