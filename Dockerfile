FROM openjdk:17-jdk-slim

COPY build/libs/auth-service-0.0.1-SNAPSHOT.jar /auth-service.jar

ENTRYPOINT ["java", "-jar", "/auth-service.jar"]