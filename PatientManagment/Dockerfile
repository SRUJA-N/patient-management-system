# Stage 1: Build the application
# UPGRADE: Use Maven with Java 21
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY patient-service ./patient-service
COPY billing-service ./billing-service
# Build the jar, skipping tests to save time
RUN mvn clean package -DskipTests

# Stage 2: Run the application
# UPGRADE: Use Java 21 Alpine runtime
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
# Copy the built jar from the 'build' stage
COPY --from=build /app/patient-service/target/*.jar app.jar
EXPOSE 4000
ENTRYPOINT ["java", "-jar", "app.jar"]
