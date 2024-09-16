# Use a base image with Java 17
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container
COPY target/starwars-api-0.0.1.jar /app/starwars-api-0.0.1.jar

# Expose the port on which the application will run
EXPOSE 8080

# Command to run the JAR file
ENTRYPOINT ["java", "-jar", "/app/starwars-api-0.0.1.jar"]

