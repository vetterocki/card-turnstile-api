
# Turnstile API

The Turnstile API is designed to manage and process travel cards through turnstiles. It facilitates the interaction between various types of travel cards and turnstile devices, generating detailed reports for each card transaction.

![Static Badge](https://img.shields.io/badge/Java-17-brown)
![Static Badge](https://img.shields.io/badge/Spring_Framework-6-green)
![Static Badge](https://img.shields.io/badge/Spring_Boot-3.1-green)
![Static Badge](https://img.shields.io/badge/PostgreSQL-14-blue)


## Requirements
    
- Java 17
- Maven 3.12
- PostgreSQL 8+

  **or**

- Docker Engine	19.03.0+

## Running 

You can the API using spring-boot Maven plugin:
```
mvn spring-boot:run
```

Or if you have Docker installed, prerequisites above can be omitted, and the 
only command to executed looks like:

```
docker compose up -d
```

To run application unit and integration tests, following command must be executed:

```
mvn surefire:test
```

## Documentation

If application is running, the Swagger UI page is available at http://localhost:8080/swagger-ui.html