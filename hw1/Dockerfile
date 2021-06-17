FROM maven:3.8.1-openjdk-8-slim AS build

WORKDIR /app

COPY ./ ./

RUN mvn clean package -DskipTests

FROM adoptopenjdk/openjdk14


COPY --from=build app/target/*.jar /app.jar

CMD [ "java", "-jar", "/app.jar" ]