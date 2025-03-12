# E-Commerce Backend

This project is the Java backend for a web-based electronics store’s checkout system. It provides a **RESTful API** for managing authentication, products, and customer operations.

## Milestone 1: Authentication

### Features:
- **User Registration** (Admin & Customer roles)
- **Login & JWT Authentication**
- **Role-Based Access Control (RBAC)**
- **Spring Security with JWT tokens**
- **Token Expiry & Refresh Mechanism**

### Tech Stack:
- **Spring Boot 3** (Backend Framework)
- **Spring Security** (Authentication & Authorization)
- **JWT (JSON Web Tokens)** (Token-based authentication)
- **MySQL Database** (In-memory database for development/testing)
- **Maven** (Build Tool)
- **Docker** (For containerization) - TBC

---

## Getting Started

### Prerequisites
- Java 17+
- Maven
- Docker (Optional for containerization)

### Installation & Running

#### 1. Clone the repository:
```sh
git clone https://github.com/your-repo/ecommerce-backend.git
cd ecommerce-backend
```
#### 2. Build the project:
```sh
mvn clean install
```
#### 3. Run the application:
```sh
mvn spring-boot:run
```
## API Endpoints

### Authentication

#### Register a New User
**Endpoint:**  
`POST /api/auth/register`  

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "password123",
  "role": "CUSTOMER"
}
```
**Response:**
```json
{
  "message": "User registered successfully"
}
```