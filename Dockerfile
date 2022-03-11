FROM openjdk:11.0.6-jdk
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY maps maps
ENTRYPOINT ["java","-jar","/app.jar"]