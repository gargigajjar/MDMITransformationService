FROM alpine/git
WORKDIR /app
RUN git clone --branch threeohasjar https://github.com/MDMI/MDMITransformationService.git

FROM maven:3.9-eclipse-temurin-11
WORKDIR /app
COPY --from=0 /app/MDMITransformationService /app 
RUN mvn install 
ENTRYPOINT ["java","-jar","/app/target/org.mdmi.engine.jar"]

COPY ./src/main/resources/maps /maps/