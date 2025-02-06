# Use Eclipse Temurin JDK 21 as base image for building the app
FROM eclipse-temurin:21-jdk AS build

# Set working directory inside container
WORKDIR /app

# Copy Gradle/Maven build files to cache dependencies
COPY build.gradle settings.gradle gradlew ./
COPY gradle gradle
RUN ./gradlew build --no-daemon || true  # Cache dependencies first

# Copy source code and build the JAR
COPY . .
RUN ./gradlew clean bootJar --no-daemon

# Use a smaller runtime image
FROM eclipse-temurin:21-jre AS runtime

# Set working directory for runtime
WORKDIR /app

# Copy the built JAR from previous step
COPY --from=build /app/build/libs/iot-temp-mgmt-uk-biobank-test-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
