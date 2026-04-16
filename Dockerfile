# === Stage 1: Build the Application ===
# Use a Maven-enabled JDK 17 image to build the .jar file
FROM maven:3.9-eclipse-temurin-17 AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of the source code and build the application
COPY src ./src
RUN mvn clean install

# === Stage 2: Create the Final Image ===
# Use a minimal Java 17 runtime image
FROM eclipse-temurin:17-jre-focal

# Set the working directory
WORKDIR /app

# Copy the built .jar file from the 'builder' stage
COPY --from=builder /app/target/verto-shop-0.0.1-SNAPSHOT.jar ./app.jar

# Expose the port the application runs on
EXPOSE 8080

# The command to run the application
ENTRYPOINT ["java", "-jar", "./app.jar"]