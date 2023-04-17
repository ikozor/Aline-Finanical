variable "global_tags" {
  description = "Default tags to apply to all the resources"
  type        = any
  default = {
    "env"     = "Development"
    "Owner" = "Ilya Kozorezov"
    "project" = "Aline Financial Microservices"
  }
}

variable "in_testing" {
  type    = bool
  default = false
}

# Variables for creds
variable "aws_region" {
  description = "region for all services"
  type        = string
  default     = "us-east-1"
}

# Variables for vpc
variable "create_vpc" {
  description = "If the vpc should be created"
  type        = bool
  default     = true
}

variable "vpc_name" {
  description = "name of the vpc"
  type        = string
  default     = "ik-aline-financial"
}

variable "vpc_availability_zones" {
  description = "availibility zones of the vpc"
  type        = list(string)
  default     = ["us-west-2a", "us-west-2b"]
}

# Variables for eks cluster
variable "create_cluster" {
  description = "If the eks cluster should be created"
  type        = bool
  default     = true
}

variable "cluster_name" {
  description = "name of the eks cluster"
  type        = string
  default     = "ik-aline-financial"
}

variable "eks_min_nodes" {
  description = "Minimum number of nodes for nodegroup"
  type        = number
  default     = 1
}

variable "eks_max_nodes" {
  description = "Maximum number of nodes for nodegroup"
  type        = number
  default     = 6
}

variable "eks_desired_nodes" {
  description = "desired number of nodes for nodegroup"
  type        = number
  default     = 2
}

variable "eks_node_instance_types" {
  description = "instance type for eks nodes"
  type        = list(string)
  default     = ["t2.medium"]
}

# Variables for rds db
variable "create_db" {
  description = "If the rds db should be created"
  type        = bool
  default     = true
}

variable "db_username" {
  description = "username for database user"
  type        = string
}

variable "db_name" {
  description = "name for the database"
  type        = string
}

variable "rds_name" {
  description = "Name of the rds"
  type        = string
  default     = "ik-aline-financial"
}

variable "rds_allocated_storage" {
  description = "allocated storage (in GB) for rds"
  type        = number
  default     = 20
}

variable "rds_max_allocated_storage" {
  description = "max allocated storage (in GB) for rds"
  type        = number
  default     = 20
}

variable "rds_instance_class" {
  description = "instance class for rds db"
  type        = string
  default     = "db.t3.small"
}

# variable for installing the application
variable "install_application" {
  description = "if the aline financial application should be installed"
  type        = bool
  default     = false
}

variable "autoscale" {
  description = "Install autoscaling for cluster"
  type        = bool
  default     = true
}

variable "loadbalancer" {
  description = "Install alb controller for cluster"
  type        = bool
  default     = true
}

variable "monitoring" {
  description = "Install monitoring for cluster"
  type        = bool
  default     = true
}

variable "grafana_password" {
    description = "Password for grafana"
    type        = string 
    default     = "testtest123"
} 

variable "route53_zone" {
    description = "Id of the route53 zone"
    type        = string 
    default     = "Z0695888OWKTEQ5IMLUF"
} 

variable "eks_domain_name" {
    description = "domain name for aline financial for eks"
    type        = string 
    default     = "eks.alinefinancial.cloud"
} 

