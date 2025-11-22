FROM eclipse-temurin:17-jdk-alpine
ARG JAR_FILE=target/*.jar
WORKDIR /app
COPY target/login-app-1.0.0.jar app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]
