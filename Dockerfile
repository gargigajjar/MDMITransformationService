FROM openjdk:11.0.6-jdk
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar","-DMDMI_LOG_LEVEL=TRACE"]
