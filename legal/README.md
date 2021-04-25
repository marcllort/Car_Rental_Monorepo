# Legal

When doing transportation services, there is also the need to create some simple legal documents, where it states the
passengers, origin, and destination points. The invoice for the service must also be generated. This microservice deals
with the creation of these documents.

The microservice is coded in Kotlin.

## Rest Endpoint

This is the only microservice that does not use RabbitMQ. This is because it generates files, and we cannot transfer
them using a message broker. Instead, with a POST endpoint to this microservice, we send all the information required to
generate the PDF file, and then we receive the PDF already filled.

## Spring Profile

Profiles are a core feature of the Spring framework — allowing us to map our beans to different profiles — for example,
dev, test, and prod.

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

In this project there are two Spring profiles: "dev" and "docker". Most of the configurations are the same, but one:
spring.rabbitmq.host

- dev: When running on dev profile, which is when developing/running the project locally, the URL for RabbitMQ must be
  set to localhost, as the local instance will be used.
- docker: It only runs in the docker profile when running on Kubernetes. Then, the url must be set to "rabbitmq", which
  is the name set for the loadbalancer in Kubernetes. Without this change, the project wouldn't be able to connect to
  RabbitMQ.

To define the profile to use, is done through the VM parameter:

`-Dspring.profiles.active=dev`

## Docker

As Kubernetes is being used to host the infrastructure of the project, all the microservices must be containerized.

The legal, and all the other microservices, have a Dockerfile that builds the image that will later be used by
Kubernetes.

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

There is also, in the scripts folder, a docker-build.sh script to automatize the creation of the image with the
necessary tag, and its upload to the registry.

## Environment Variables

No environment variables are used in this microservice, as there is no interaction with neither DB nor external APIs.

## Templates

As the responsible for the creation of PDFs, it needs two templates to fill them with the proper information and later
send it to the email Microservice. The templates are for the invoice, and the route paper. These templates have some
fields pre created, with the corresponding ids. Using the Java library named apache.pdfbox, this fields can be filled
with the proper information about the service.

## Flows

This microservice is rather simple when it comes to its logic. The only needed flows are for the invoice and route
paper.

### Invoice

- **Required data**: The request contains all the service data, price, drivers and company name.
- **Functionality**: Generates the invoice PDF that contains the service driver, price, origin/destination and price.
- **Involved services**: Email MS
- **Response**: Generated PDF.

### Route Paper

- **Required data**: The request contains all the service data, price, drivers and company name.
- **Functionality**: Generates the route paper PDF that contains the name of the clients, the driver,
  origin/destination, and pick-up time.
- **Involved services**: Email MS
- **Response**: Generated PDF.