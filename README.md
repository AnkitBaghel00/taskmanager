# Task Management API

A production-style, multi-role Task/Project Management REST API built with Spring Boot. Built as a portfolio project to demonstrate backend engineering fundamentals: JWT authentication, role-based access control, relational data modeling, layered architecture, and containerized deployment.

## Tech Stack

- **Java 21**, **Spring Boot**
- **Spring Security** + **JWT** (stateless authentication)
- **Spring Data JPA** / **Hibernate**
- **MySQL**
- **Maven**
- **Docker** / **Docker Compose**
- **JUnit 5** + **Mockito** (unit testing)

## Features

- **Authentication**: Register and login with JWT-based, stateless authentication. Passwords hashed with BCrypt.
- **Role-based access control**: Three roles (`ADMIN`, `MANAGER`, `MEMBER`) enforced via Spring Security's `@PreAuthorize` and manual authorization checks where role alone isn't sufficient (e.g., task status updates allowed for the assignee *or* a manager/admin).
- **Project management**: Create, list, and fetch projects. Creation restricted to `MANAGER`/`ADMIN`.
- **Task management**: Create tasks under a project, assign to users, update status, list/filter tasks by project with pagination.
- **Global exception handling**: Centralized `@ControllerAdvice` with custom exception types (`ResourceNotFoundException`, `DuplicateResourceException`, `InvalidCredentialsException`) mapped to correct HTTP status codes, instead of raw stack traces.
- **Containerized**: Multi-stage `Dockerfile` and `docker-compose.yml` (app + MySQL, with healthcheck-based startup ordering).

## Architecture

Standard layered architecture:

```
Controller → Service (interface + impl) → Repository → Database
```

- **DTOs** used throughout — entities are never exposed directly in requests or responses.
- **JWT filter** (`JwtAuthFilter`) validates tokens once per request and populates Spring Security's context; downstream code reads identity via `SecurityContextHolder`, never re-parses tokens.
- **Entity relationships**: `Project.createdBy` and `Task.project` / `Task.assignedTo` are proper `@ManyToOne` JPA relationships, not raw foreign key IDs.

## Getting Started

### Prerequisites
- JDK 21
- Maven
- MySQL (or Docker, see below)

### Run locally
1. Clone the repo
2. Create a MySQL database: `CREATE DATABASE taskmanager_db;`
3. Copy `application.properties.example` to `application.properties` and fill in your local DB credentials and a JWT secret (generate one with `openssl rand -base64 64`)
4. Run: `mvn spring-boot:run`

### Run with Docker
```bash
docker compose up --build
```
This starts both the app and a MySQL container together, with the app waiting for MySQL to be healthy before starting. Set real values in a `.env` file (see `.env.example`) before running.

The API will be available at `http://localhost:8080`.

## API Endpoints

### Auth (public)
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register a new user (defaults to `MEMBER` role) |
| POST | `/api/auth/login` | Login, returns JWT token |

### Projects (requires authentication)
| Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | `/api/project/create` | Create a project | `MANAGER`, `ADMIN` |
| GET | `/api/project` | List all projects | Any authenticated user |
| GET | `/api/project/{id}` | Get a project by id | Any authenticated user |

### Tasks (requires authentication)
| Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | `/api/task/create` | Create a task under a project | `MANAGER`, `ADMIN` |
| GET | `/api/task?projectId={id}` | List/filter tasks, paginated | Any authenticated user |
| PATCH | `/api/task/update/{id}` | Update task status | Assignee, `MANAGER`, or `ADMIN` |

All protected endpoints require `Authorization: Bearer <token>`.

## Testing

Unit tests cover the service layer (`UserServiceImpl`) using JUnit 5 and Mockito, with dependencies mocked to isolate business logic from the database. Run with:
```bash
mvn test
```

## Known Limitations

- `Page<T>` is returned directly from the task listing endpoint; Spring Data notes this isn't guaranteed stable across versions. A `PagedModel`-based DTO wrapper would be a production-grade improvement.
- Test coverage is currently focused on `UserServiceImpl` as a representative example; `ProjectServiceImpl` and `TaskServiceImpl` would benefit from equivalent coverage.
- No refresh token mechanism — tokens are valid until expiry (24h) with no revocation.

## Author

Ankit Baghel
