FROM openjdk:8-jre-slim
COPY ./build/libs/findapple-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "-Xmx500m", "/app.jar"]
EXPOSE 8080