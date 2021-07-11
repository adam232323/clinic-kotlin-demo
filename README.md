# Kotlin demo Clinic application

### Quick setup
`./gradlew bootRun`

### Application addresses
* Default api address: `http://localhost:8080`
* Swagger address: `{api-address}/swagger-ui.html`

### Properties configuration
If not configured Spring Boot loads with profile `default`
To run own app configuration create `application-default.yml` in `src/main/resources`


### Postman Rest API test collection 
https://www.getpostman.com/collections/c1c37906355c64cba09f

### DB Configuration
By default application runs on H2 database but it also can be deployed with MySql configuration. 
Example MySql configuration properties:
```
spring:
     datasource:
       url: jdbc:mysql://localhost:3306/kotlin-demo-clinic
       username: root
       password: root
       driver-class-name: com.mysql.cj.jdbc.Driver
     jpa:
       hibernate:
         ddl-auto: validate
     liquibase:
       enabled: true
```

### Liquibase (db migrations)
To use gradle plugin add own `liqubase.properties` file in `liquibase` directory based on `liqubase-example.properties`