# Use official OpenJDK 17 slim image
FROM openjdk:17-jdk-slim

# Set working director
WORKDIR /app

# Copy project jar (update the jar name if needed)
COPY target/waarc-backend.jar app.jar

# Expose port (Render will override with $PORT)
EXPOSE 7001

# Set environment variables from .env automatically (Render does this)
ENV PORT=7001

# Start the application
CMD ["sh", "-c", "java -jar app.jar"]