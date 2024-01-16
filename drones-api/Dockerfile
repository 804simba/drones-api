FROM openjdk:17-slim

ENV JAVA_HOME=/usr/lib/jvm/default-jvm

WORKDIR /app/

COPY target/drones-api-0.0.1-SNAPSHOT.jar /app/drones-api.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/drones-api.jar"]