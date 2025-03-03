# Stage 1: Build the JAR file using Maven
FROM --platform=linux/amd64 maven:3.8.6-eclipse-temurin-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and the src folder to the container
COPY pom.xml .
COPY src ./src
COPY . .

# Run the Maven build to create the JAR file
RUN mvn clean package -Pproduction -DskipTests

# Stage 2: Run the JAR file
FROM --platform=linux/amd64 eclipse-temurin:17-jre

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port 8081 (to match docker-compose.yml)
EXPOSE 8081

# Copy .env to the container for OpenAI API Key (ensure it's included in docker-compose)
COPY .env .env

# Set environment variable inside the container (alternative to --env-file in compose)
ENV OPENAI_API_KEY=${OPENAI_API_KEY}

# Set the entry point to run the JAR file
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
