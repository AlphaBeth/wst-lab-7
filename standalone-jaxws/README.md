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

## JUDDI

### Installation

Just download and unpack archive from official site.

```bash
wget 'http://apache-mirror.rbc.ru/pub/apache/juddi/juddi/3.3.6/juddi-distro-3.3.6.zip'
unzip juddi-distro-3.3.6.zip
```

then run startup script

```bash
cd juddi-distro-3.3.6/juddi-tomcat-3.3.6/bin/
#run in background
./startup.sh
#NOT in background
./catailna.sh run
```

### Accessing JUDDI

By default, server starts up on port 8080. Open `localhost:8080` in your browser.

### Authorization

By default, ANY user can log in into user GUI (`http://localhost:8080/juddi-gui/home.jsp`) - use any combination 
of username and password.

> By default jUDDI ships with 2 publishers: root and uddi. Root is the owner of the repository, while the uddi user is 
  the owner of all the default tmodels and categorizations.
  
### JUDDI Administration

If you want to change some defaults (e.g. authorization scheme) you have to open administrator interface `http://localhost:8080/juddiv3/admin/home.jsp`.
It requires authorization, create tomcat user with role `uddiadmin` (edit file `juddi-distro-3.3.6/juddi-tomcat-3.3.6/conf/tomcat-users.xml`).
```xml
  <role rolename="uddiadmin"/>
  <user username="NAME" password="PASS" roles="uddiadmin" />
```
