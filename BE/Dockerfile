# Stage 1: Build the application
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app

# Copy source code
COPY . .

# Fix Windows line endings and permissions for gradlew
RUN sed -i 's/\r$//' gradlew && chmod +x gradlew

# Build the JAR file using the wrapper
RUN ./gradlew bootJar -x test --no-daemon

# Stage 2: Create the runtime image
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the server port
EXPOSE 9999

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
