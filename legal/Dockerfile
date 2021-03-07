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

WORKDIR /
ADD src/main/kotlin/com/car/rental/legal/template/ ./legal/src/main/kotlin/com/car/rental/legal/template/

EXPOSE 8080
EXPOSE 8081
ENTRYPOINT ["java","-jar","/usr/local/lib/legal.jar","-Dspring.profiles.active=docker"]