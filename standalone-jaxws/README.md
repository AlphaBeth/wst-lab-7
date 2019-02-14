# Standalone exterminatus service

Standalone version of service - no application server is required, just build and run.

## Build and run

`mvn clean package`

`java -jar target/standalone-jaxws-1.0-jar-with-dependencies.jar`

## Configuration

- `config.properties`

Common configuration of service (endpoint URLs).

- `datasource.properties`

Properties for database connection, used by Hikari connection pool.

