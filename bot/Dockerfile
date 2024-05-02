FROM openjdk:21 as builder
COPY bot/target/bot.jar .
RUN java -Djarmode=layertools -jar bot.jar extract

FROM openjdk:21-slim
LABEL authors="yuuusha"
COPY --from=builder dependencies/ ./
COPY --from=builder snapshot-dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder application/ ./
EXPOSE 8090
EXPOSE 8001
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
