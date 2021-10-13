FROM openjdk:8-jre-slim
COPY ./build/libs/findapple-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "-Xmx200m", "/app.jar"]
EXPOSE 7780