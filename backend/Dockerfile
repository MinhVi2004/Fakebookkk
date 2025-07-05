# syntax=docker/dockerfile:1
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copy everything
COPY . .

# Build project
RUN ./mvnw clean install

# Run the jar file
CMD ["java", "-jar", "target/*.jar"]
