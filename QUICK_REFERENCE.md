# Order Management System - Quick Reference & Commands

## 🚀 Quick Start Commands

### Using Docker Compose (Recommended)
```bash
# Navigate to project
cd OrderManagement

# Start everything
docker-compose up --build

# Stop everything
docker-compose down

# View logs
docker-compose logs -f app
docker-compose logs -f mysql

# Rebuild without cache
docker-compose up --build --no-cache
```

### Local Development Setup
```bash
# Build project
mvn clean install

# Run application
mvn spring-boot:run

# Run tests
mvn test

# Run integration tests
mvn verify

# Generate test coverage report
mvn clean test jacoco:report

# View coverage report
open target/site/jacoco/index.html  # macOS
xdg-open target/site/jacoco/index.html  # Linux
start target\site\jacoco\index.html  # Windows
```

---

## 🌐 API Quick Commands

### Create Order
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
      }
    ]
  }'
```

### Get All Orders
```bash
curl http://localhost:8080/api/orders?pageNumber=0&pageSize=10
```

### Get Specific Order
```bash
curl http://localhost:8080/api/orders/1
```

### Get Orders by Status
```bash
curl http://localhost:8080/api/orders/status/PENDING?pageNumber=0&pageSize=10
```

### Get Orders by Customer Email
```bash
curl http://localhost:8080/api/orders/customer/john@example.com?pageNumber=0&pageSize=10
```

### Update Order Status
```bash
curl -X PUT http://localhost:8080/api/orders/1/status/PROCESSING
```

### Cancel Order
```bash
curl -X DELETE http://localhost:8080/api/orders/1/cancel
```

### Get Count of Orders by Status
```bash
curl http://localhost:8080/api/orders/count/status/PENDING
```

---

## 🗄️ Database Commands

### Connect to MySQL in Docker
```bash
docker exec -it order-management-mysql mysql -u orderuser -p orderdb
# Password: orderpass
```

### Check Database
```sql
-- Show tables
SHOW TABLES;

-- Show orders
SELECT * FROM orders;

-- Show order items
SELECT * FROM order_items;

-- Count orders by status
SELECT status, COUNT(*) FROM orders GROUP BY status;

-- Check flyway history
SELECT * FROM flyway_schema_history;
```

### Local MySQL Connection
```bash
mysql -u orderuser -p orderdb
# Password: orderpass
```

---

## 📊 Swagger UI & Docs

### Access API Documentation
```
http://localhost:8080/api/swagger-ui.html
```

### Access OpenAPI JSON
```
http://localhost:8080/api/v3/api-docs
```

### Access OpenAPI YAML
```
http://localhost:8080/api/v3/api-docs.yaml
```

---

## 🐳 Docker Commands

### Build Docker Image
```bash
docker build -t order-management:1.0 .
```

### Run Container Manually
```bash
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/orderdb \
  -e SPRING_DATASOURCE_USERNAME=orderuser \
  -e SPRING_DATASOURCE_PASSWORD=orderpass \
  order-management:1.0
```

### Push to Docker Registry
```bash
docker tag order-management:1.0 your-registry/order-management:1.0
docker push your-registry/order-management:1.0
```

### Clean Up Docker
```bash
# Remove unused images
docker system prune

# Remove all stopped containers
docker container prune

# Remove all unused volumes
docker volume prune

# Full cleanup
docker system prune -a
```

---

## 🧪 Testing Commands

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=OrderRepositoryTest
mvn test -Dtest=OrderServiceIntegrationTest
mvn test -Dtest=OrderControllerIntegrationTest
```

### Run Specific Test Method
```bash
mvn test -Dtest=OrderRepositoryTest#testSaveOrder
```

### Run Tests with Detailed Output
```bash
mvn test -X
```

### Skip Tests During Build
```bash
mvn clean install -DskipTests
```

### Run Tests with Coverage
```bash
mvn clean test jacoco:report
```

### View Coverage Report
```bash
# After running: mvn clean test jacoco:report
open target/site/jacoco/index.html
```

---

## 📝 Logging & Debugging

### Enable Debug Logging
Edit `src/main/resources/application.properties`:
```properties
logging.level.com.example=DEBUG
logging.level.org.springframework.web=DEBUG
logging.level.org.springframework.data=DEBUG
```

### View Application Logs
```bash
# Using Docker
docker-compose logs -f app

# Using Maven
mvn spring-boot:run (logs shown in console)
```

### Enable SQL Logging
```properties
logging.level.org.hibernate.SQL=DEBUG
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.show-sql=true
```

---

## 🔍 Troubleshooting Commands

### Check if Port is in Use
```bash
# macOS/Linux
lsof -i :8080

# Windows
netstat -ano | findstr :8080
```

### Kill Process Using Port
```bash
# macOS/Linux
lsof -ti:8080 | xargs kill -9

# Windows
taskkill /PID <PID> /F
```

### Check MySQL Connection
```bash
# Test connection
mysql -h localhost -u orderuser -p orderdb -e "SELECT 1;"

# In Docker
docker exec order-management-mysql mysql -u orderuser -p orderdb -e "SELECT 1;"
```

### Check Docker Container Health
```bash
docker-compose ps
docker inspect order-management-app
```

### View Docker Compose Logs
```bash
docker-compose logs
docker-compose logs app
docker-compose logs mysql
docker-compose logs --tail=50 app
```

---

## 🔧 Maven Commands

### Clean and Build
```bash
mvn clean install
```

### Compile Only
```bash
mvn compile
```

### Package JAR
```bash
mvn package
```

### Run Application
```bash
mvn spring-boot:run
```

### Update Dependencies
```bash
mvn dependency:resolve
mvn dependency:tree  # Show dependency tree
```

### Format Code
```bash
# Install formatter
mvn fmt:format
```

### Check Vulnerabilities
```bash
mvn dependency-check:check
```

---

## 📦 Dependency Management

### Add New Dependency
1. Edit `pom.xml`
2. Add to `<dependencies>` section
3. Run `mvn dependency:resolve`
4. Restart IDE

### Update Dependency Version
```bash
mvn versions:display-dependency-updates
mvn versions:use-latest-versions
```

### Show Dependency Tree
```bash
mvn dependency:tree
```

---

## 🚢 Deployment Commands

### Build for Production
```bash
mvn clean package -DskipTests -P prod
```

### Build Docker Image for Production
```bash
docker build -t order-management:1.0-prod --target production .
```

### Deploy to Kubernetes
```bash
# Create ConfigMap
kubectl create configmap order-app-config --from-file=application.properties

# Apply deployment
kubectl apply -f k8s/deployment.yaml

# Check deployment status
kubectl get deployments
kubectl get pods
kubectl describe pod <pod-name>
```

---

## 📊 Performance Monitoring

### Monitor Application Metrics
```bash
# Install actuator dependency first
# Then access:
curl http://localhost:8080/actuator/metrics
curl http://localhost:8080/actuator/health
```

### Monitor Database Connections
```bash
# In MySQL
SHOW PROCESSLIST;
SHOW STATUS LIKE 'Threads%';
```

### Monitor Scheduled Jobs
```bash
# Check application logs for:
# "[INFO] Starting scheduled job to update PENDING orders to PROCESSING"
docker-compose logs app | grep "scheduled job"
```

---

## 🔐 Security Commands

### Scan for Vulnerabilities
```bash
mvn org.owasp:dependency-check-maven:check
```

### Generate Security Report
```bash
mvn clean install
mvn owasp-dependency-check:aggregate
open target/dependency-check-report.html
```

---

## 📚 Documentation Commands

### Generate JavaDoc
```bash
mvn javadoc:javadoc
open target/site/apidocs/index.html
```

### Generate Site Report
```bash
mvn clean site
open target/site/index.html
```

---

## 🔄 Git Commands (for Version Control)

### Initial Setup
```bash
git init
git add .
git commit -m "Initial commit: Order Management System"
```

### Regular Workflow
```bash
git status
git add <files>
git commit -m "Your message"
git push
```

### Create Feature Branch
```bash
git checkout -b feature/your-feature
git push -u origin feature/your-feature
```

---

## 💡 IDE Setup

### IntelliJ IDEA
```bash
# Open project
File → Open → Select OrderManagement folder

# Set Java version
File → Project Structure → SDKs → Select Java 17

# Enable annotation processing
Settings → Build, Execution, Deployment → Compiler → Annotation Processors → Enable

# Run application
Run → Run (Shift+F10)

# Debug application
Run → Debug (Shift+F9)
```

### VS Code
```bash
# Open workspace
code OrderManagement

# Run in terminal
Ctrl+` to open terminal
mvn spring-boot:run
```

---

## 📋 Common Workflows

### Complete Development Cycle
```bash
1. Clone/navigate to project
cd OrderManagement

2. Install dependencies
mvn clean install

3. Start database (Docker)
docker-compose up mysql

4. Run application
mvn spring-boot:run

5. Access API
http://localhost:8080/api/swagger-ui.html

6. Run tests
mvn test

7. Stop database
docker-compose down
```

### Docker Development Cycle
```bash
1. Start everything
docker-compose up --build

2. Test API
curl http://localhost:8080/api/orders

3. Check logs
docker-compose logs -f app

4. Stop everything
docker-compose down

5. Clean up
docker system prune
```

### CI/CD Pipeline
```bash
1. Build: mvn clean package
2. Test: mvn test
3. Coverage: mvn jacoco:report
4. Docker Build: docker build -t app:version .
5. Push: docker push registry/app:version
6. Deploy: kubectl apply -f deployment.yaml
```

---

## 🆘 Emergency Commands

### Reset Database
```bash
# In MySQL
DROP DATABASE orderdb;
CREATE DATABASE orderdb;
CREATE USER 'orderuser'@'localhost' IDENTIFIED BY 'orderpass';
GRANT ALL PRIVILEGES ON orderdb.* TO 'orderuser'@'localhost';
FLUSH PRIVILEGES;

# Then restart application (Flyway will recreate schema)
```

### Reset Docker Environment
```bash
docker-compose down -v  # Remove volumes too
docker system prune -a  # Clean everything
docker-compose up --build  # Rebuild
```

### Force Kill Process
```bash
# macOS/Linux
pkill -f "java.*ordermanagement"

# Windows
taskkill /IM java.exe /F
```

### Clear Maven Cache
```bash
rm -rf ~/.m2/repository
mvn clean install  # Will re-download all dependencies
```

---

## 🎯 Daily Usage Tips

**Start of Day:**
```bash
cd OrderManagement
docker-compose up --build
# App ready at http://localhost:8080/api/swagger-ui.html
```

**During Development:**
```bash
# Watch for changes (in separate terminal)
mvn compile -watch

# Or just restart with Ctrl+C and:
mvn spring-boot:run
```

**Before Commit:**
```bash
mvn clean test
mvn jacoco:report
# Review coverage
```

**End of Day:**
```bash
docker-compose down
git add .
git commit -m "Your changes"
git push
```

---

## 📞 Getting Help

### Check Logs First
```bash
docker-compose logs app
```

### Check Swagger UI
```
http://localhost:8080/api/swagger-ui.html
```

### Read Documentation
- README.md - Overview and features
- GETTING_STARTED.md - Setup instructions
- API_DOCUMENTATION.md - API reference
- IMPLEMENTATION_SUMMARY.md - Technical details

### Search Code
```bash
grep -r "your search term" src/
```

---

**Last Updated:** May 31, 2024  
**Created For:** Order Management System v1.0.0

