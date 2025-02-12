# Stage 1: Build the JAR file using Maven
FROM --platform=linux/amd64 maven:3.8.8-eclipse-temurin-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and the src folder to the container
COPY pom.xml .
COPY src ./src

# Ensure target directory exists before build
RUN mkdir -p /app/target

# ✅ Run Maven using Bash instead of `/bin/sh`
RUN ["/bin/bash", "-c", "mvn clean package -Pproduction -DskipTests"]

# Stage 2: Run the JAR file
FROM --platform=linux/amd64 eclipse-temurin:17-jre

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar /app.jar

# Expose port 8081 (to match docker-compose.yml)
EXPOSE 8081

# Run the application
CMD ["sh", "-c", "if [ -f /app.jar ]; then java -jar /app.jar; else echo 'ERROR: JAR file missing'; exit 1; fi"]
