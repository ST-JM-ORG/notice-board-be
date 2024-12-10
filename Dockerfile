FROM openjdk:17-jdk

ENV SPRING_PROFILE dev
ENV SERVER_PORT 8080
ENV PROJECT_LOG_LEVEL info
ENV APP_HOME /home/ubuntu/docker/notice-board-be/

WORKDIR ${APP_HOME}
ARG JAR_FILE=notice-board-be-*.jar
COPY ./target/${JAR_FILE} ${APP_HOME}/notice-board-be.jar
ENTRYPOINT ["java", "-jar", "notice-board-be.jar"]

CMD ["-Xms256M", "-Xmx256M", "--spring.profiles.active=${SPRING_PROFILE}", "--server.port=${SERVER_PORT}", "--project.log.level=${PROJECT_LOG_LEVEL}"]
