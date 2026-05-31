# Getting Started with Order Management System

## Quick Start Guide

### Option 1: Using Docker Compose (Recommended)

The easiest way to get started is using Docker Compose, which sets up both the MySQL database and the Spring Boot application.

#### Prerequisites
- Docker (version 20.10+)
- Docker Compose (version 1.29+)

#### Steps

1. **Navigate to project directory**
```bash
cd OrderManagement
```

2. **Start the application**
```bash
docker-compose up --build
```

This command will:
- Build the Docker image for the application
- Start MySQL 8.0 container
- Start the Spring Boot application
- Set up all necessary databases and tables via Flyway

3. **Verify the application is running**
```bash
curl -X GET http://localhost:8080/api/swagger-ui.html
```

4. **Stop the application**
```bash
docker-compose down
```

---

### Option 2: Local Development Setup

#### Prerequisites
- Java 17 or higher
- Maven 3.8.1+
- MySQL 8.0 (local installation)

#### Step 1: Set up MySQL Database

**On macOS (using Homebrew):**
```bash
brew install mysql
brew services start mysql
mysql -u root -p
```

**On Linux (Ubuntu):**
```bash
sudo apt-get install mysql-server
sudo mysql -u root -p
```

**On Windows:**
- Download MySQL installer from mysql.com
- Run the installer and follow setup wizard
- Start MySQL server

#### Step 2: Create Database and User

Once MySQL is running, create the database:

```sql
CREATE DATABASE orderdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'orderuser'@'localhost' IDENTIFIED BY 'orderpass';
GRANT ALL PRIVILEGES ON orderdb.* TO 'orderuser'@'localhost';
FLUSH PRIVILEGES;
```

#### Step 3: Update Application Configuration

Edit `src/main/resources/application.properties`:

```properties
# Update these with your MySQL credentials if different
spring.datasource.url=jdbc:mysql://localhost:3306/orderdb?useSSL=false&serverTimezone=UTC
spring.datasource.username=orderuser
spring.datasource.password=orderpass
```

#### Step 4: Build the Application

```bash
cd OrderManagement
mvn clean install
```

#### Step 5: Run the Application

```bash
mvn spring-boot:run
```

Or:

```bash
java -jar target/order-management-1.0.0.jar
```

#### Step 6: Verify the Application

Once the application starts, you should see logs indicating:
- Flyway migrations are running
- Scheduled jobs are enabled
- Application is listening on port 8080

Access the Swagger UI:
```
http://localhost:8080/api/swagger-ui.html
```

---

## Testing the API

### 1. Create an Order

```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "John Doe",
    "customerEmail": "john@example.com",
    "shippingAddress": "123 Main Street, New York, NY 10001",
    "orderItems": [
      {
        "productName": "Laptop",
        "productCode": "PROD-001",
        "quantity": 1,
        "unitPrice": 1299.99
      },
      {
        "productName": "Mouse",
        "productCode": "PROD-002",
        "quantity": 2,
        "unitPrice": 29.99
      }
    ]
  }'
```

**Expected Response (201 Created):**
```json
{
  "id": 1,
  "orderNumber": "ORD-1622548800000-ABC12345",
  "customerName": "John Doe",
  "customerEmail": "john@example.com",
  "shippingAddress": "123 Main Street, New York, NY 10001",
  "status": "PENDING",
  "totalAmount": 1359.97,
  "orderItems": [...],
  "createdAt": "2024-05-31T10:30:00",
  "updatedAt": "2024-05-31T10:30:00"
}
```

### 2. Get All Orders

```bash
curl -X GET "http://localhost:8080/api/orders?pageNumber=0&pageSize=10" \
  -H "Content-Type: application/json"
```

### 3. Get Order by ID

```bash
curl -X GET http://localhost:8080/api/orders/1 \
  -H "Content-Type: application/json"
```

### 4. Get Orders by Status

```bash
curl -X GET "http://localhost:8080/api/orders/status/PENDING?pageNumber=0&pageSize=10" \
  -H "Content-Type: application/json"
```

### 5. Update Order Status

```bash
curl -X PUT http://localhost:8080/api/orders/1/status/SHIPPED \
  -H "Content-Type: application/json"
```

### 6. Cancel Order

```bash
curl -X DELETE http://localhost:8080/api/orders/1/cancel \
  -H "Content-Type: application/json"
```

---

## Running Tests

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=OrderServiceIntegrationTest
```

### Run with Maven Failsafe (Integration Tests)
```bash
mvn verify
```

### Run with Coverage Report
```bash
mvn clean test jacoco:report
```

Coverage report will be available at:
```
target/site/jacoco/index.html
```

---

## Troubleshooting

### Issue: "Connection refused" when starting application

**Solution:**
- Verify MySQL is running: `brew services list` (macOS) or `systemctl status mysql` (Linux)
- Check port 3306 is not blocked
- Verify database credentials in `application.properties`

### Issue: "Flyway migration failed"

**Solution:**
- Ensure database is created and empty before first run
- Delete `flyway_schema_history` table and restart if migrations are corrupted
- Check file permissions on migration SQL files

### Issue: "Port 8080 already in use"

**Solution:**
- Change port in `application.properties`: `server.port=8081`
- Or kill process using port 8080:
  - macOS/Linux: `lsof -ti:8080 | xargs kill -9`
  - Windows: `netstat -ano | findstr :8080` then `taskkill /PID <PID> /F`

### Issue: Docker container fails to start

**Solution:**
- Check Docker daemon is running
- Check disk space: `docker system df`
- Clean up unused images: `docker system prune`
- Rebuild image: `docker-compose up --build`

### Issue: Scheduled job not running

**Solution:**
- Verify `@EnableScheduling` is present on main application class
- Check application logs for job execution
- Job runs every 5 minutes (300000 ms) after 60-second initial delay

---

## Environment Variables

You can override properties using environment variables:

```bash
# Using Docker Compose
export SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/orderdb
export SPRING_DATASOURCE_USERNAME=orderuser
docker-compose up

# Using Docker run
docker run -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/orderdb \
           -p 8080:8080 \
           order-management:latest
```

---

## Development Tips

### Enable Debug Logging
In `application.properties`:
```properties
logging.level.com.example=DEBUG
logging.level.org.springframework.web=DEBUG
logging.level.org.springframework.data=DEBUG
```

### Access MySQL from Docker Container
```bash
docker exec -it order-management-mysql mysql -u orderuser -p orderdb
```

### View Application Logs
```bash
docker-compose logs -f app
```

### Rebuild without cache
```bash
docker-compose build --no-cache
```

---

## IDE Setup

### IntelliJ IDEA
1. Open project as Maven project
2. File → Project Structure → SDKs → Set Java 17
3. Enable annotation processing: Settings → Build → Compiler → Annotation Processors
4. Install Lombok plugin: Plugins → Search "Lombok"
5. Install MapStruct plugin: Plugins → Search "MapStruct"

### VS Code
1. Install Extension Pack for Java
2. Install Lombok Annotations Support for VS Code
3. Create `.vscode/settings.json`:
```json
{
  "java.home": "/path/to/java17",
  "java.configuration.updateBuildConfiguration": "interactive"
}
```

---

## Next Steps

1. Review the [README.md](README.md) for comprehensive documentation
2. Explore API endpoints via Swagger UI at `http://localhost:8080/api/swagger-ui.html`
3. Check the [src/main/java](src/main/java) directory structure
4. Review test classes for usage examples
5. Modify `application.properties` for your environment
6. Implement additional features from the roadmap

---

## Common API Workflows

### Workflow 1: Complete Order Lifecycle
```bash
# 1. Create order
ORDER_ID=$(curl -s -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{...}' | jq -r '.id')

# 2. Wait 5 minutes for auto-status update (or manually update)
curl -X PUT http://localhost:8080/api/orders/$ORDER_ID/status/PROCESSING \
  -H "Content-Type: application/json"

# 3. Mark as shipped
curl -X PUT http://localhost:8080/api/orders/$ORDER_ID/status/SHIPPED \
  -H "Content-Type: application/json"

# 4. Mark as delivered
curl -X PUT http://localhost:8080/api/orders/$ORDER_ID/status/DELIVERED \
  -H "Content-Type: application/json"
```

### Workflow 2: Cancel an Order
```bash
# Only works if order status is PENDING
curl -X DELETE http://localhost:8080/api/orders/$ORDER_ID/cancel \
  -H "Content-Type: application/json"
```

---

## Support

For issues or questions:
1. Check the [README.md](README.md) troubleshooting section
2. Review application logs: `docker-compose logs app`
3. Check MySQL logs: `docker-compose logs mysql`
4. Verify all services are healthy: `docker-compose ps`

---

**Last Updated**: May 31, 2024

