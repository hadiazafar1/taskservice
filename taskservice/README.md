# 📝 Task Management System

A modern **Task Management REST API** built using **Java 21**, **Spring Boot 3**, and powered by **RabbitMQ**, **PostgreSQL**, and **Docker**. Designed with clean architecture, TDD, DTO-based validation, and containerized microservice principles.

---

## 🚀 Features

- 📌 Create, update, and retrieve tasks
- ✅ Input validation with `@Valid`
- 📨 Publish task creation events via **RabbitMQ**
- 🧪 Unit tested services using JUnit & Mockito
- 🐳 Fully Dockerized (PostgreSQL, RabbitMQ, Spring Boot App)
- 🔒 Ready for Spring Security integration

---

## 🧰 Tech Stack

| Category           | Technology                     |
|--------------------|--------------------------------|
| Language           | Java 21                        |
| Framework          | Spring Boot 3                  |
| Messaging Queue    | RabbitMQ                       |
| Database           | PostgreSQL                     |
| Build Tool         | Maven                          |
| Testing            | JUnit 5, Mockito               |
| Containerization   | Docker, Docker Compose         |

---

## 🏁 Getting Started

### 🚨 Prerequisites

- Java 21
- Maven
- Docker + Docker Compose
- Git

---

### 🐳 Run with Docker

```bash
# Build JAR
mvn clean package

# Start all services (App, RabbitMQ, PostgreSQL)
docker compose up --build
Make sure Docker is running on your system.

🛠 REST Endpoints
Method	Endpoint	Description
POST	/api/tasks	Create a task
GET	/api/tasks	Get all tasks
PUT	/api/tasks/{id}	Update a task

📦 RabbitMQ
UI: http://localhost:15672

Default credentials:

Username: guest

Password: guest

The service publishes a TaskCreatedEvent when a new task is created.

🧪 Running Tests
bash
Copy
Edit
mvn test
All service layer tests are located in src/test/java.

📂 Project Structure
css
Copy
Edit
src/
 └── main/
     ├── java/com/example/taskservice
     │   ├── controller/
     │   ├── service/
     │   ├── dto/
     │   ├── config/
     │   └── messaging/
     └── resources/
         ├── application.properties
         ├── application-docker.properties
         └── ...
🧑‍💻 Author
Hadia Zafar
🌐 github.com/HadiaZafar1

