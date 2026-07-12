# Deployment Guide

## Docker Production Deployment

### 1. Environment Variables

Create `.env` in project root:

```env
JWT_SECRET=your-production-secret-min-256-bits-long
POSTGRES_PASSWORD=strong-db-password
```

### 2. Build & Run

```bash
docker-compose up --build -d
```

### 3. Verify

- Frontend: http://your-server:3000
- API Health: http://your-server:8080/api/public/health
- Swagger: http://your-server:8080/api/swagger-ui.html

## Manual Deployment

### Backend (JAR)

```bash
cd backend
mvn clean package -DskipTests
java -jar target/hospital-management-1.0.0.jar \
  --spring.datasource.url=jdbc:postgresql://host:5432/hospital_db \
  --spring.datasource.username=hospital_user \
  --spring.datasource.password=your-password \
  --app.jwt.secret=your-jwt-secret
```

### Frontend (Static)

```bash
cd frontend
npm run build
# Deploy dist/ to nginx, Vercel, or Netlify
```

### Nginx Reverse Proxy

```nginx
server {
    listen 80;
    server_name hospital.example.com;

    location / {
        root /var/www/hospital-frontend;
        try_files $uri $uri/ /index.html;
    }

    location /api {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

## AWS S3 Storage Migration

Replace local file storage with S3:

1. Implement `S3StorageService` implementing `StorageService` interface
2. Set `app.storage.type=s3` in application.yml
3. Configure AWS credentials via environment variables

```yaml
app:
  storage:
    type: s3
    s3:
      bucket: hospital-files
      region: ap-south-1
```

## Database Backup

```bash
pg_dump -U hospital_user hospital_db > backup.sql
```

## SSL/TLS

Use Let's Encrypt with Certbot for HTTPS in production.

## Monitoring

- Spring Boot Actuator endpoints available at `/api/actuator/health`
- Configure logging level in `application-prod.yml`
