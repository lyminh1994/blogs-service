FROM eclipse-temurin:11-alpine
COPY target/blogs-service-1.0.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
