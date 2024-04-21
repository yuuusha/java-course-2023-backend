FROM openjdk:21
LABEL authors="yuuusha"
WORKDIR /app
COPY target/scrapper.jar /app/scrapper.jar
EXPOSE 8090
EXPOSE 8001
CMD ["java", "-jar", "bot.jar"]
