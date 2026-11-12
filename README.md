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
| Framework  | Spring Boot, Spring Web, Spring Data MongoDB      |
| Security   | Spring Security, JWT (jjwt)                       |
| Realtime   | Spring WebSocket (STOMP)                          |
| Database   | MongoDB                                           |
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
- MongoDB Community Server 7+ (running locally on port 27017)

### Setup

1. **Get the database connection string** from the project owner (shared MongoDB Atlas cluster), and ask to have your IP added in Atlas under *Network Access*.

2. **Create your local config** (git-ignored):
   ```bash
   cp src/main/resources/application-local.properties.example src/main/resources/application-local.properties
   ```
   Then put the real `spring.data.mongodb.uri` in it. Mail settings are optional (only needed to send real emails).

3. **Run with the `local` profile**
   ```bash
   git clone https://github.com/Malekkk25/speed-enicar-backend.git
   cd speed-enicar-backend
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=local
   ```
   In IntelliJ: *Run > Edit Configurations > Active profiles = local*.

   Alternatively, use a local MongoDB (`mongodb://localhost:27017/speedenicarthage`, the default) and skip the profile.

The API is available at <http://localhost:8088/api>. Collections and indexes are created automatically. You can also override the connection with the `MONGODB_URI` environment variable.

### Demo accounts

On startup an empty database is filled with demo data (students, moods, grades, absences, appointments, records, messages...). Disable with `app.seed.enabled=false`.

| Role | Email | Password |
|------|-------|----------|
| ADMIN | `admin@enicar.tn` | `Admin@123` |
| SCOLARITY | `scolarite@enicar.tn` | `Scolarite@123` |
| PSYCHOLOGIST | `psy@enicar.tn` / `psy2@enicar.tn` | `Psy@12345` |
| STUDENT | `student@enicar.tn` (+ `yasmine@`, `omar@`, `ines@`, `mehdi@`, `salma@enicar.tn`) | `Student@123` |

## Project structure

```
src/main/java/tn/enicarthage/speedenicar_projet/
├── auth/                # login, register, refresh
├── security/            # JWT filter and services
├── config/              # security, WebSocket, MongoDB, async
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
