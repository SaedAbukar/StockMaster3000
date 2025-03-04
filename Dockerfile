# Stage 1: Build the JAR file using Maven
FROM maven:3.8.6-eclipse-temurin-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and the src folder to the container
COPY pom.xml .
COPY src ./src

# Run the Maven build to create the JAR file
# Delete the test skipper later
RUN mvn clean package -Pproduction -DskipTests

# Stage 2: Run the JAR file
FROM eclipse-temurin:17-jre

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port 8081 (to match docker-compose.yml)
EXPOSE 8081

# Set the entry point to run the JAR file
ENTRYPOINT ["java", "-jar", "/app.jar"]