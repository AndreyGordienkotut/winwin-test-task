# WinWin Travel – Mini Test Task
Two Spring Boot services with PostgreSQL and Docker

### Overview
- Service A (auth-api) – JWT auth + calls Service B
- Service B (data-api) – internal transform service
- PostgreSQL - stores users + processing_log
- Docker Compose - runs entire system

### Architecture
- auth-api → localhost:8080
- data-api → localhost:8081
- internal communication → http://data-api:8081
- protected via X-Internal-Token
 -JWT required for /api/process

### Environment Variables
- файл .env в git было додано для більш швидкої перевірки, але розумію що в проді додавати його не потрібно)

POSTGRES_URL=jdbc:postgresql://postgres:5432/winwin

POSTGRES_USER=postgres

POSTGRES_PASSWORD=postgres

JWT_SECRET=12345t7vPfsdf42f8yQ9z3a5b6c7d8eF0g1H2i3J4k5L6m7N8o9p0q1R2s3T4u5V6w7X8y9Z0A1B2C3D4E5F6G7H8I9J0K1L2M3N4O5P6Q7R8S9T0U1V2W3X4Y5Z

JWT_EXPIRATION=3600000

INTERNAL_TOKEN=internal-secret

### Getting Started

mvn -f auth-api/pom.xml clean package -DskipTests
mvn -f data-api/pom.xml clean package -DskipTests

docker compose up -d --build

### API Flow Example
Register

curl -Method POST http://localhost:8080/api/auth/register `
-Headers @{ "Content-Type" = "application/json" } `
-Body '{"email":"a@a.com","password":"pass"}'

Login

curl -Method POST http://localhost:8080/api/auth/login `
-Headers @{ "Content-Type" = "application/json" } `
-Body '{"email":"a@a.com","password":"pass"}'

Save token

$response = curl -Method POST http://localhost:8080/api/auth/login `
-Headers @{ "Content-Type" = "application/json" } `
-Body '{"email":"a@a.com","password":"pass"}'
$token = ($response.Content | ConvertFrom-Json).token

Precess request

curl -Method POST http://localhost:8080/api/process `
-Headers @{
    "Authorization" = "Bearer $token"
    "Content-Type"  = "application/json"
} `
-Body '{"text":"hello winwin.travel"}'

Call without internal token

curl -Method POST http://localhost:8081/api/transform `
-Headers @{ "Content-Type" = "application/json" } `
-Body '{"text":"hello"}'

