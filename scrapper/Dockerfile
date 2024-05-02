FROM openjdk:21 as builder
COPY target/scrapper.jar .
RUN java -Djarmode=layertools -jar scrapper.jar extract

FROM openjdk:21-slim
LABEL authors="yuuusha"
COPY --from=builder dependencies/ ./
COPY --from=builder snapshot-dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder application/ ./
EXPOSE 8090
EXPOSE 8001
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
