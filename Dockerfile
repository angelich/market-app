FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY build/libs/market-app-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

CMD ["java", "-jar", "market-app-0.0.1-SNAPSHOT.jar"]
