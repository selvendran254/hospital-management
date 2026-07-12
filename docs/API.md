# Hospital Management System — API Documentation

## Base URL

```
http://localhost:8080/api
```

## Authentication

All protected endpoints require a Bearer token in the Authorization header:

```
Authorization: Bearer <jwt_token>
```

### Register

```http
POST /auth/register
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "Password@123",
  "fullName": "John Doe",
  "phone": "+919876543210",
  "role": "PATIENT"
}
```

### Login

```http
POST /auth/login
Content-Type: application/json

{
  "email": "admin@hospital.com",
  "password": "Admin@123"
}
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "email": "admin@hospital.com",
  "role": "ADMIN",
  "fullName": "System Administrator"
}
```

### Forgot Password

```http
POST /auth/forgot-password
Content-Type: application/json

{ "email": "user@example.com" }
```

## Appointments

### Book Appointment (Patient)

```http
POST /appointments
Authorization: Bearer <token>

{
  "doctorId": "uuid",
  "appointmentDate": "2026-07-15",
  "startTime": "10:00",
  "reason": "General checkup"
}
```

### Get Doctor Availability

```http
GET /doctors/{id}/availability?date=2026-07-15
```

### Update Status (Doctor/Admin)

```http
PATCH /appointments/{id}/status
{ "status": "APPROVED" }
```

## Prescriptions

```http
POST /prescriptions
{
  "patientId": "uuid",
  "appointmentId": "uuid",
  "diagnosis": "Viral fever",
  "medications": "Paracetamol 500mg - 3 days",
  "instructions": "Rest and hydration"
}
```

### Download PDF

```http
GET /prescriptions/{id}/pdf
```

## Laboratory

```http
GET /lab/tests                    # List all tests
POST /lab/reports                 # Create report
GET /lab/reports/pending          # Pending reports
GET /lab/reports/completed        # Completed reports
POST /lab/reports/{id}/upload     # Upload report file
```

## Pharmacy

```http
GET /medicines                    # List medicines
POST /medicines                   # Add medicine
GET /pharmacy/alerts              # Stock & expiry alerts
POST /pharmacy/purchases          # Record purchase
POST /pharmacy/sales              # Record sale
```

## Blood Bank

```http
GET /blood/inventory              # All blood groups
POST /blood/inventory/add         # Add stock
POST /blood/requests              # Blood request
GET /blood/donors                 # Donor list
```

## Room Management

```http
GET /rooms                        # All rooms
GET /beds/available               # Available beds
POST /admissions                  # Admit patient
PATCH /admissions/{id}/discharge  # Discharge patient
```

## Billing

```http
POST /bills                       # Generate bill
GET /bills/patient/{patientId}    # Patient bills
POST /payments                    # Pay bill (dummy gateway)
```

## Admin Dashboard

```http
GET /admin/dashboard/stats
```

Returns: total patients, doctors, revenue, appointments, beds, blood units, pharmacy sales.

## Search

```http
GET /search?q=cardiology&type=departments
```

Types: `doctors`, `patients`, `departments`, `medicines`

## File Upload

```http
POST /files/upload
Content-Type: multipart/form-data

file: <binary>
folder: profiles | reports | prescriptions
```

## Error Responses

```json
{
  "timestamp": "2026-07-12T10:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/appointments"
}
```

## Swagger UI

Interactive API documentation: http://localhost:8080/api/swagger-ui.html
