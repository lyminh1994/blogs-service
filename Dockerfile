FROM eclipse-temurin:17.0.5_8-jre-alpine
LABEL authors="lyminh"

COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]