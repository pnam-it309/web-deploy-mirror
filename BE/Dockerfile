# Development Stage
FROM eclipse-temurin:17-jdk-jammy AS development
WORKDIR /app
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*
COPY . .
RUN sed -i 's/\r$//' gradlew && chmod +x gradlew
EXPOSE 9999
CMD ["./gradlew", "bootRun", "--no-daemon"]

# Build Stage
FROM development AS build
RUN ./gradlew bootJar -x test --no-daemon

# Production Stage
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 9999
ENTRYPOINT ["java", "-jar", "app.jar"]
