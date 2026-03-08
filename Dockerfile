# Use official OpenJDK 17 slim variant
FROM openjdk:17-jdk-bullseye

# Set working directory
WORKDIR /app

# Copy project jar (update the jar name if needed)
COPY target/waarc-backend.jar app.jar

# Expose port (Render will override with $PORT)
EXPOSE 7001

# Start the application
CMD ["sh", "-c", "java -jar app.jar"]