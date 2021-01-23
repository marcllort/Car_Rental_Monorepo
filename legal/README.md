# Legal
When doing transportation services, there is also the need to create some simple legal documents, where it states the passengers, origin, and destination points. The invoice of the
service must also be generated. This microservice will deal with the creation of these documents.

The microservice is coded in Kotlin.

## RabbitMQ
This microservice, as all the other ones, is using RabbitMQ to communicate between themselves.

RabbitMQ is an open-source general message broker, which supports protocols including MQTT, AMQP and STOMP. 
It can handle high-throughput use cases, such as online payment processing. 
It can handle background jobs or act as a message broker between microservices. 

RabbitMQ is a good option for a simple publish/subscribe message broker, as it will perform better and have an easier implementation, 
plus lots of support online due to the large userbase. Kafka would be better in case the messages would have to be stored even after being read by the 
subscriber, like in case of metrics analytics.

## Spring Profile
Profiles are a core feature of the Spring framework — allowing us to map our beans to different profiles — for example, dev, test, and prod.

```
spring:
   profiles:
      active: "dev"
   rabbitmq:
      host: localhost
      username: guest
      password: guest
      virtual-host: /
      port: 5672
      
      ...
```

In this project there are two Spring profiles: "dev" and "docker". Most of the configurations are the same, but one: spring.rabbitmq.host

- dev: When running on dev profile, which is when developing/running the project locally, the URL for RabbitMQ must be set to localhost, as the local instance will be used.
- docker: It only runs in the docker profile when running on Kubernetes. Then, the url must be set to "rabbitmq", which is the name set for the loadbalancer in Kubernetes. Without this change, the project wouldn't be able to connect to RabbitMQ.

To define the profile to use, is done through the VM parameter: 

`-Dspring.profiles.active=dev`

## Docker
As Kubernetes is being used to host the infrastructure of the project, all the microservices must be containerized.

The legal, and all the other microservices, have a Dockerfile that builds the image that will later be used by Kubernetes.

Important to notice that the dockerfile is setting the spring profile to "docker", to use the correct RabbitMQ URL.
```
#
# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM openjdk:11-jre-slim
COPY --from=build /home/app/target/legal-0.0.1-SNAPSHOT.jar /usr/local/lib/legal.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/legal.jar","-Dspring.profiles.active=docker"]
```

There is also, in the scripts folder, a docker-build.sh script to automatize the creation of the image with the necessary tag, and its upload to the registry.