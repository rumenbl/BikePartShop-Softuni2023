FROM maven:3.9.5-eclipse-temurin-17-alpine AS build

ENV BUILD_HOME=/build
RUN mkdir -p "$BUILD_HOME"

WORKDIR $BUILD_HOME
COPY pom.xml $BUILD_HOME
COPY src $BUILD_HOME/src

RUN mvn -f "$BUILD_HOME"/pom.xml clean package -DskipTests

FROM eclipse-temurin:17-alpine

WORKDIR /app
COPY --from=build /build/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
