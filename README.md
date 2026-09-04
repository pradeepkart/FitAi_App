# AI Fitness Tracker

A portfolio-ready monorepo for tracking workouts, weight, hydration, calories, goals, BMI, and personalized rule-based fitness guidance. The React/Vite client talks to a Spring Boot REST API secured with JWT; JPA/Hibernate persists data in MySQL.

## Features

- Registration/login with BCrypt passwords and expiring JWTs
- USER and ADMIN authorization enforced by Spring Security
- Ownership-safe workout, weight, water, calorie, and goal CRUD
- Profile, BMI, exercise library, dashboard summaries, and responsive UI
- Provider-neutral `AIRecommendationService`; included rule-based provider needs no API key
- Admin user list, application statistics, user deletion, and exercise management APIs
- Structured validation errors, safe environment configuration, CORS, Docker, and SPA rewrites

## Architecture

`React → Axios/JWT → Controller → Service → Repository → JPA/Hibernate → MySQL`

Backend packages follow `config`, `controller`, `dto`, `entity`, `exception`, `repository`, `security`, and `service`. Records are always located using both record ID and the authenticated user's ID, preventing cross-account access.

## Local setup

Requirements: Java 21, Maven 3.9+, and Node 20+. The default development profile uses a persistent local H2 database, so MySQL is optional during initial development.

Start the backend with no database configuration:

```powershell
$env:JWT_SECRET='generate-a-random-secret-with-at-least-32-characters'
cd Backend
mvn spring-boot:run
```

To use MySQL 8 instead, first run `CREATE DATABASE fitness_tracker;`, then set `SPRING_PROFILES_ACTIVE=mysql`, `DB_USERNAME`, `DB_PASSWORD`, and optionally `DB_URL` before starting the backend. The MySQL profile is also the production profile used by Render.

Start the client in another terminal:

```powershell
cd Frontend
Copy-Item .env.example .env
npm install
npm run dev
```

Open `http://localhost:5173`; the API runs at `http://localhost:8080/api`.

## Environment variables

Backend: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET`, `JWT_EXPIRATION` (default `86400000`), `FRONTEND_URL`, optional `AI_API_KEY`, `DDL_AUTO`, and Render-provided `PORT`. Password-reset email uses `MAIL_USERNAME` with a Gmail address and `MAIL_PASSWORD` with a Google App Password.

Frontend: `VITE_API_BASE_URL` (include `/api`). Never commit actual credentials.

## API overview

| Area                        | Endpoints                                                                             |
| --------------------------- | ------------------------------------------------------------------------------------- |
| Auth                        | `POST /api/auth/register`, `POST /api/auth/login`                                     |
| Profile                     | `GET, PUT /api/user/profile`                                                          |
| Workouts                    | `GET, POST /api/workouts`; `GET, PUT, DELETE /api/workouts/{id}`                      |
| Exercises                   | `GET /api/exercises[/{id}]`; ADMIN `POST, PUT, DELETE`                                |
| Weight/water/calories/goals | `GET, POST /api/{resource}`; `PUT, DELETE /api/{resource}/{id}`                       |
| Fitness/AI                  | `GET /api/fitness/bmi`, `POST /api/ai/recommendation`                                 |
| Admin                       | `GET /api/admin/stats`, `GET /api/admin/users[/{id}]`, `DELETE /api/admin/users/{id}` |

Registration and login return `{ token, user }`. The client stores these locally and its Axios interceptor sends `Authorization: Bearer <token>`. Spring's JWT filter validates the signature/expiration, loads the role, and creates the security context. A `401` clears the client session.

## Docker and Render

Build locally with `docker build -t ai-fitness-api Backend` and run while supplying the environment variables above. The multi-stage image compiles with Maven and starts the Java 21 JAR on `${PORT:8080}`.

For MySQL 8 on Render, create a private Docker service from the official MySQL 8 image. Add a persistent disk mounted at `/var/lib/mysql`; without it, service restarts lose database files. Set `MYSQL_DATABASE=fitness_tracker`, `MYSQL_USER=fitness_user`, and secure `MYSQL_PASSWORD`/`MYSQL_ROOT_PASSWORD` secret values. Do not expose MySQL publicly.

Create the backend Render Web Service with repository root `Backend`, Docker runtime, and `Backend/Dockerfile` (if Render requests a path from repository root). Set:

```text
DB_URL=jdbc:mysql://<private-mysql-host>:3306/fitness_tracker
DB_USERNAME=fitness_user
DB_PASSWORD=<secret>
JWT_SECRET=<long-random-secret>
JWT_EXPIRATION=86400000
FRONTEND_URL=https://<project>.vercel.app
```

Use the MySQL service's Render private hostname. Deploy and verify an auth request at `https://<backend>.onrender.com/api/auth/login` before connecting the client.

## Vercel

Import the same repository, set Root Directory to `Frontend`, Build Command to `npm run build`, Output Directory to `dist`, and `VITE_API_BASE_URL=https://<backend>.onrender.com/api`. `vercel.json` provides SPA route fallback. After the first deployment, copy the exact Vercel origin into backend `FRONTEND_URL` and redeploy the backend.

## Verification checklist

- Register; duplicate email fails; valid login works; wrong password fails
- Anonymous protected request is rejected; USER cannot call ADMIN APIs
- Create/read/update/delete each owned tracker record
- A second user cannot address the first user's record ID
- BMI uses latest weight; AI response includes disclaimer
- Invalid and expired JWTs fail; validation errors are readable
- Refresh preserves login; logout clears it; mobile layout works
- Production Vercel origin passes CORS and all deep links load

## GitHub

```bash
git init
git add .
git commit -m "Build AI Fitness Tracker"
git branch -M main
git remote add origin <repository-url>
git push -u origin main
```

## Limitations and next ideas

The included AI is deterministic general guidance, not medical advice, and `AI_API_KEY` is reserved for a future external provider implementation. Useful next steps are controller/service tests, charts, pagination, refresh tokens, email verification, password reset, and an audit trail for admin actions.
