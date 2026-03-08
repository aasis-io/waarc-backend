# Use Eclipse Temurin JDK 17
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Install Maven
RUN apt-get update && apt-get install -y maven git && rm -rf /var/lib/apt/lists/*

# Copy project files
COPY pom.xml .
COPY src ./src

# Build the project inside the container
RUN mvn clean package -DskipTests

# Run the built jar (replace with your actual jar name from target/)
CMD ["sh", "-c", "java -jar target/waarc-backend.jar"]

# Expose port (Render overrides $PORT)
EXPOSE 7001