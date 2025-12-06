# Stage 1: Build the application
FROM gradle:jdk17 AS build
WORKDIR /app

# Copy source code
COPY . .

# Build the JAR file, skipping tests to avoid environment issues during build
RUN gradle bootJar -x test --no-daemon

# Stage 2: Create the runtime image
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the server port
EXPOSE 9999

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
