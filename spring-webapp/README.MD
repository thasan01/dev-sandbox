# Spring WebApp
A simple web application build with SpringBoot framework.

## Features
- Model View Controller (MVC) Pattern
- REST API layer
-- JPA / Hiberbate based CRUD operations
-- Basic Auth
-- Secured with Role Based Access Controls (RBAC) 
-- Central error handling
- UI Layer
-- JSP / JSTL based View
- Utilities
-- Postman project with REST endpoints

## Setup
To build and run the application from command line:
```sh
mvn clean package
cd target
java -jar -Dspring.profiles.active=sandbox  spring-webapp.war
```