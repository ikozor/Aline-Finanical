# Build application
FROM maven:3.8.6-eclipse-temurin-11-alpine AS build

ARG service_type
ENV service_type=$service_type

COPY . /aline-${service_type}-microservice
WORKDIR /aline-${service_type}-microservice/

RUN mvn -Dmaven.test.skip package

# Run application
FROM eclipse-temurin:11-jre-alpine as prod

ARG service_type
ARG app_port

ENV service_type=${service_type}
ENV app_port=${app_port}

WORKDIR /app/

COPY --from=build /aline-${service_type}-microservice/${service_type}-microservice/target/${service_type}-microservice-*.jar application.jar

EXPOSE ${app_port} 

CMD java -jar application.jar

