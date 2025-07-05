FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app
COPY . .

RUN ./mvnw clean package -DskipTests

# Tăng heap size lên 512MB hoặc 1GB
CMD ["java", "-Xmx512m", "-jar", "target/backend-0.0.1-SNAPSHOT.jar"]
