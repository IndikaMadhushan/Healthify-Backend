# Healthify Backend (Personal Health Monitoring) — Spring Boot API

Backend service for the **Healthify / Personal Health Monitoring** platform.  
It provides REST APIs for user/doctor workflows, authentication & authorization, file handling, and integrations (email, storage, chatbot), backed by a MySQL database.

> Repo: `IndikaMadhushan/Healthify-Backend`

---

## What this project does

This project is a **Spring Boot (Java 17)** backend that powers a health monitoring application. It handles:

- Secure **authentication/authorization** using **Spring Security + JWT**
- **CRUD** style APIs for platform modules (patients, doctors, records, etc.)
- **File uploads** (multipart) and storage integration (Supabase buckets)
- **Email sending** (e.g., verification / contact-us / notifications)
- **Scheduled tasks** (Spring Scheduling enabled)
- Optional integration for an **AI chatbot** (OpenAI API key configured via env var)

---

## Tech Stack

**Backend**
- Java 17
- Spring Boot 3.5.x
- Spring Web (REST APIs)
- Spring Data JPA (Hibernate)
- Spring Security
- Bean Validation (Jakarta Validation)

**Auth / Utilities**
- JWT (io.jsonwebtoken `jjwt`)
- ModelMapper
- Lombok
- spring-dotenv (env loading)

**Database**
- MySQL 8

**Integrations**
- Spring Mail (SMTP)
- Supabase (storage buckets)
- OpenPDF (PDF generation)
- OpenAI API (chatbot)

**DevOps / Tooling**
- Maven Wrapper (`mvnw`)
- Docker + Docker Compose
- Jenkins pipeline (`jenkinsfile`)

---

## Features (high level)

- **JWT-based authentication** (login/register + protected endpoints)
- **Role-based access** via Spring Security
- **MySQL persistence** via JPA/Hibernate (`ddl-auto=update`)
- **CORS enabled** for frontend dev (`http://localhost:5173`)
- **File upload support** with size limits configurable via env vars
- **Email support** (SMTP)
- **Supabase storage configuration** (profile images, doctor verification docs, medical files)
- **Scheduling enabled** (`@EnableScheduling`)
- **Dockerized deployment** (backend + mysql)

---

## My Role

**Team Leader / Backend Lead**

- Led the backend architecture and implementation of the majority of server-side functionality.
- Designed and implemented core API structure, security (JWT + Spring Security), persistence layer (JPA), and integrations.
- Coordinated the backend development workflow and ensured the system was deployable using Docker.



---

## How to Run

You can run this backend in **two ways**:

### Option A — Run with Docker Compose (recommended)

**Prerequisites**
- Docker + Docker Compose installed

**1) Create an env file**
Your `docker-compose.yml` references an env file at:

- `/etc/healthify/app.env`

On Linux/macOS you can create it like:

```bash
sudo mkdir -p /etc/healthify
sudo nano /etc/healthify/app.env
```

**2) Add environment variables**
Minimum required (based on `src/main/resources/application.properties`):

```env
# --- Database ---
DATABASE_URL=jdbc:mysql://mysql:3306/healthy
DATABASE_USERNAME=healthy_user
DATABASE_PASSWORD=healthy_pass

# --- JWT ---
JWT_SECRET=change_me_to_a_long_secure_secret
JWT_EXPIRATION_MS=86400000

# --- Upload limits (examples) ---
FILE_UPLOAD_SIZE_LIMIT=10MB
FILE_UPLOAD_REQUEST_SIZE_LIMIT=10MB

# --- Mail (examples) ---
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your_email@example.com
MAIL_PASSWORD=your_email_password_or_app_password

# --- Contact-us (optional) ---
CONTACT_US_TO_EMAIL=your_email@example.com
CONTACT_US_FROM_EMAIL=healthify-noreply@example.com

# --- Supabase ---
SUPABASE_URL=your_supabase_url
SUPABASE_SERVICE_KEY=your_supabase_service_key
SUPABASE_BUCKET_PROFILE_IMAGES=profile-images
SUPABASE_BUCKET_DOCTOR_VERIFICATION_DOCS=doctor-verification-docs
SUPABASE_BUCKET_MEDICAL_FILES=medical-files

# --- Optional: OpenAI chatbot ---
OPENAI_API_KEY=your_openai_api_key

# --- Optional: encryption key ---
DATA_ENCRYPTION_KEY=
```

**3) Start services**
From the repo root:

```bash
docker compose up -d
```

**Ports**
- MySQL: `localhost:3306`
- Backend API: `localhost:8083`

---

### Option B — Run locally with Maven (dev)

**Prerequisites**
- Java 17 installed
- MySQL running locally
- Maven (or use the Maven wrapper `./mvnw`)

**1) Create a `.env` file (optional but recommended)**
This project uses `spring-dotenv`, so you can create a `.env` at the project root and place the same variables shown above.

**2) Run**
```bash
./mvnw spring-boot:run
```

If you prefer building a jar:
```bash
./mvnw clean package
java -jar target/*.jar
```

---

## Notes / Configuration

- Main application entry point:  
  `src/main/java/com/healthcare/personal_health_monitoring/PersonalHealthMonitoringApplication.java`
- CORS currently allows: `http://localhost:5173` (frontend dev)
- Database schema auto update is enabled: `spring.jpa.hibernate.ddl-auto=update`

