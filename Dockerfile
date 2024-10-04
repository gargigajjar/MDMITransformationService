FROM maven:3.9-eclipse-temurin-11
WORKDIR /app
COPY / /app 
ENTRYPOINT ["java","-jar","/app/target/org.mdmi.transformation.service.jar"]