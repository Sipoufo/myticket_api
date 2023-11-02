FROM openjdk:17
EXPOSE 8080
ADD target/myticket-integration.jar myticket-integration.jar
ENTRYPOINT ["java", "-jar", "/myticket-integration.jar"]