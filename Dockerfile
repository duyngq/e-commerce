# Use an official OpenJDK 17 runtime as a parent image
FROM openjdk:17-jdk-slim

# Create an app directory
WORKDIR /app

# Copy the Spring Boot jar to the container
COPY target/e-store-0.0.1-SNAPSHOT.jar /app/e-store.jar

# Expose port 8080 (the default for Spring Boot)
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "/app/e-store.jar"]