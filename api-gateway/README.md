# API Gateway

Microservice responsible for the authentication of the user, and protecting the endpoints of the backend.

To do the authentication, Firebase (from Google) is used to provide a token, and also allow for advanced sign-in
options, and the same token is being used when the authenticated user tries to do a call to the API.

It checks if the user is logged in, and has the necessary role/permissions to do the requested call/action.

## Endpoints

- /public/covid
- /public/data

- /protected/data
- /protected/calendar
- /protected/
- /protected/

- /super/user
- /super/list-users
- /super/create-user
- /super/update-user
- /super/delete-user
- /super/set-claim
- /super/activate-account
- /super/reset-password
- /super/revoke-token

- /session/login
- /session/logout
- /session/me
- /session/create/token

### Firebase Authentication for Spring boot

Firebase is a backendless platform to run applications without a dedicated backend. But, sometimes you may need to
communicate with API of an existing backend, or you may want a dedicated backend to perform operations that cannot be
done through firebase infrastructure.

This **Spring Boot Starter** is perfect for such situations when you want to extend firebase's authentication mechanism
with **Spring Security** to seamlessly create and use protected rest API's.

### Configuration

- Be sure to add the following environment variable globally or project specific run configuration environment
  variable `GOOGLE_APPLICATION_CREDENTIALS=path_to_firebase_server_config.json`

- The starter can be configured to use firebase session as client side / strictly server side or both together.
- Http Only / Secure enabled Session cookies may not work as expected in development hosts (localhost, 120.0.0.1).
  Adding self-signed ssl certificate with reverse proxied host will work perfectly fine. Read this article
  => [Local Domain Names with SSL for development applications ](https://thepro.io/post/local-domain-names-with-ssl-for-local-development-applications-LG)
- Following application properties can edited to customize for your needs.
- In the (not public) '/creds' folder, the Firebase and TLS secrets are stored, which as explained earlier, are passed
  as parameters when executing the project.

### Role Management

- Roles can be added through `SecurityRoleService` during registration of user or manually managed by Super admins
- Super Admins are defined through application property `security.super-admins`
- With roles integrated with spring security, spring authorization annotations
  like **`@Secured, @RolesAllowed, @PreAuthorize, @PostAuthorized`** etc will work out of the box.
- I personally like to define per role annotations like **`@IsSuper`** etc for the sake of simplicity.

```
    @GetMapping("data")
	@isSuper
	public String getProtectedData() {
		return "You have accessed seller only data from spring boot";
	}
```

## Environment vars

- GOOGLE_APPLICATION_CREDENTIALS=api-gateway/scripts/car-rental.json
- SUPER_ADMINS=mac12llm2@gmail.com
- CORS_DOMAIN=http://localhost:63343
- SECRET_HASH=xxxxxxx   (used for the encryption and decryption of the user email password stored in firebase)

## RabbitMQ

This microservice, as all the other ones, is using RabbitMQ to communicate between themselves.

RabbitMQ is an open-source general message broker, which supports protocols including MQTT, AMQP and STOMP. It can
handle high-throughput use cases, such as online payment processing. It can handle background jobs or act as a message
broker between microservices.

RabbitMQ is a good option for a simple publish/subscribe message broker, as it will perform better and have an easier
implementation, plus lots of support online due to the large userbase. Kafka would be better in case the messages would
have to be stored even after being read by the subscriber, like in case of metrics analytics.

## Encryption/Decryption

This service needs to store the email password of the user in the Firestore storage DB. To do so, it must be encrypted,
so it is not in plain text and insecure. For this, the AES encryption is being used. The password is hashed with the
password received as an environment variable.

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

The api-gw, and all the other microservices, have a Dockerfile that builds the image that will later be used by
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
COPY --from=build /home/app/target/api-gateway-0.0.1-SNAPSHOT.jar /usr/local/lib/api-gateway.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/api-gateway.jar","-Dspring.profiles.active=docker"]
```

There is also, in the scripts folder, a docker-build.sh script to automatize the creation of the image with the
necessary tag, and its upload to the registry.