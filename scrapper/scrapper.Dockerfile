FROM openjdk:21
LABEL authors="Nick552"
WORKDIR /app
COPY target/scrapper.jar app/scrapper.jar
EXPOSE 8080
EXPOSE 8081
CMD ["java", "-jar", "app/scrapper.jar"]
