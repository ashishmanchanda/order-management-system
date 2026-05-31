# Order Management System - Spring Boot

A comprehensive Spring Boot application for managing customer orders with advanced features including pagination, optimistic locking, audit fields, structured logging, and comprehensive testing.

## Project Overview

This project demonstrates professional Spring Boot development practices with:
- RESTful API design with comprehensive documentation
- Database migrations using Flyway
- Integration testing with Testcontainers
- MapStruct for DTO mapping
- Global exception handling
- Structured logging with SLF4J
- Optimistic locking with @Version
- Docker containerization

## Core Features

### 1. Order Management
- **Create Orders**: Place orders with multiple items
- **Retrieve Orders**: Fetch order details by ID or order number
- **List Orders**: Get all orders with pagination and optional status filtering
- **Update Status**: Change order status (PENDING → PROCESSING → SHIPPED → DELIVERED)
- **Cancel Orders**: Cancel only PENDING orders

### 2. Order Status Workflow
- **PENDING**: Initial status when order is created
- **PROCESSING**: Automatically updated from PENDING every 5 minutes
- **SHIPPED**: Manual status update
- **DELIVERED**: Final status
- **CANCELLED**: Only available for PENDING orders

### 3. Background Processing
- Automatic status update job runs every 5 minutes
- Converts PENDING orders to PROCESSING automatically
- Fully transactional and error-handled

## Technology Stack

### Core
- **Java**: 17
- **Spring Boot**: 3.1.5
- **Spring Data JPA**: ORM and data access
- **MySQL**: Primary database
- **H2**: Testing database

### Build & Configuration
- **Maven**: Dependency management and build
- **Flyway**: Database migrations

### API & Documentation
- **Spring Web**: RESTful services
- **OpenAPI 3.0**: API documentation with Swagger UI
- **Jackson**: JSON processing

### Data Mapping
- **MapStruct**: DTO to entity mapping

### Testing
- **JUnit 5**: Unit testing framework
- **Testcontainers**: Integration testing with real MySQL
- **MockMvc**: REST controller testing
- **Spring Test**: Spring-specific testing utilities

### Utilities
- **Lombok**: Boilerplate reduction
- **SLF4J**: Structured logging

### Containerization
- **Docker**: Application containerization
- **Docker Compose**: Multi-container orchestration

## Project Structure

```
order-management/
├── src/
│   ├── main/
│   │   ├── java/com/example/ordermanagement/
│   │   │   ├── OrderManagementApplication.java
│   │   │   ├── controller/
│   │   │   │   └── OrderController.java
│   │   │   ├── service/
│   │   │   │   ├── OrderService.java
│   │   │   │   └── impl/
│   │   │   │       └── OrderServiceImpl.java
│   │   │   ├── entity/
│   │   │   │   ├── Order.java
│   │   │   │   ├── OrderItem.java
│   │   │   │   └── OrderStatus.java
│   │   │   ├── dto/
│   │   │   │   ├── OrderDTO.java
│   │   │   │   ├── OrderItemDTO.java
│   │   │   │   ├── CreateOrderDTO.java
│   │   │   │   ├── CreateOrderItemDTO.java
│   │   │   │   └── PagedResponse.java
│   │   │   ├── repository/
│   │   │   │   ├── OrderRepository.java
│   │   │   │   └── OrderItemRepository.java
│   │   │   ├── mapper/
│   │   │   │   └── OrderMapper.java
│   │   │   ├── exception/
│   │   │   │   ├── OrderNotFoundException.java
│   │   │   │   ├── OrderCancellationException.java
│   │   │   │   └── ValidationException.java
│   │   │   ├── config/
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   ├── ErrorResponse.java
│   │   │   │   └── SwaggerConfig.java
│   │   │   └── job/
│   │   │       └── OrderStatusUpdateJob.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── db/migration/
│   │           ├── V1__Create_orders_table.sql
│   │           └── V2__Create_order_items_table.sql
│   └── test/
│       ├── java/com/example/ordermanagement/
│       │   ├── repository/
│       │   │   └── OrderRepositoryTest.java
│       │   ├── service/
│       │   │   └── OrderServiceIntegrationTest.java
│       │   └── controller/
│       │       └── OrderControllerIntegrationTest.java
│       └── resources/
│           └── application-test.properties
├── pom.xml
├── Dockerfile
└── docker-compose.yml
```

## API Endpoints

### Order Creation
```
POST /api/orders
```
**Request:**
```json
{
  "customerName": "John Doe",
  "customerEmail": "john@example.com",
  "shippingAddress": "123 Main St, City, State 12345",
  "orderItems": [
    {
      "productName": "Laptop",
      "productCode": "PROD-001",
      "quantity": 1,
      "unitPrice": 999.99
    }
  ]
}
```

### Get Order by ID
```
GET /api/orders/{id}
```

### Get Order by Order Number
```
GET /api/orders/number/{orderNumber}
```

### Get All Orders (Paginated)
```
GET /api/orders?pageNumber=0&pageSize=10
```

### Get Orders by Status
```
GET /api/orders/status/{status}?pageNumber=0&pageSize=10
```

### Get Orders by Customer Email
```
GET /api/orders/customer/{customerEmail}?pageNumber=0&pageSize=10
```

### Update Order Status
```
PUT /api/orders/{id}/status/{status}
```

### Cancel Order
```
DELETE /api/orders/{id}/cancel
```

### Get Order Count by Status
```
GET /api/orders/count/status/{status}
```

## Advanced Features

### 1. Optimistic Locking
The `Order` entity uses `@Version` annotation for optimistic locking:
```java
@Version
@Column(nullable = false)
private Long version = 0L;
```

This prevents concurrent update conflicts when multiple requests modify the same order simultaneously.

### 2. Audit Fields
All entities include audit timestamps:
```java
@CreationTimestamp
@Column(nullable = false, updatable = false)
private LocalDateTime createdAt;

@UpdateTimestamp
@Column(nullable = false)
private LocalDateTime updatedAt;
```

### 3. Structured Logging
SLF4J is used with Lombok's `@Slf4j` annotation:
```java
@Slf4j
public class OrderController {
    public void someMethod() {
        log.info("Creating order for customer: {}", customerName);
        log.error("Error occurred", exception);
    }
}
```

### 4. MapStruct DTO Mapping
Automatic type-safe mapping between entities and DTOs:
```java
@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDTO toOrderDTO(Order order);
    Order toOrder(CreateOrderDTO createOrderDTO);
}
```

### 5. Global Exception Handling
Centralized error handling with custom error responses:
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    // Handles all exceptions with consistent response format
}
```

### 6. Database Migrations (Flyway)
Version-controlled database schema changes:
```
V1__Create_orders_table.sql
V2__Create_order_items_table.sql
```

### 7. Pagination Support
All list endpoints support pagination:
```java
Page<Order> orders = orderRepository.findAll(pageable);
```

## Getting Started

### Prerequisites
- Java 17+
- Maven 3.6+
- MySQL 8.0 (or use Docker)
- Docker & Docker Compose (optional, for containerization)

### Local Setup

1. **Clone the repository**
```bash
git clone <repository-url>
cd OrderManagement
```

2. **Create MySQL Database**
```bash
mysql -u root -p
CREATE DATABASE orderdb;
CREATE USER 'orderuser'@'localhost' IDENTIFIED BY 'orderpass';
GRANT ALL PRIVILEGES ON orderdb.* TO 'orderuser'@'localhost';
FLUSH PRIVILEGES;
```

3. **Update application.properties** (if using different credentials)
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/orderdb
spring.datasource.username=orderuser
spring.datasource.password=orderpass
```

4. **Build and Run**
```bash
mvn clean install
mvn spring-boot:run
```

5. **Access the Application**
- Application: http://localhost:8080/api
- Swagger UI: http://localhost:8080/api/swagger-ui.html
- API Docs: http://localhost:8080/api/v3/api-docs

### Docker Setup

1. **Build and Run with Docker Compose**
```bash
docker-compose up --build
```

2. **Access the Application**
- Application: http://localhost:8080/api
- Swagger UI: http://localhost:8080/api/swagger-ui.html
- MySQL: localhost:3306 (user: orderuser, password: orderpass)

3. **Stop the Application**
```bash
docker-compose down
```

## Running Tests

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=OrderRepositoryTest
```

### Run with Coverage
```bash
mvn clean test jacoco:report
```

## AI/Tools Used & Explanations

### 1. **ChatGPT/Cursor AI - API Design**
- Used for designing RESTful API endpoints following best practices
- **Issues Found**: Initial design had inconsistent endpoint naming conventions
- **Correction**: Standardized to `/api/orders/{id}/status/{status}` format with proper HTTP verbs

### 2. **ChatGPT - Exception Handling Strategy**
- Used to design global exception handling with consistent error responses
- **Issues Found**: Initially threw generic RuntimeException without proper context
- **Correction**: Created custom exceptions and implemented GlobalExceptionHandler with proper error response format

### 3. **Cursor AI - Database Schema Design**
- Used for designing optimal Flyway migrations with proper indexes
- **Issues Found**: Missing indexes on frequently queried columns
- **Correction**: Added indexes on `order_number`, `customer_email`, `status` for query optimization

### 4. **ChatGPT - Optimistic Locking Implementation**
- Used to understand and implement @Version annotation for concurrent updates
- **Issues Found**: Race conditions when multiple users update same order
- **Correction**: Added @Version field with Hibernate's optimistic locking mechanism

### 5. **Cursor AI - Test Strategy**
- Used for designing comprehensive test suite with Testcontainers
- **Issues Found**: Tests were flaky with H2 database compatibility issues
- **Correction**: Switched to real MySQL with Testcontainers for integration tests

### 6. **ChatGPT - Docker Configuration**
- Used for multi-stage Docker build and docker-compose orchestration
- **Issues Found**: Application started before database was ready
- **Correction**: Added health checks and `depends_on` conditions in docker-compose

### 7. **ChatGPT - Logging Strategy**
- Used to implement structured logging with SLF4J and Lombok
- **Issues Found**: Logs were not consistent or searchable in production
- **Correction**: Added contextual logging at different levels (DEBUG, INFO, WARN, ERROR)

### 8. **Cursor AI - MapStruct Configuration**
- Used for automatic DTO-to-Entity mapping with MapStruct
- **Issues Found**: Manual mapping was error-prone and tedious
- **Correction**: Configured MapStruct processor in Maven compiler plugin for compile-time code generation

## Key Design Patterns Used

### 1. **Service Layer Pattern**
- `OrderService` interface with `OrderServiceImpl` implementation
- Separates business logic from controllers

### 2. **Repository Pattern**
- Spring Data JPA repositories for data access
- Custom query methods using `@Query`

### 3. **DTO Pattern**
- Separate request/response DTOs to isolate entities
- MapStruct for automatic conversion

### 4. **Exception Handling Pattern**
- Custom exceptions for domain-specific errors
- Global exception handler for consistent responses

### 5. **Pagination Pattern**
- Spring Data's Pageable interface
- Custom PagedResponse wrapper for consistent pagination format

### 6. **Job Scheduling Pattern**
- `@Scheduled` annotation for periodic tasks
- Background job for auto-updating order statuses

## Performance Considerations

1. **Database Indexes**: Added on frequently queried columns
2. **Lazy Loading**: Used FetchType.LAZY for order items
3. **Pagination**: Prevents loading large datasets into memory
4. **Optimistic Locking**: Reduces database locks and increases concurrency
5. **Connection Pooling**: Configured in application.properties

## Security Considerations (Future Enhancements)

- [ ] Spring Security for authentication/authorization
- [ ] API rate limiting
- [ ] Input validation against injection attacks
- [ ] HTTPS/TLS for data in transit
- [ ] Sensitive data encryption

## Monitoring & Observability

### Logging
- Structured logs using SLF4J
- Different log levels for different components
- Contextual information in all logs

### Health Checks
- Spring Boot Actuator endpoints
- Docker health checks
- Database connectivity monitoring

## Database Schema

### Orders Table
```sql
CREATE TABLE orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_number VARCHAR(255) UNIQUE NOT NULL,
    customer_name VARCHAR(255) NOT NULL,
    customer_email VARCHAR(255) NOT NULL,
    shipping_address TEXT NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    total_amount DECIMAL(19, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0,
    INDEX idx_order_number (order_number),
    INDEX idx_customer_email (customer_email),
    INDEX idx_status (status)
);
```

### Order Items Table
```sql
CREATE TABLE order_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    product_code VARCHAR(100) NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(19, 2) NOT NULL,
    total_price DECIMAL(19, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    INDEX idx_order_id (order_id),
    INDEX idx_product_code (product_code)
);
```

## Troubleshooting

### Application Won't Start
```
Error: java.sql.SQLException: Connection refused
Solution: Ensure MySQL is running on localhost:3306
```

### Docker Build Fails
```
Error: Maven build fails in Docker
Solution: Ensure pom.xml is in the root directory and use docker-compose up --build
```

### Tests Fail
```
Error: Testcontainers unable to connect to MySQL
Solution: Ensure Docker daemon is running and check Docker socket permissions
```

## Future Enhancements

1. **Email Notifications**: Send order confirmation and status update emails
2. **Payment Integration**: Add payment processing capabilities
3. **Inventory Management**: Track product stock levels
4. **Order History**: View previous orders and reorder functionality
5. **Advanced Analytics**: Order trends and metrics
6. **GraphQL API**: Alternative to REST API
7. **Caching**: Redis integration for frequently accessed data
8. **Elasticsearch**: Full-text search on order items
9. **Microservices**: Split into separate services for scalability
10. **Event Streaming**: Kafka for asynchronous order processing

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see LICENSE file for details.

## Contact

For questions or suggestions, please reach out to the development team.

## Acknowledgments

- Spring Boot community for excellent documentation
- TestContainers for simplifying integration testing
- MapStruct for type-safe DTO mapping
- The AI tools (ChatGPT, Cursor AI) for assistance in architecture and implementation decisions

