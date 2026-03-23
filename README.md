# 📚 BookManagerApi

![Spring Boot](https://img.shields.io/badge/SpringBoot-3.x-brightgreen)
![Java](https://img.shields.io/badge/Java-17+-orange)
![JWT](https://img.shields.io/badge/Auth-JWT-blue)
![License](https://img.shields.io/badge/License-MIT-lightgrey)

A production-grade **Bookmark Management REST API** built with Spring Boot.
This API allows users to securely manage bookmarks with advanced features like tagging, favorites, search, and visit tracking.

---

## 📑 Table of Contents

* [🚀 Features](#-features)
* [🛠 Tech Stack](#-tech-stack)
* [📂 Project Structure](#-project-structure)
* [🔐 Authentication (JWT)](#-authentication-jwt)
* [📡 API Endpoints](#-api-endpoints)
* [📥 Sample Requests & Responses](#-sample-requests--responses)
* [⚙️ Getting Started](#️-getting-started)
* [🗃️ Configuration](#️-configuration)
* [🧹 Soft Delete Behavior](#-soft-delete-behavior)
* [🤝 Contributing](#-contributing)
* [📜 License](#-license)

---

## 🚀 Features

* 🔐 JWT Authentication (Register/Login)
* 📌 Full CRUD for Bookmarks
* 🗑 Soft Delete using Hibernate
* 📄 Pagination & Sorting
* 🔍 Full-text Search (title + description)
* 🏷 Tag-based Filtering (Many-to-Many)
* ⭐ Toggle Favorite Bookmarks
* 📊 Visit Tracking (count + last visited)
* ⚠️ Global Exception Handling
* 📦 DTO-based Architecture
* 👤 Role-based Access (ROLE_USER)

---

## 🛠 Tech Stack

* Java 17+
* Spring Boot
* Spring Security (JWT)
* Spring Data JPA + Hibernate
* PostgreSQL (or any JPA DB)
* Lombok
* Jakarta Validation
* JJWT (io.jsonwebtoken)
* Maven

---

## 📂 Project Structure

```
com.dipanshu.BookManagerApi
├── config/         
├── controller/     
├── dto/            
├── entity/         
├── exception/      
├── mapper/         
├── repository/     
├── security/       
```

---

## 🔐 Authentication (JWT)

* Users authenticate via `/auth/login`
* Server generates a **JWT token**
* Token must be sent in headers:

```
Authorization: Bearer <your-token>
```

* Token is validated using a custom JWT filter
* Role-based access enforced via Spring Security

---

## 📡 API Endpoints

### 🔑 Auth APIs

| Method | Endpoint       | Description     |
| ------ | -------------- | --------------- |
| POST   | /auth/register | Register user   |
| POST   | /auth/login    | Login & get JWT |

---

### 📚 Bookmark APIs (Require JWT)

| Method | Endpoint                  | Description          |
| ------ | ------------------------- | -------------------- |
| POST   | /bookmarks                | Create bookmark      |
| GET    | /bookmarks                | Get all (pagination) |
| GET    | /bookmarks/{id}           | Get by ID            |
| PUT    | /bookmarks/{id}           | Update               |
| DELETE | /bookmarks/{id}           | Soft delete          |
| GET    | /bookmarks/search         | Search               |
| GET    | /bookmarks/tags/{tagName} | Filter by tag        |
| PATCH  | /bookmarks/{id}/favorite  | Toggle favorite      |
| POST   | /bookmarks/{id}/visit     | Record visit         |

---

## 📥 Sample Requests & Responses

### 🧑 Register

**Request**

```json
{
  "username": "dipanshu",
  "password": "password123"
}
```

**Response**

```json
{
  "token": "jwt-token",
  "type": "Bearer"
}
```

---

### 🔐 Login

**Request**

```json
{
  "username": "dipanshu",
  "password": "password123"
}
```

**Response**

```json
{
  "token": "jwt-token",
  "type": "Bearer"
}
```

---

### 📌 Create Bookmark

**Request**

```json
{
  "title": "Google",
  "url": "https://google.com",
  "description": "Search Engine",
  "tags": ["search", "tech"]
}
```

---

### 🔍 Search Bookmark

```
GET /bookmarks/search?query=google&page=0&size=10
```

---

### ❌ Error Response

```json
{
  "success": false,
  "message": "Resource not found",
  "errorCode": "RESOURCE_NOT_FOUND",
  "timestamp": "2026-03-23T10:00:00"
}
```

---

## ⚙️ Getting Started

### 1️⃣ Clone Repository

```bash
git clone https://github.com/dipanshubatra/BookManagerApi.git
cd BookManagerApi
```

### 2️⃣ Configure Database

Update `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/bookmanager
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3️⃣ Run Application

```bash
mvn spring-boot:run
```

---

## 🗃️ Configuration

```properties
jwt.secret=your-secret-key
jwt.expiration=3600000

spring.datasource.url=...
spring.datasource.username=...
spring.datasource.password=...

spring.jpa.hibernate.ddl-auto=update
```

---

## 🧹 Soft Delete Behavior

* Bookmarks are **not permanently deleted**
* Marked as deleted using Hibernate
* Automatically excluded from queries

---

## 🤝 Contributing

Contributions are welcome!

1. Fork the repo
2. Create a feature branch
3. Commit changes
4. Open a Pull Request

---

## 📜 License

This project is licensed under the MIT License.

---

## 👨‍💻 Author

**Dipanshu Batra**

---

> 🚀 Production-ready backend project demonstrating real-world Spring Boot architecture and best practices.
