# DailyReportSystem

DailyReportSystem is a Spring Boot web application for managing daily work reports. Users can register, log in, create daily reports, edit or delete their own reports, and add comments to report detail pages. Administrators can manage users from the admin area.

## Features

- User registration and form login with Spring Security
- Daily report list, detail, create, edit, and delete screens
- Comment creation on daily report detail pages
- Admin-only user list, create, and delete screens
- Thymeleaf templates and shared CSS for server-rendered pages

## Tech Stack

- Java 21
- Spring Boot 3.4
- Spring MVC
- Spring Security
- Spring Data JPA
- Thymeleaf
- MySQL
- Maven Wrapper

## Project Structure

```text
src/main/java/com/example/
  controller/     MVC controllers
  controller/admin/ Admin-only controllers
  entity/         JPA entities
  repository/     Spring Data repositories
  security/       Security configuration
src/main/resources/
  templates/      Thymeleaf views
  static/css/     Stylesheets
  application.properties
  data.sql        Initial seed data
src/test/java/    Tests
```

## Requirements

- Java 21
- MySQL 8 or compatible

The default database settings are in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/daily_report_system
spring.datasource.username=devuser
spring.datasource.password=password
```

Create the database and user before running the application, or update the properties for your local environment.

## Running Locally

```bash
./mvnw spring-boot:run
```

On Windows:

```bat
mvnw.cmd spring-boot:run
```

Then open:

```text
http://localhost:8080
```

## Tests

Run the test suite with:

```bash
./mvnw test
```

The current test suite includes a Spring Boot context loading test.

## Initial Data

`data.sql` seeds one regular user and one administrator. Passwords are stored as BCrypt hashes. If you change `spring.jpa.hibernate.ddl-auto`, review how schema creation and seed data should behave in your local environment.

## Development Notes

- Static assets are under `src/main/resources/static`.
- Thymeleaf templates are grouped by feature under `src/main/resources/templates`.
- `/admin/**` routes require `ROLE_ADMIN`.
- Report edit and delete actions are restricted to the report owner.
