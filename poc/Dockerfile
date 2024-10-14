FROM maven:3-eclipse-temurin-17 as build

WORKDIR /poc
COPY . .
RUN mvn clean install -DskipTests -U


FROM openjdk:17
COPY --from=build poc/target/poc-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8090

ENTRYPOINT ["java", "-jar", "/app.jar"]