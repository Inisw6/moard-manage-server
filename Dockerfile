# Build stage
FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle build --no-daemon -x test

# Run stage
FROM amazoncorretto:17-alpine
WORKDIR /app

# Install necessary packages
RUN apk add --no-cache bash

# Copy the built jar
COPY --from=build /app/build/libs/*.jar app.jar

# Copy .env file
COPY .env .env

# Environment variables for OAuth and API
ENV NAVER_CLIENT_ID=""
ENV NAVER_CLIENT_SECRET=""
ENV YOUTUBE_API_KEY=""

# Expose port
EXPOSE 8080

# Use JSON array format for ENTRYPOINT
ENTRYPOINT ["java", "-jar", "app.jar"] 