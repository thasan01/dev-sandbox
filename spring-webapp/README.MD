# Spring WebApp
A simple web application build with SpringBoot framework.

## Features
1. Model View Controller (MVC) Pattern
2. REST API layer
    * JPA / Hiberbate based CRUD operations
    * Basic Auth
    * Secured with Role Based Access Controls (RBAC) and Spring Security 
    * Central error handling
3. UI Layer
    * JSP / JSTL based View
4. Utilities
    * Postman project with REST endpoints

## Setup
To build and run the application from command line:
```sh
mvn clean package
cd target
java -jar -Dspring.profiles.active=sandbox  spring-webapp.war
```




