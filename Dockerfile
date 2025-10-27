# Build Stage
FROM maven:3.8.8-eclipse-temurin-17-alpine AS build

WORKDIR /app

# Cache dependencies based on pom.xml
COPY ./pom.xml ./
RUN --mount=type=cache,target=/root/.m2 mvn dependency:resolve dependency:resolve-plugins

# Build application
COPY ./src/ ./src/
RUN --mount=type=cache,target=/root/.m2 mvn clean package -DskipTests

# Runtime Stage
FROM bellsoft/liberica-runtime-container:jre-17-crac-cds-slim-musl AS runtime

COPY --from=build /app/target/*.jar /app/app.jar

EXPOSE 8080
CMD ["java", "-XX:+UseSerialGC", "-XX:+UseContainerSupport", "-jar", "/app/app.jar"]


