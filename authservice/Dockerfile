# Stage 1: Build
FROM maven:3.9.9-amazoncorretto-21 AS build
WORKDIR /app

# Copy application source code
COPY . /app

ARG GITLAB_PERSONAL_ACCESS_TOKEN
ENV GITLAB_PERSONAL_ACCESS_TOKEN=${GITLAB_PERSONAL_ACCESS_TOKEN}

# Copy Maven settings for custom repository configurations
COPY settings.xml /root/.m2/settings.xml

# Switch to root user for managing certificates
USER root

# Copy and import the certificate into the Java keystore
COPY certificates/gitlab.mindfire.co.in.crt /tmp/gitlab_mindfire.crt
RUN keytool -keystore $JAVA_HOME/lib/security/cacerts \
    -storepass changeit -noprompt -trustcacerts \
    -importcert -alias gitlab_mindfire_co_in -file /tmp/gitlab_mindfire.crt

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM amazoncorretto:21
WORKDIR /app

# Copy the built application from the build stage
COPY --from=build /app/target/authenticationservice-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 8082

# Set the default command to run the application
CMD ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "app.jar"]
