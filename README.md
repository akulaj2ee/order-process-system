# 🛒 Order Processing System

A backend application for managing e-commerce orders, built using Spring Boot.
This system supports order creation, status tracking, cancellation, and automated processing.

---

# 🚀 Features

* Create an order with multiple items
* Retrieve order details by ID
* List all orders with optional status filtering
* Update order status (PENDING → PROCESSING → SHIPPED → DELIVERED)
* Cancel orders (only allowed in PENDING state)
* Background scheduler to auto-process pending orders
* Global exception handling with consistent API responses
* Structured logging using SLF4J + Logback
* API documentation using Swagger (OpenAPI)
* Unit + Integration tests with high coverage (~90%+)
* Code coverage report using JaCoCo

---

# 🏗️ Tech Stack

* Java 17
* Spring Boot 3
* Spring Data JPA
* H2 Database (in-memory)
* Lombok
* SLF4J + Logback
* JUnit 5 + Mockito
* Swagger (SpringDoc OpenAPI)

---

# 📦 Project Structure

```
controller    → REST APIs
service       → Business logic
repository    → Data access layer
entity        → JPA entities
dto           → Request/Response models
validator     → Business rule validation
exception     → Custom exceptions & handler
scheduler     → Background job
filter        → Correlation ID logging
```

---

# ⚙️ Setup Instructions

## 1️⃣ Clone Repository

```
git clone <your-repo-url>
cd order-system
```

---

## 2️⃣ Build the Project

```
mvn clean install
```

---

## 3️⃣ Run the Application

```
mvn spring-boot:run
```

---

## 4️⃣ Application URL

Base URL:

```
http://localhost:8080/api/v1
```

---

# 📘 API Documentation (Swagger)

After starting the application:

* Swagger UI:
  http://localhost:8080/api/v1/swagger-ui/index.html

* OpenAPI Spec:
  http://localhost:8080/api/v1/v3/api-docs

---

# 🔗 API Endpoints

## 🟢 Create Order

POST `/api/v1/orders`

### Request:

```json
{
  "items": [
    {
      "productName": "Phone",
      "quantity": 2,
      "price": 500
    }
  ]
}
```

### Response:

```json
{
  "success": true,
  "data": {
    "id": 1,
    "status": "PENDING",
    "totalAmount": 1000
  }
}
```

---

## 🔵 Get Order

GET `/api/v1/orders/{id}`

---

## 🟡 List Orders

GET `/api/v1/orders`

Optional:

```
/api/v1/orders?status=PENDING
```

---

## 🔴 Cancel Order

DELETE `/api/v1/orders/{id}`

---

## 🟣 Update Order Status

PUT `/api/v1/orders/{id}/status?status=PROCESSING`

---

# 🧪 Running Tests

```
mvn test
```

---

# 📊 Code Coverage

```
mvn test
```

Open report:

```
target/site/jacoco/index.html
```

---

# 🧠 Design Decisions

* Used layered architecture (Controller → Service → Repository)
* Implemented custom exceptions for better error handling
* Added validation for order status transitions
* Used scheduler for background processing
* Logging includes correlation ID for traceability
* High test coverage focusing on business logic

---

# 🔐 Production Considerations

* Replace H2 with PostgreSQL
* Use Flyway for DB migrations
* Add authentication (JWT / OAuth2)
* Implement caching (Redis)
* Introduce event-driven architecture (Kafka)

---

# 🤖 AI Usage

This project was developed with assistance from AI tools (ChatGPT) for:

* Initial project scaffolding
* Generating boilerplate code
* Writing unit tests

All generated code was reviewed, validated, and improved with:

* Proper exception handling
* Optimized testing strategy
* Production-ready logging and structure

---

# 👨‍💻 Author

Akula Ramesh
