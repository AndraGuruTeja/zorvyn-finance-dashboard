# Zorvyn Finance Dashboard

A simple but effective personal finance management system built with Spring Boot and a clean Vanilla JS frontend. It tracks income and expenses, provides some basic analytics, and handles user roles correctly.

## Tech Stack
*   **Backend:** Java 17, Spring Boot 3.2.5, Spring Security (JWT)
*   **Database:** H2 (for development/testing), PostgreSQL (for production)
*   **Frontend:** HTML5, CSS3, Vanilla JavaScript (no frameworks) — **Located in `src/main/resources/static`**
*   **API Specs:** Swagger UI / OpenAPI 3.0

## Features
*   **Unified Serving:** The entire app runs on a single port (**9090**). No separate server is required for the frontend.
*   **JWT Auth:** Secure login and registration with token-based sessions.
*   **Role-Based Access (RBAC):** 
    *   `ADMIN` - Can do everything: manage users and all financial records.
    *   `ANALYST` - Can view the dashboard and see all records but can't edit them.
    *   `VIEWER` - Can only see the dashboard overview.
*   **Financial Tracking:** Full CRUD for records with filtering (date range, category, type).
*   **Dashboard:** Instant totals for income/expenses, category breakdowns, and monthly trends.
*   **Data Accuracy:** All calculations are handled in the database using SQL aggregations.

## Quick API Overview
*   `POST /api/auth/login` - Get your access token
*   `GET /api/dashboard/summary` - Key stats for the dashboard
*   `GET /api/records` - List your transactions (paged and filterable)
*   `POST /api/users` - Create new users (Admin only)

## How to Run Locally

1.  **Clone the repo**
2.  **Build the project:**
    ```bash
    mvn clean install
    ```
3.  **Run the app:**
    ```bash
    mvn spring-boot:run
    ```
4.  **Access the App:**
    Open [http://localhost:9090](http://localhost:9090) in your browser. The frontend is automatically served by the Spring Boot server.

## Test Users (Seeded)
| Role | Email | Password |
| :--- | :--- | :--- |
| **Admin** | `admin@zorvyn.com` | `admin123` |
| **Analyst** | `jane@zorvyn.com` | `analyst123` |
| **Viewer** | `bob@zorvyn.com` | `viewer123` |

## Documentation
*   **Swagger UI:** [http://localhost:9090/swagger-ui.html](http://localhost:9090/swagger-ui.html)
*   **H2 Console:** [http://localhost:9090/h2-console](http://localhost:9090/h2-console) (JDBC URL: `jdbc:h2:mem:financedb`)

## Notes
The backend is designed to be very strict with validation. If you send an invalid email or an amount less than 0.01, the API will reject it with a specific `errors` list in the response. All sensitive actions are protected by PreAuthorize checks.
