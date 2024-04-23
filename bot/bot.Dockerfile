FROM openjdk:21
LABEL authors="Nick552"
WORKDIR /app
COPY target/bot.jar app/bot.jar
EXPOSE 8090
EXPOSE 8091
CMD ["java", "-jar", "app/bot.jar"]
