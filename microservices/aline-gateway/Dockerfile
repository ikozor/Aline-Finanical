# Build the application
FROM maven:3.8.6-eclipse-temurin-11-alpine AS build

COPY . /aline-gateway
WORKDIR /aline-gateway

RUN mvn -Dmaven.test.skip package

# Run application
FROM eclipse-temurin:11-jre-alpine 

WORKDIR /app/
COPY --from=build /aline-gateway/target/aline-gateway-*.jar application.jar

CMD java -jar application.jar

