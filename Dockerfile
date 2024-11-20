FROM alpine/git
WORKDIR /app
RUN git clone --branch develop https://github.com/MDMI/MDMITransformationService.git

FROM maven:3.9-eclipse-temurin-21
WORKDIR /app
COPY --from=0 /app/MDMITransformationService /app
RUN mvn install
ENTRYPOINT ["java","-jar","/app/target/org.mdmi.transformation.service.jar"]