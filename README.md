<p align="center">
    <img
    src="https://readme-typing-svg.demolab.com/?font=Fira+Code&weight=700&size=48&duration=3000&pause=1000&color=58A6FF&center=true&vCenter=true&width=500&lines=Duke%20Quiz"
    alt="Quiz System"
    />
</p>

<p align="center">
    A Spring-based quiz management system powered by PostgreSQL and integrated with Gopher Auth for authentication. It enables users to create, answer, and manage questions while maintaining a built-in history of their activity
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Language-Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java" />
  <img src="https://img.shields.io/badge/Framework-Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="Spring" />
  <img src="https://img.shields.io/badge/Database-PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL" />
  <img src="https://img.shields.io/badge/Container-Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker" />
</p>

---

## Introduction

Before being migrated to Java, the system was a local-only quiz management application that relied entirely on local storage. Later on, I decided that the project needs to be rewritten using Java and Spring Boot, introducing API endpoints for data management and core application functionality. The original version was developed as part of an educational application commissioned by a client.

The project aims to keep its design minimal while providing all the essential features required for a complete and reliable system. These include integration with Gopher Auth for authentication, a security layer, Docker support for containerization and deployment, and automated tests to help ensure reliability and maintainability.

## Features

### Questions Service

- **Question Management:** Handle the creation, retrieval, updating, and deletion of questions.

### History Service

- **History Management:** Handle the management and retrieval of history.

### JSON Import Script

- **JSON File Parsing:** Read and parse questions from a JSON file.
- **Data Import:** Import questions into the database.

### Authentication Layer (based on [Gopher Auth](https://github.com/Carbon32/gopher-auth))

- **Token Verification:** Verify authentication tokens included in requests.
- **User Identity:** Identify authenticated users and make their identity available to services.
- **Protected Routes:** Restrict access to endpoints that require authentication.

### Rate Limiting

- **Abuse Prevention:** Limit requests to reduce abusive activity.
- **IP-Based Tracking:** Track request activity by IP address to help prevent abuse.

Alongside these core features, the project includes various smaller features and implementation details that help tie everything together.

---

## Requirements

Before running the project, make sure you have the following installed:

- [Java](https://www.oracle.com/java/technologies/downloads/)
- [PostgreSQL](https://www.postgresql.org/download/)
- Bash, to execute the provided shell scripts
- **Gopher Auth** running and reachable by the Java API

If you plan to run the project using Docker, make sure [Docker](https://www.docker.com/) is installed and running.

---

## Getting Started

Follow these steps to configure and run the project locally.

### 1. Configure Environment Variables

Go to `src/main/resources/application.properties` in the project folder to configure the application's environment.

Make sure all required variables are properly configured before starting the application, including the settings needed to connect to your PostgreSQL database.

> **Note:** I believe the variable names are descriptive and self-explanatory.

Run PostgreSQL and your authentication API (this project is set to use Gopher Auth) before starting.

### 2. Run the Application

Once your environment is configured, start the application using:

```bash
bash run.sh
```

The application should now start.

---

## Running with Docker

If you prefer Docker, the project includes a dedicated script for running the application in a containerized environment.

Execute:

```bash
bash docker.sh
```

Before doing so, make sure Docker is installed and running and that the required environment configuration is in place.

For Docker-specific configuration options, refer to `docker.env.example`.

---

## Frontend Integration

Integrating this system in a frontend should be easy, especially if you're using the [vove-auth](https://github.com/Carbon32/vove-auth) example. But I will still provide an implementation under the name **vove-quiz**.

---

*I believe the project is suitable for real-world use, provided that it is properly integrated with the necessary supporting services and configured for your specific environment.*