# Car Rental Backend

Spring Boot 3.3.5, Java 21, PostgreSQL and JWT.

## Local
Create database `car_rental_db`, update the local PostgreSQL password in
`src/main/resources/application.properties`, then run:

`mvn spring-boot:run`

Base URL: `http://localhost:8081/api`

## Railway variables
`SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`,
`SPRING_DATASOURCE_PASSWORD`, `JWT_SECRET`, `JWT_EXPIRATION`,
`FRONTEND_URL`, `ADMIN_EMAIL`, `ADMIN_PASSWORD`, `ADMIN_NAME`.
