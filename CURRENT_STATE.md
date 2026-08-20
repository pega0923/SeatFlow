# SeatFlow Current State

Last updated: 2026-08-20 (Asia/Seoul)

This file is the handoff document for continuing SeatFlow in a new chat. Read `PROJECT_SPEC.md` first for the intended product and phase definitions, then use this file to determine what has actually been completed and what should happen next.

## Current Position

- Current phase: **Phase 1 — SeatFlow v0 (`GET /api/seats`)**
- Phase 0 status: **Complete and pushed**
- Phase 1 status: **Implemented and covered by an H2 API integration test; real MySQL endpoint verification pending**
- Current focus: Apply the explicit Phase 1 schema/seed SQL to local MySQL, then start the application and call `GET /api/seats` directly.

The local `seatflow` database and dedicated application account have been created. The user successfully started the application against MySQL using an environment-supplied password. No credential was shared with Codex or stored in tracked files.

## Completed

### Project documentation

- `AGENTS.md` contains the working rules for Codex.
- `PROJECT_SPEC.md` defines the product scope and Phase 0–12 roadmap.
- `CURRENT_STATE.md` records the actual working state and next steps.

### Local development environment

- Windows x64 development environment is in use.
- Temurin JDK 21.0.12 is installed.
- `JAVA_HOME` points to `C:\Program Files\Eclipse Adoptium\jdk-21.0.12.8-hotspot\`.
- Git for Windows 2.55.0.windows.4 is installed and available on PATH.
- MySQL Community Server 8.4.11 is installed.
- Windows service `MySQL84` is running with automatic startup.
- MySQL listens locally on TCP port 3306.
- IntelliJ IDEA 2026.1.4 is installed and the SeatFlow folder has been opened and trusted.
- Temporary installers and Initializr archives created during setup were deleted after installation.

### Git and GitHub

- Local repository branch: `main`
- Git author name: `pega`
- Git author email: `48636680+pega0923@users.noreply.github.com`
- GitHub repository: https://github.com/pega0923/SeatFlow
- Remote: `origin` → `https://github.com/pega0923/SeatFlow.git`
- `main` tracks `origin/main`.
- Initial project commit: `de7be9c chore: initialize Spring Boot project`
- Phase 0 completion commit: `52feebd chore: complete local MySQL configuration`

### Spring Boot project skeleton

- Group: `com.seatflow`
- Artifact/root project: `seatflow`
- Java: 21
- Spring Boot: 4.1.0
- Gradle Wrapper: 9.5.1 using Kotlin DSL
- Main class: `src/main/java/com/seatflow/SeatFlowApplication.java`

Current runtime dependencies:

- Spring Web MVC
- Spring Data JPA
- Bean Validation
- MySQL Connector/J

Current test dependencies:

- Spring Boot JPA, Validation, and Web MVC test starters
- H2 used only as an in-memory test database with MySQL compatibility mode
- JUnit Platform launcher

H2 is test-only. MySQL remains the required development and production database.

### Local database configuration

- The main application uses MySQL at `jdbc:mysql://127.0.0.1:3306/seatflow` by default.
- The application database username defaults to `seatflow_app`.
- The password must be supplied through the `SEATFLOW_DB_PASSWORD` environment variable.
- `SEATFLOW_DB_URL` and `SEATFLOW_DB_USERNAME` can override the defaults when needed.
- `.env.example` documents the expected variable names without containing real credentials.
- Local `.env` files and `application-local.properties` are ignored to reduce the risk of committing secrets.
- Open Session in View is disabled and Hibernate schema handling is set to `validate`; schema changes will remain explicit as entities are introduced.
- The local `seatflow` database and `seatflow_app` account have been created.

### Phase 1 seat listing implementation

- `Seat` maps the initial seat fields to the `seats` table.
- `SeatStatus` currently distinguishes operationally available and unavailable seats.
- `SeatRepository` reads seats ordered by seat number.
- `SeatService` owns the read-only transaction and maps entities to API response DTOs.
- `SeatController` exposes `GET /api/seats`.
- `SeatResponse` prevents the persistence entity from becoming the public API contract.
- `database/phase1-seat-schema.sql` explicitly creates the MySQL table and inserts three repeatable sample rows.
- `SeatControllerTest` verifies the Controller → Service → Repository → H2 → JSON flow.

## Verification Evidence

The following checks were actually run successfully:

```text
java -version
→ Temurin OpenJDK 21.0.12

mysql --version
→ MySQL Community Server 8.4.11

Get-Service MySQL84
→ Running / Automatic

TCP connection to 127.0.0.1:3306
→ Successful

gradlew.bat --version
→ Gradle 9.5.1 using JVM 21.0.12

gradlew.bat test
→ BUILD SUCCESSFUL

gradlew.bat build
→ BUILD SUCCESSFUL
→ Executable JAR generated at build/libs/seatflow-0.0.1-SNAPSHOT.jar

After implementing `GET /api/seats`:

gradlew.bat test
→ BUILD SUCCESSFUL

gradlew.bat build
→ BUILD SUCCESSFUL
→ `SeatControllerTest` passed using H2 in MySQL compatibility mode

After adding the environment-variable-based MySQL configuration:

gradlew.bat test
→ BUILD SUCCESSFUL

gradlew.bat build
→ BUILD SUCCESSFUL

Manual verification performed by the user:

SEATFLOW_DB_PASSWORD supplied in the local PowerShell session
→ gradlew.bat bootRun
→ Application started successfully against the local MySQL `seatflow` database
```

The real MySQL startup verification was performed directly by the user so the database passwords did not need to be exposed to Codex or written to the repository.

## Important Decisions

- Use the Gradle Wrapper; do not require a global Gradle installation.
- Keep MySQL as the real application database.
- Use H2 only to keep automated context tests independent from local MySQL credentials.
- Do not commit the MySQL root password, application DB password, API keys, or tokens.
- Use a dedicated MySQL application account rather than connecting the application as `root`.
- Supply local application database credentials through environment variables; do not place the real password in tracked configuration.
- Use `spring.jpa.hibernate.ddl-auto=validate` so the application checks the schema instead of silently changing it.
- Disable Open Session in View so database access remains within explicit application-layer operations.
- Return `SeatResponse` DTOs rather than exposing JPA entities directly through the API.
- Keep schema creation explicit in a versioned SQL file; the runtime application account retains CRUD-only privileges and Hibernate continues to validate rather than modify the schema.
- Do not add Security, JWT, Redis, Docker, Swagger/OpenAPI, or later-phase features yet.
- IntelliJ project files, Gradle caches, and build outputs are ignored by Git.

## Next Work — Ordered

### 1. Finish Phase 0

- [x] Create a local MySQL database named `seatflow`.
- [x] Create a dedicated local MySQL user for the application and grant only the privileges needed for `seatflow`.
- [x] Choose a safe way to supply local DB credentials without committing them.
- [x] Add minimal Spring datasource/JPA configuration.
- [x] Start the application against MySQL.
- [x] Verify the real database connection.
- [x] Re-run the automated tests and full Gradle build.
- [x] Commit and push the completed Phase 0 slice.

### 2. Start Phase 1 only after Phase 0 is verified

Implement the smallest working vertical slice for:

```http
GET /api/seats
```

Expected initial flow:

```text
Client
→ SeatController
→ SeatService
→ SeatRepository
→ MySQL
→ JSON response
```

Phase 1 initial scope:

- [x] Create the minimum `Seat` entity required by the specification.
- [x] Create `SeatRepository`.
- [x] Create `SeatService`.
- [x] Create `SeatController`.
- [x] Add minimal seed/test data only if needed to verify the endpoint.
- [x] Test repository/service/API behavior at an appropriate level.
- [ ] Run the application and call `GET /api/seats` directly.
- [x] Build and run all tests.
- [x] Explain the request flow and the concepts the user must understand.
- [ ] Commit and push the Phase 1 slice.

Do not add reservations, users, authentication, Redis, Docker, lottery logic, recurring reservations, reviews, waiting lists, or statistics during this slice.

## Known Non-Blocking Notes

- IntelliJ automatically recognized the Gradle files after the project was trusted; no separate `Load Gradle Project` prompt appeared.
- IntelliJ may suggest installing the advanced Spring plugin. It is not required for building, running, testing, or implementing the current project.
- The Spring Initializr-generated `HELP.md` is ignored by the generated `.gitignore` and is not part of the repository.
- MySQL passwords remain local and were never provided to Codex or added to tracked configuration.

## Updating This File

After a meaningful milestone, update at least these sections:

1. `Last updated`
2. `Current Position`
3. `Completed`
4. `Verification Evidence`
5. `Important Decisions` when a real decision was made
6. `Next Work — Ordered`
7. `Known Non-Blocking Notes` or blockers

Rules:

- Reflect actual code and Git state, not intentions.
- Mark a checkbox complete only after implementation and verification.
- Include the exact command or request used for important verification.
- Never record secrets.
- Keep future work limited to the next small slice from `PROJECT_SPEC.md`.
