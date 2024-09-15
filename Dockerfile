# Use an official Java runtime as a parent image
FROM openjdk:17-slim

# Set the working directory in the container
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . .

# Update package list and install postgresql-client using apt-get for Debian-based images
RUN apt-get update && apt-get install -y postgresql-client

# Give execution permission to mvnw
RUN chmod +x ./mvnw

# Build the application using Maven wrapper (if you are using mvnw)
RUN ./mvnw package -DskipTests

# Run the application
CMD ["java", "-jar", "target/platform-0.0.1-SNAPSHOT.jar"]

