git commit -m "Initial commit"
# Use official Maven image to build the application
FROM maven:3.9-eclipse-temurin-17 AS builder

# Set working directory
WORKDIR /app

# Copy pom.xml
COPY pom.xml .

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Use official OpenJDK runtime as base image
FROM eclipse-temurin:17-jre

# Set working directory
WORKDIR /app

# Copy jar from builder stage
COPY --from=builder /app/target/order-management-*.jar app.jar

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
    CMD java -cp app.jar org.springframework.boot.loader.JarLauncher || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

