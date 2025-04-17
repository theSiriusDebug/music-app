FROM maven:3.8.6-openjdk-17-slim AS build

WORKDIR /app

COPY src .

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY --from=build /app/target/music-sync-backend.jar /app/music-sync-backend.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "music-sync-backend.jar"]