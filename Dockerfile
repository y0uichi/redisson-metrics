FROM eclipse-temurin:8-jre
WORKDIR /app
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]