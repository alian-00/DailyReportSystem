# Repository Guidelines

## Project Structure & Module Organization

This is a Java 21 Spring Boot daily report system using Maven, Spring MVC, Spring Security, Spring Data JPA, Thymeleaf, and MySQL.

- `src/main/java/com/example/` contains the application code.
- `controller/` holds MVC controllers, including `admin/` for administrator user management.
- `entity/` defines JPA entities such as `User`, `DailyReport`, and `Comments`.
- `repository/` contains Spring Data repository interfaces.
- `security/` contains authentication and authorization configuration.
- `src/main/resources/templates/` contains Thymeleaf views grouped by feature.
- `src/main/resources/static/css/style.css` contains shared styling.
- `src/main/resources/application.properties` and `data.sql` configure and seed the database.
- `src/test/java/` contains JUnit/Spring Boot tests.

## Build, Test, and Development Commands

- `./mvnw spring-boot:run` starts the application locally.
- `./mvnw test` runs the test suite.
- `./mvnw clean package` compiles, tests, and builds the executable jar under `target/`.
- On Windows Command Prompt or PowerShell, use `mvnw.cmd` instead of `./mvnw`.

The default configuration expects MySQL at `localhost:3306/daily_report_system` with username `devuser` and password `password`.

## Coding Style & Naming Conventions

Follow the existing Spring Boot style: package names are lowercase, classes are `PascalCase`, methods and fields are `camelCase`, and controllers return Thymeleaf template paths such as `reports/list`.

Use 4-space indentation in Java files. Prefer constructor injection for new code when practical. Keep comments short and useful.

## Testing Guidelines

Tests use JUnit 5 through `spring-boot-starter-test`, with Spring Security test support available. Place tests under `src/test/java` using the production package structure. Name test classes after the unit or slice under test, for example `ReportControllerTests`.

Run `./mvnw test` before submitting changes. Add focused tests for controller behavior, repository queries, validation, and security rules when changing those areas.

## Commit & Pull Request Guidelines

The current history uses concise Japanese summary commits, for example `日報管理アプリのコメント機能の実装完了`. Keep commit messages short, descriptive, and scoped to the change.

Pull requests should include a summary, testing performed, database or configuration changes, and screenshots for visible UI changes. Call out changes to security behavior, authentication, or seeded data.

## Security & Configuration Tips

Do not commit real credentials. Treat the values in `application.properties` as local-development defaults only. Be careful with `spring.jpa.hibernate.ddl-auto=create`, which recreates schema data on startup.
