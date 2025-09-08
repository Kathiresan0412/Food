# Multi-stage Dockerfile for Spring Boot app built with Gradle

# ===== BUILD STAGE =====
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

# Cache gradle wrapper and dependencies first
COPY gradlew .
COPY gradle gradle
RUN chmod +x gradlew

# Copy build scripts and sources
COPY build.gradle.kts settings.gradle.kts ./
COPY src src

# Build the bootable jar (skip tests by default for faster container builds)
RUN ./gradlew clean bootJar -x test

# ===== RUN STAGE =====
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the fat jar produced by Spring Boot plugin
COPY --from=builder /app/build/libs/*-SNAPSHOT.jar app.jar

EXPOSE 8080

# If you deploy behind Heroku/Render/etc., they can inject PORT env
ENV PORT=8080

ENTRYPOINT ["java","-jar","/app/app.jar"]


