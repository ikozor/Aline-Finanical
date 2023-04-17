kubectl create secret docker-registry regcred \
  --docker-server=239153380322.dkr.ecr.us-east-1.amazonaws.com \
  --docker-username=AWS \
  --docker-password=$(aws ecr get-login-password) \
