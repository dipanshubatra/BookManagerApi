# BookManagerApi

![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=flat-square&logo=springboot&logoColor=white)
![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![JWT](https://img.shields.io/badge/Auth-JWT-000000?style=flat-square&logo=jsonwebtokens&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/Database-PostgreSQL-4169E1?style=flat-square&logo=postgresql&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-lightgrey?style=flat-square)

A production-grade **Bookmark Management REST API** built with Spring Boot — featuring JWT authentication, tagging, favorites, full-text search, visit tracking, and soft deletes.

🌐 **[Live API](https://apibookmark.vercel.app/)** · 🖥 **[Frontend App](https://github.com/dipanshubatra/BookmarkApifrontend)** · 📦 **[Backend Repo](https://github.com/dipanshubatra/BookManagerApi)**

---

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Authentication](#authentication)
- [API Reference](#api-reference)
- [Sample Requests & Responses](#sample-requests--responses)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [Soft Delete Behavior](#soft-delete-behavior)
- [Contributing](#contributing)
- [License](#license)

---

## Features

| Feature | Details |
|---|---|
| 🔐 JWT Authentication | Secure register & login flow |
| 📌 Full CRUD | Create, read, update, and delete bookmarks |
| 🗑 Soft Delete | Records marked deleted, never permanently removed |
| 📄 Pagination & Sorting | Efficient list retrieval at scale |
| 🔍 Full-text Search | Search across title and description |
| 🏷 Tag Filtering | Many-to-many tag relationships |
| ⭐ Favorites | Toggle bookmarks as favorites |
| 📊 Visit Tracking | Track visit count and last visited timestamp |
| ⚠️ Global Exception Handling | Consistent, structured error responses |
| 📦 DTO Architecture | Clean separation of API and domain layers |

---

## Tech Stack

- **Language:** Java 17+
- **Framework:** Spring Boot, Spring Security, Spring Data JPA
- **Database:** PostgreSQL (compatible with any JPA-supported DB)
- **Auth:** JWT via JJWT (`io.jsonwebtoken`)
- **ORM:** Hibernate with soft delete support
- **Utilities:** Lombok, Jakarta Validation
- **Build:** Maven

---

## Project Structure

```
com.dipanshu.BookManagerApi
├── config/          # Security & app configuration
├── controller/      # REST controllers
├── dto/             # Request & response DTOs
├── entity/          # JPA entities
├── exception/       # Global exception handling
├── mapper/          # Entity ↔ DTO mappers
├── repository/      # Spring Data repositories
└── security/        # JWT filter & utilities
```

---

## Authentication

This API uses **JWT (JSON Web Token)** for stateless authentication.

1. Register or log in via the `/auth` endpoints to receive a token.
2. Include the token in all subsequent requests:

```
Authorization: Bearer <your-token>
```

Token validation is handled by a custom Spring Security filter. All bookmark endpoints require a valid token.

---

## API Reference

### Auth

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/auth/register` | Register a new user |
| `POST` | `/auth/login` | Log in and receive a JWT |

### Bookmarks *(JWT required)*

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/bookmarks` | Create a bookmark |
| `GET` | `/bookmarks` | List all bookmarks (paginated) |
| `GET` | `/bookmarks/{id}` | Get a bookmark by ID |
| `PUT` | `/bookmarks/{id}` | Update a bookmark |
| `DELETE` | `/bookmarks/{id}` | Soft delete a bookmark |
| `GET` | `/bookmarks/search` | Full-text search |
| `GET` | `/bookmarks/tags/{tagName}` | Filter by tag |
| `PATCH` | `/bookmarks/{id}/favorite` | Toggle favorite status |
| `POST` | `/bookmarks/{id}/visit` | Record a visit |

---

## Sample Requests & Responses

### Register

```json
// POST /auth/register
{
  "username": "dipanshu",
  "password": "password123"
}

// Response 200 OK
{
  "token": "<jwt-token>",
  "type": "Bearer"
}
```

### Login

```json
// POST /auth/login
{
  "username": "dipanshu",
  "password": "password123"
}

// Response 200 OK
{
  "token": "<jwt-token>",
  "type": "Bearer"
}
```

### Create Bookmark

```json
// POST /bookmarks
{
  "title": "Google",
  "url": "https://google.com",
  "description": "Search Engine",
  "tags": ["search", "tech"]
}
```

### Search Bookmarks

```
GET /bookmarks/search?query=google&page=0&size=10
```

### Error Response

```json
{
  "success": false,
  "message": "Resource not found",
  "errorCode": "RESOURCE_NOT_FOUND",
  "timestamp": "2026-03-23T10:00:00"
}
```

---

## Getting Started

### Prerequisites

- Java 17+
- Maven
- PostgreSQL

### 1. Clone the Repository

```bash
git clone https://github.com/dipanshubatra/BookManagerApi.git
cd BookManagerApi
```

### 2. Configure the Database

Update `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/bookmanager
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Run the Application

```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`.

---

## Configuration

| Property | Description |
|----------|-------------|
| `jwt.secret` | Secret key for signing JWT tokens |
| `jwt.expiration` | Token expiry in milliseconds (e.g. `3600000` = 1 hour) |
| `spring.datasource.url` | JDBC connection URL |
| `spring.datasource.username` | Database username |
| `spring.datasource.password` | Database password |
| `spring.jpa.hibernate.ddl-auto` | Schema strategy (`update` recommended for dev) |

---

## Soft Delete Behavior

Bookmarks are never permanently removed from the database. Deletion sets a flag using Hibernate's soft delete support, and all queries automatically exclude soft-deleted records. This preserves data integrity while keeping the API clean for end users.

---

## Contributing

Contributions are welcome! To get started:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/my-feature`)
3. Commit your changes (`git commit -m 'Add my feature'`)
4. Push to your branch (`git push origin feature/my-feature`)
5. Open a Pull Request

Please keep code style consistent and add relevant tests where applicable.

---

## License

This project is licensed under the [MIT License](LICENSE).

---

Made with ☕ by **[Dipanshu Batra](https://github.com/dipanshubatra)**
