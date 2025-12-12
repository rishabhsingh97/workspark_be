# ConfigService

This is a Config Server that provides centralized configuration management for your microservices.

## Prerequisites
-Java 21
-Maven
-Spring Boot 3.4.0
-Spring Cloud 2023.0.3 (for microservices support)
-Docker

## Building the project

### Docker

For running the docker 
```bash
docker-compose -f docker-compose.yml up -d

```
For checking the instance

```bash
docker ps

```

To build the project, run the following Maven command:

```bash
mvn clean install
```


## Requirements for Config Client

### The following dependencies are to be added in Config Client:

```
               </dependency>
			<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-bus-kafka</artifactId>
		</dependency>

        	<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-config</artifactId>
		</dependency>

        		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-actuator</artifactId>
		</dependency>

```

### Properties to be added in application.yml

```bash
spring:
  cloud:
    config:
      uri: http://localhost:8081/config
      label: latest

```

Have to annotate the Controller with 
_@RefreshScope_ 

By hitting the busrefresh endpoint the properties file of the clients get refreshed 

```bash
POST:
http://localhost:8081/config/actuator/busrefresh
```
Here,8081 is the port of the Config Server.