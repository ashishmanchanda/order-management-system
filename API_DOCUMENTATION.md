# Order Management System - API Documentation

## Base URL
```
http://localhost:8080/api
```

## Authentication
Currently, no authentication is required. (Future enhancement: Add Spring Security)

## Content Type
All requests and responses use `application/json`

---

## API Endpoints

### 1. Create Order

**Endpoint:** `POST /orders`

**Description:** Create a new order with multiple items

**Request Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
{
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
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "orderNumber": "ORD-1622548800000-ABC12345",
  "customerName": "John Doe",
  "customerEmail": "john@example.com",
  "shippingAddress": "123 Main Street, New York, NY 10001",
  "status": "PENDING",
  "totalAmount": 1359.97,
  "orderItems": [
    {
      "id": 1,
      "productName": "Laptop",
      "productCode": "PROD-001",
      "quantity": 1,
      "unitPrice": 1299.99,
      "totalPrice": 1299.99,
      "createdAt": "2024-05-31T10:30:00",
      "updatedAt": "2024-05-31T10:30:00"
    },
    {
      "id": 2,
      "productName": "Mouse",
      "productCode": "PROD-002",
      "quantity": 2,
      "unitPrice": 29.99,
      "totalPrice": 59.98,
      "createdAt": "2024-05-31T10:30:00",
      "updatedAt": "2024-05-31T10:30:00"
    }
  ],
  "createdAt": "2024-05-31T10:30:00",
  "updatedAt": "2024-05-31T10:30:00"
}
```

**Error Response (400 Bad Request):**
```json
{
  "timestamp": "2024-05-31T10:30:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "Validation failed for one or more fields",
  "path": "/api/orders",
  "validationErrors": {
    "customerEmail": "Customer email should be valid",
    "orderItems": "Order must contain at least one item"
  }
}
```

---

### 2. Get All Orders

**Endpoint:** `GET /orders`

**Description:** Retrieve all orders with pagination

**Query Parameters:**
- `pageNumber` (optional): Page number starting from 0. Default: 0
- `pageSize` (optional): Number of records per page. Default: 10

**Example:**
```
GET /orders?pageNumber=0&pageSize=10
```

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": 1,
      "orderNumber": "ORD-1622548800000-ABC12345",
      "customerName": "John Doe",
      "customerEmail": "john@example.com",
      "shippingAddress": "123 Main Street",
      "status": "PENDING",
      "totalAmount": 1359.97,
      "orderItems": [...],
      "createdAt": "2024-05-31T10:30:00",
      "updatedAt": "2024-05-31T10:30:00"
    }
  ],
  "pageNumber": 0,
  "pageSize": 10,
  "totalElements": 25,
  "totalPages": 3,
  "isLast": false
}
```

---

### 3. Get Order by ID

**Endpoint:** `GET /orders/{id}`

**Description:** Retrieve a specific order by its ID

**Path Parameters:**
- `id` (required): The order ID (Long)

**Example:**
```
GET /orders/1
```

**Response (200 OK):**
```json
{
  "id": 1,
  "orderNumber": "ORD-1622548800000-ABC12345",
  "customerName": "John Doe",
  "customerEmail": "john@example.com",
  "shippingAddress": "123 Main Street",
  "status": "PENDING",
  "totalAmount": 1359.97,
  "orderItems": [...],
  "createdAt": "2024-05-31T10:30:00",
  "updatedAt": "2024-05-31T10:30:00"
}
```

**Error Response (404 Not Found):**
```json
{
  "timestamp": "2024-05-31T10:30:00",
  "status": 404,
  "error": "Order Not Found",
  "message": "Order not found with ID: 999",
  "path": "/api/orders/999"
}
```

---

### 4. Get Order by Order Number

**Endpoint:** `GET /orders/number/{orderNumber}`

**Description:** Retrieve a specific order by its order number

**Path Parameters:**
- `orderNumber` (required): The order number (String, format: ORD-TIMESTAMP-CODE)

**Example:**
```
GET /orders/number/ORD-1622548800000-ABC12345
```

**Response (200 OK):** Same as Get Order by ID

---

### 5. Get Orders by Status

**Endpoint:** `GET /orders/status/{status}`

**Description:** Retrieve all orders with a specific status

**Path Parameters:**
- `status` (required): Order status - PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED

**Query Parameters:**
- `pageNumber` (optional): Default: 0
- `pageSize` (optional): Default: 10

**Example:**
```
GET /orders/status/PENDING?pageNumber=0&pageSize=10
```

**Response (200 OK):** Same format as Get All Orders

---

### 6. Get Orders by Customer Email

**Endpoint:** `GET /orders/customer/{customerEmail}`

**Description:** Retrieve all orders for a specific customer

**Path Parameters:**
- `customerEmail` (required): The customer's email address

**Query Parameters:**
- `pageNumber` (optional): Default: 0
- `pageSize` (optional): Default: 10

**Example:**
```
GET /orders/customer/john@example.com?pageNumber=0&pageSize=10
```

**Response (200 OK):** Same format as Get All Orders

---

### 7. Update Order Status

**Endpoint:** `PUT /orders/{id}/status/{status}`

**Description:** Update the status of an existing order

**Path Parameters:**
- `id` (required): The order ID (Long)
- `status` (required): New status - PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED

**Example:**
```
PUT /orders/1/status/PROCESSING
```

**Response (200 OK):**
```json
{
  "id": 1,
  "orderNumber": "ORD-1622548800000-ABC12345",
  "customerName": "John Doe",
  "customerEmail": "john@example.com",
  "shippingAddress": "123 Main Street",
  "status": "PROCESSING",
  "totalAmount": 1359.97,
  "orderItems": [...],
  "createdAt": "2024-05-31T10:30:00",
  "updatedAt": "2024-05-31T10:31:00"
}
```

---

### 8. Cancel Order

**Endpoint:** `DELETE /orders/{id}/cancel`

**Description:** Cancel an order (only if status is PENDING)

**Path Parameters:**
- `id` (required): The order ID (Long)

**Example:**
```
DELETE /orders/1/cancel
```

**Response (200 OK):**
```json
{
  "id": 1,
  "orderNumber": "ORD-1622548800000-ABC12345",
  "customerName": "John Doe",
  "customerEmail": "john@example.com",
  "shippingAddress": "123 Main Street",
  "status": "CANCELLED",
  "totalAmount": 1359.97,
  "orderItems": [...],
  "createdAt": "2024-05-31T10:30:00",
  "updatedAt": "2024-05-31T10:31:00"
}
```

**Error Response (400 Bad Request):**
```json
{
  "timestamp": "2024-05-31T10:30:00",
  "status": 400,
  "error": "Order Cancellation Failed",
  "message": "Only PENDING orders can be cancelled. Current status: PROCESSING",
  "path": "/api/orders/1/cancel"
}
```

---

### 9. Get Order Count by Status

**Endpoint:** `GET /orders/count/status/{status}`

**Description:** Get the total count of orders for a specific status

**Path Parameters:**
- `status` (required): Order status

**Example:**
```
GET /orders/count/status/PENDING
```

**Response (200 OK):**
```
25
```

---

## Status Codes

### Success Codes
- **200 OK**: Request successful, data returned
- **201 Created**: Resource created successfully

### Error Codes
- **400 Bad Request**: Invalid input or request validation failed
- **404 Not Found**: Resource not found
- **500 Internal Server Error**: Server error occurred

---

## Error Response Format

All error responses follow this format:

```json
{
  "timestamp": "2024-05-31T10:30:00",
  "status": <HTTP_STATUS_CODE>,
  "error": "<ERROR_TYPE>",
  "message": "<ERROR_MESSAGE>",
  "path": "<REQUEST_PATH>",
  "validationErrors": {} // Only for validation errors
}
```

---

## Pagination Response Format

Paginated endpoints return responses in this format:

```json
{
  "content": [...],           // Array of items
  "pageNumber": 0,            // Current page (0-indexed)
  "pageSize": 10,             // Items per page
  "totalElements": 100,       // Total items
  "totalPages": 10,           // Total pages
  "isLast": false             // Is this the last page?
}
```

---

## Order Status Flow

```
PENDING ---(auto 5 min)---> PROCESSING
   |                            |
   |                            ├---> SHIPPED
   |                            |       |
   |                            |       └---> DELIVERED
   |
   └---(cancel)---> CANCELLED
```

**Note:** 
- Only PENDING orders can be cancelled
- PENDING orders automatically transition to PROCESSING every 5 minutes
- Manual status updates can be made at any time

---

## Rate Limiting
Currently not implemented. To be added in future versions.

---

## Versioning
API Version: 1.0

Current URL format: `/api/orders`
Future versions might use: `/api/v2/orders`

---

## Examples Using cURL

### Create an order
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "Jane Smith",
    "customerEmail": "jane@example.com",
    "shippingAddress": "456 Oak Ave, Boston, MA 02101",
    "orderItems": [
      {
        "productName": "Keyboard",
        "productCode": "PROD-003",
        "quantity": 1,
        "unitPrice": 89.99
      }
    ]
  }'
```

### Get all orders
```bash
curl -X GET "http://localhost:8080/api/orders?pageNumber=0&pageSize=5" \
  -H "Content-Type: application/json"
```

### Get specific order
```bash
curl -X GET http://localhost:8080/api/orders/1 \
  -H "Content-Type: application/json"
```

### Update order status
```bash
curl -X PUT http://localhost:8080/api/orders/1/status/SHIPPED \
  -H "Content-Type: application/json"
```

### Cancel order
```bash
curl -X DELETE http://localhost:8080/api/orders/1/cancel \
  -H "Content-Type: application/json"
```

---

## Examples Using JavaScript/Fetch

### Create an order
```javascript
const orderData = {
  customerName: "John Doe",
  customerEmail: "john@example.com",
  shippingAddress: "123 Main Street, New York, NY 10001",
  orderItems: [
    {
      productName: "Laptop",
      productCode: "PROD-001",
      quantity: 1,
      unitPrice: 1299.99
    }
  ]
};

fetch('http://localhost:8080/api/orders', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify(orderData)
})
.then(response => response.json())
.then(data => console.log('Order created:', data))
.catch(error => console.error('Error:', error));
```

### Get all orders
```javascript
fetch('http://localhost:8080/api/orders?pageNumber=0&pageSize=10')
  .then(response => response.json())
  .then(data => console.log('Orders:', data.content))
  .catch(error => console.error('Error:', error));
```

---

## WebSocket Support
Not currently implemented. To be added in future versions for real-time order status updates.

---

## GraphQL Support
Not currently implemented. Alternative to REST API planned for future versions.

---

## Changelog

### Version 1.0.0 (May 31, 2024)
- Initial release
- Core order management functionality
- Status workflow implementation
- Pagination support
- API documentation

