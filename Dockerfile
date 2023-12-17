FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/api-gateway-service-1.0.jar ApiGateWayService.jar
ENTRYPOINT ["java", "-jar", "ApiGateWayService.jar"]