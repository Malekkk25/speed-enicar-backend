# SpeedENICar: Backend

REST API for **SpeedENICar**, a student support platform for ENICarthage. It handles authentication, student follow-up, psychologist appointments and video consultations, confidential records, academic absences, documents, messaging and notifications.

> Frontend repository: [speed-enicar-frontend](https://github.com/Malekkk25/speed-enicar-frontend) (Angular)

## Features

- **Authentication**: register, login and token refresh with JWT (access and refresh tokens); role-based access (`STUDENT`, `PSYCHOLOGIST`, `SCOLARITY`)
- **Student module**: profile, mood tracking, journal, absences, appointments, documents, reports
- **Psychologist module**: dashboard, appointment workflow (reserve, confirm, cancel, complete), schedule and time slots, confidential records, student alerts
- **Video consultations**: consultation rooms (join / end) with WebRTC signaling over WebSocket (STOMP)
- **Scolarity module**: student management, grades and averages, prolonged and unjustified absences, at-risk student analysis
- **Documents**: upload (PDF, PNG, JPEG), validation / rejection, download
- **Messaging**: conversations between users with read status
- **Notifications**: in-app notifications (unread count, mark as read) and email notifications (Thymeleaf templates) when a consultation is ready
- **Cross-cutting**: global exception handling, unified `ApiResponse` wrapper, AOP logging, async task execution

## Tech stack

| Layer      | Tools                                             |
|------------|---------------------------------------------------|
| Language   | Java 17                                           |
| Framework  | Spring Boot, Spring Web, Spring Data JPA          |
| Security   | Spring Security, JWT (jjwt)                       |
| Realtime   | Spring WebSocket (STOMP)                          |
| Database   | PostgreSQL                                        |
| Mail       | Spring Mail, Thymeleaf email templates            |
| Other      | Bean Validation, Actuator, Lombok, Maven          |

## API overview

| Base path                    | Purpose                                      |
|------------------------------|----------------------------------------------|
| `/api/auth`                  | `register`, `login`, `refresh`               |
| `/api/student`               | profile, `/moods`, `/journal`, `/absences`, `/appointments`, `/documents`, `/reports` |
| `/api/psychologist`          | dashboard, appointments, records, alerts, schedule |
| `/api/consultations`         | `/room/{roomId}`, `/join`, `/end`            |
| `/api/scolarity`             | students, grades, absences, analysis         |
| `/api/documents`             | upload, download, validate, reject           |
| `/api/messages`              | conversations and messages                   |
| `/api/notifications`         | list, unread count, mark as read             |

## Getting started

### Prerequisites

- JDK 17+
- Maven (or use the included `mvnw`)
- PostgreSQL

### Setup

1. **Create the database**
   ```sql
   CREATE DATABASE speedenicarthage;
   ```

2. **Configure** `src/main/resources/application.properties` with your own values:
   ```properties
   server.port=8088

   spring.datasource.url=jdbc:postgresql://localhost:5432/speedenicarthage
   spring.datasource.username=YOUR_DB_USER
   spring.datasource.password=YOUR_DB_PASSWORD

   app.jwt.secret=YOUR_LONG_RANDOM_SECRET
   app.jwt.expiration-ms=86400000
   app.jwt.refresh-expiration-ms=604800000

   spring.mail.username=YOUR_EMAIL
   spring.mail.password=YOUR_APP_PASSWORD
   ```
   Keep real credentials out of Git: use environment variables (for example `${DB_PASSWORD}`) or a local, git-ignored config file.

3. **Run**
   ```bash
   git clone https://github.com/Malekkk25/speed-enicar-backend.git
   cd speed-enicar-backend
   ./mvnw spring-boot:run
   ```

The API is available at <http://localhost:8088/api>. Tables are created automatically (`spring.jpa.hibernate.ddl-auto=update`).

## Project structure

```
src/main/java/tn/enicarthage/speedenicar_projet/
├── auth/                # login, register, refresh
├── security/            # JWT filter and services
├── config/              # security, WebSocket, JPA, async
├── user/                # user entities and repositories
├── student/             # student features
├── module_psychologue/  # appointments, consultations, records, schedule
├── scolarity/           # academic affairs features
├── messaging/           # conversations and messages
├── notification/        # notifications and listeners
└── common/              # enums, DTOs, exceptions, aspects
```

## Author

**Malek** ([@Malekkk25](https://github.com/Malekkk25))
