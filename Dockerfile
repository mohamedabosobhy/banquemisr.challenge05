FROM openjdk:11-jdk-slim
WORKDIR /app
COPY ../target/taskManagmentSystem-1.0.0.jar app.jar
COPY roles-config.json roles-config.json

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]