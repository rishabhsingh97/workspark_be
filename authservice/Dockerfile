# Stage 1: Build
FROM maven:3.9.9-amazoncorretto-21 AS build
WORKDIR /app
COPY . /app
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM amazoncorretto:21
WORKDIR /app
COPY --from=build /app/target/authservice-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8082
CMD ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "app.jar"]
