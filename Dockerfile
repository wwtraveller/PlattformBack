# Use an official Java runtime as a parent image
FROM openjdk:17-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . .

# Build the application using Maven wrapper (if you are using mvnw)
RUN ./mvnw package -DskipTests

# Run the application
CMD ["java", "-jar", "target/platform-0.0.1-SNAPSHOT.jar"]

RUN chmod +x ./mvnw
