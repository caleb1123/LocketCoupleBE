# Getting started with Java and Spring-boot

![](https://github.com/flavours/documentation/workflows/CI/badge.svg)

## Quick start project

### Clone the repository

```
git clone https://github.com/caleb1123/LocketCoupleBE
```

### Build image project

```
docker build -t locket-backend:latest .

```

The project includes a ``web`` service, running the Java code, and a ``db`` service, running a Postgres database.
See the ``docker-compose.yml`` file for details.

### Run the project local

```
docker run -d -p 8080:8080 --name locket-backend-container locket-backend:latest
````

Containers for both services will be launched. The project can be reached at http://localhost:8080.

### Explanation of the Sections:
- **Swagger Documentation**: http://localhost:8080/swagger-ui/index.html#/







