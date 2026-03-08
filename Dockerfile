# Use Eclipse Temurin JDK 17 base image
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy the built jar file
COPY target/waarc-backend.jar app.jar

# Expose the port (Render will override it with $PORT)
EXPOSE 7001

# Run the application
CMD ["sh", "-c", "java -jar app.jar"]