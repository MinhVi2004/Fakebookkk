# Use Java 17
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy everything into image
COPY . .

# Build Spring Boot project using Maven Wrapper
RUN ./mvnw clean package -DskipTests

# Run the built JAR file (change the name if yours is different)
CMD ["java", "-jar", "target/backend-0.0.1-SNAPSHOT.jar"]
