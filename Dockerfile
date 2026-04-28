# Stage 1: Build
FROM maven:3.9.11-eclipse-temurin-25 AS build
WORKDIR /app

# Copy only pom.xml first for caching dependencies
COPY pom.xml ./
RUN mvn dependency:go-offline -B

# COPY source
COPY src ./src

# Build without tests
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:25-jdk-alpine
WORKDIR /app

# Copy built jar from build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]