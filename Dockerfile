# Use Eclipse Temurin JDK 17
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the project inside Docker
RUN apt-get update && apt-get install -y maven && \
    mvn clean package -DskipTests

# Copy the generated jar (path inside container)
COPY target/waarc-backend.jar app.jar

# Expose port (Render overrides $PORT)
EXPOSE 7001

# Start the app
CMD ["sh", "-c", "java -jar app.jar"]