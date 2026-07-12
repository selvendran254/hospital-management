# Hospital Management System

A complete, production-ready Hospital Management System with React frontend and Spring Boot backend.

## Architecture

```
hospital-management/
├── frontend/     React + TypeScript + Vite + Tailwind CSS
├── backend/      Spring Boot 3 + Java 21 + JWT + JPA
├── database/     PostgreSQL schema + seed data
├── docs/         Documentation
└── docker-compose.yml
```

## Tech Stack

| Layer | Technologies |
|-------|-------------|
| Frontend | React 18, TypeScript, Vite, Tailwind CSS, React Router, React Hook Form, Axios, TanStack Query, Recharts |
| Backend | Spring Boot 3.3, Java 21, Spring Security, JWT, Spring Data JPA, Hibernate |
| Database | PostgreSQL 16 |
| Storage | Local file storage (S3/MinIO ready interface) |
| API Docs | Swagger/OpenAPI 3 |
| Build | Maven (backend), npm (frontend) |

## Quick Start (Docker)

```bash
cd hospital-management
docker-compose up --build
```

| Service | URL |
|---------|-----|
| Frontend | http://localhost:3000 |
| Backend API | http://localhost:8080/api |
| Swagger UI | http://localhost:8080/api/swagger-ui.html |
| PostgreSQL | localhost:5432 |

## Local Development

### Prerequisites

- Node.js 20+
- Java 21 (JDK)
- Maven 3.9+
- PostgreSQL 16

### Database

```bash
psql -U postgres -c "CREATE DATABASE hospital_db;"
psql -U postgres -d hospital_db -f database/schema.sql
```

### Backend

```bash
cd backend
export JWT_SECRET=your-256-bit-secret-key-change-in-production
mvn spring-boot:run
```

### Frontend

```bash
cd frontend
cp .env.example .env
npm install
npm run dev
```

Frontend: http://localhost:5173

## Default Credentials

| Role | Email | Password |
|------|-------|----------|
| Admin | admin@hospital.com | Admin@123 |
| Doctor | dr.sharma@hospital.com | Doctor@123 |
| Patient | patient1@hospital.com | Patient@123 |

## User Roles & Dashboards

| Role | Dashboard Path | Key Features |
|------|---------------|--------------|
| Admin | `/admin` | Statistics, CRUD for all entities, analytics charts |
| Doctor | `/doctor` | Patients, prescriptions, schedule, leave management |
| Patient | `/patient` | Appointments, medical history, lab reports, bills |
| Receptionist | `/receptionist` | Register patients, check-in, billing, admit/discharge |
| Laboratory Staff | `/laboratory` | Lab tests, pending/completed reports |
| Pharmacist | `/pharmacist` | Medicine inventory, purchases, sales, alerts |

## Public Website Pages

Home, About, Departments, Doctors, Services, Health Packages, Appointment Booking, Emergency, Blood Bank, Laboratory, Pharmacy, Careers, Blog, Contact, Login, Register

## API Modules

- `/api/auth` — Authentication (JWT)
- `/api/doctors` — Doctor management
- `/api/patients` — Patient management
- `/api/appointments` — Appointment booking
- `/api/prescriptions` — Prescriptions
- `/api/lab` — Laboratory tests & reports
- `/api/pharmacy` — Pharmacy operations
- `/api/blood` — Blood bank inventory
- `/api/rooms` — Room & bed management
- `/api/bills` — Billing & payments
- `/api/admin` — Admin dashboard stats
- `/api/search` — Global search
- `/api/notifications` — Notification center

## Security

- JWT token-based authentication
- BCrypt password encryption
- Role-based access control (RBAC)
- CORS configuration
- Input validation (Bean Validation)
- Global exception handling
- Rate limiting structure (ready to enable)

## License

MIT
