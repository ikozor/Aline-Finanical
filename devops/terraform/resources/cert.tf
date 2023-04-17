resource "aws_acm_certificate" "alb_certificate" {
  domain_name       = var.eks_domain_name
  validation_method = "DNS"
  lifecycle {
    create_before_destroy = true
  }
}

resource "aws_route53_record" "alb_cert_dns" {
  allow_overwrite = true
  name =  tolist(aws_acm_certificate.alb_certificate.domain_validation_options)[0].resource_record_name
  records = [tolist(aws_acm_certificate.alb_certificate.domain_validation_options)[0].resource_record_value]
  type = tolist(aws_acm_certificate.alb_certificate.domain_validation_options)[0].resource_record_type
  zone_id = var.route53_zone
  ttl = 60
}

resource "aws_acm_certificate_validation" "alb_cert_validate" {
  certificate_arn = aws_acm_certificate.alb_certificate.arn
  validation_record_fqdns = [aws_route53_record.alb_cert_dns.fqdn]
}
