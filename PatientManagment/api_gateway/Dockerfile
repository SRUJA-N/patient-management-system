# Stage 1: Build the application
# Use Maven with Java 21
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom.xml first for dependency caching
COPY pom.xml .
# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the jar, skipping tests to save time
RUN mvn clean package -DskipTests

# Stage 2: Run the application
# Use Java 21 Alpine runtime
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copy the built jar from the 'build' stage
COPY --from=build /app/target/*.jar app.jar

# Expose API Gateway port
EXPOSE 4004

ENTRYPOINT ["java", "-jar", "app.jar"]
