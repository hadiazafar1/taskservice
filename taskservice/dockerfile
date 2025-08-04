# Dockerfile

# Use a lightweight Java image
FROM eclipse-temurin:21-jdk-alpine

# Set app directory
WORKDIR /app

# Copy the built jar file
COPY target/taskservice-0.0.1-SNAPSHOT.jar app.jar

# Expose port (change if needed)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
