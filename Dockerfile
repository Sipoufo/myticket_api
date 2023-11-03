# Docker Build Maven Stage
FROM maven:3-openjdk-17 AS build
WORKDIR /opt/app
COPY ./ /opt/app
RUN mvn clean install -DskipTests

# Run spring boot in Docker
FROM openjdk:17-jdk
COPY --from=build /opt/app/target/myticket.jar myticket.jar
ENV PORT 8081
EXPOSE $PORT
ENTRYPOINT ["java","-jar","-Xmx1024M","-Dserver.port=${PORT}","myticket.jar"]
