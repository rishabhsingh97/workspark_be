# Stage 1: Build
FROM maven:3.9.9-amazoncorretto-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests
EXPOSE 8761

# Stage 2: Run
FROM amazoncorretto:21
WORKDIR /app
COPY --from=build /app/target/discoveryservice-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]
