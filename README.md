# MyHealth (Spring Boot + H2 + JWT) — fixed architecture

## Run
### Option A: IntelliJ
1. Open as Maven project
2. Enable annotation processing (for Lombok)
3. Run `kg.myhealth.MyHealthApplication`

### Option B: Maven CLI
```bash
mvn clean spring-boot:run
```

Open:
- http://localhost:8080 (frontend)
- http://localhost:8080/h2-console (H2 Console)
  - JDBC URL: `jdbc:h2:mem:myhealth`

## API
- POST `/api/auth/register` {email,password,fullName,age,gender}
- POST `/api/auth/login` {email,password}
- GET `/api/profile`
- PUT `/api/profile` {fullName,age,gender}
- POST `/api/health-data` {type,value,source,recordedAt?}
- GET `/api/health-data/latest`
- GET `/api/reports/summary`
- GET `/api/recommendations/latest`
- GET `/api/notifications`

JWT:
- Stored in browser localStorage by the demo frontend
- Sent as `Authorization: Bearer <token>`
