# API Gateway

Microservice responsible for the price calculation and the calls to the calendar microservice that may need a pre
request before being able to be executed. It also handles the two functions that check every day if there's been
completed services the day before, or there are services the next day, this way the required documents can be created
for it.

There are some calls, like the newService, that need some information before being able to do them. In this case we
would need the price and the free drivers. This microservice calculates the price taking into consideration the origin
and destination of the service, and adds it to the request. It also handles the pre request to the calendar to find
which are the drivers that are free to do the service that the user wants to create.

## Environment vars

- GOOGLE_APPLICATION_CREDENTIALS=api-gateway/scripts/car-rental.json
- SECRET_HASH=xxxxxxx   (used for the encryption and decryption of the user email password stored in firebase)
- google.client.client-secret=xxxxxx (GoogleAPI secret)
- HERE_API_KEY= API key for the Here maps platform.

## RabbitMQ

This microservice, as all the other ones, is using RabbitMQ to communicate between themselves.

RabbitMQ is an open-source general message broker, which supports protocols including MQTT, AMQP and STOMP. It can
handle high-throughput use cases, such as online payment processing. It can handle background jobs or act as a message
broker between microservices.

RabbitMQ is a good option for a simple publish/subscribe message broker, as it will perform better and have an easier
implementation, plus lots of support online due to the large userbase. Kafka would be better in case the messages would
have to be stored even after being read by the subscriber, like in case of metrics analytics.

The RabbitMQ URL is dependent on the profile we are working at, as if we are locally the `localhost` will be used, but
if it is running on Docker, the `rabbitmq` URL (of the Kubernetes cluster) will be used instead.

## Spring Profile

Profiles are a core feature of the Spring framework — allowing us to map our beans to different profiles — for example,
dev, test, and prod.

```yaml
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

The orchestrator, and all the other microservices, have a Dockerfile that builds the image that will later be used by
Kubernetes.

Important to notice that the dockerfile is setting the spring profile to "docker", to use the correct RabbitMQ URL.

```dockerfile
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
COPY --from=build /home/app/target/orchestrator-0.0.1-SNAPSHOT.jar /usr/local/lib/orchestrator.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/api-gateway.jar","-Dspring.profiles.active=docker"]
```

There is also, in the scripts folder, a docker-build.sh script to automatize the creation of the image with the
necessary tag, and its upload to the registry.

## Flows

Most of the logic of this flows is about doing a sequence of calls to different microservices to generate an end result.
The only "original" logic is about doing the price estimation of a service.

### Create New Service

- **Required data**: The request contains all the service data but the confirmedDatetime.
- **Functionality**: Create a new unconfirmed (not in calendar) service. Basically a request for service, that the
  company must accept/deny. Orchestrator generates a price estimation, sends the information to calendar, and once the
  event is created it sends the request to the email MS, to send the request email to the driver.
- **Involved services**: Database, Email MS, Calendar MS.
- **Response**: Success/Error message

### Confirm Service

- **Required data**: The request contains the service ID.
- **Functionality**: Once a service has been confirmed, a confirmedDatetime will be added to the Database table, and a
  Calendar event will be created, which will send invitations to the drivers, and a confirmation email to the client.
- **Involved services**: Database, Calendar API, Calendar MS and Email MS.
- **Response**: Success/Error message

### Update Service

- **Required data**: The request contains all the service data but the confirmedDatetime.
- **Functionality**: Updates the service that has been sent. In case it has a confirmedDatetime (which means it already
  has a calendar event), will also update the calendar event.
- **Involved services**: Database, Calendar API, Calendar MS.
- **Response**: Success/Error message

### Cronjob

- **Required data**: List of calendar events for the week.
- **Functionality**: Calls the legal microservice to generate the Route Paper, in case that there is a new service the
  next day. If instead, there was a completed service the day before, it calls the legal service to generate the
  invoice. After the file has been received, an email with the file and a custom message is sent to the driver or the
  client, depending on the case.
- **Involved services**: Legal MS, Calendar MS, Email MS and Calendar API
- **Response**: Success/Error message