# BikePartShop - Spring Advanced Course Project
[![Overall Code Coverage](https://codecov.io/gh/rumenbl/BikePartShop-Softuni2023/graph/badge.svg?token=UH9BXNL3O7)](https://codecov.io/gh/rumenbl/BikePartShop-Softuni2023)
[![CodeFactor](https://www.codefactor.io/repository/github/rumenbl/bikepartshop-softuni2023/badge)](https://www.codefactor.io/repository/github/rumenbl/bikepartshop-softuni2023)
## Overview
This project, created as part of the SoftUni Spring Advanced course, serves as a comprehensive Bike Part Shop application.

## Technologies Used
- Java 17
- Spring Boot
- Spring Security
- Thymeleaf
- HTML
- CSS
- Spring WebFlux
- Spring Web
- MySQL
- Maven
- Spring Data
- Bootstrap
- Docker
- Docker Compose
- Hibernate
- ModelMapper

## Project Functionality
- **Login**: Authentication for users.
- **Register**: User registration functionality.
- **Home (Unauthenticated Users)**: Landing page for non-logged-in users.
- **Home (Authenticated Users)**: Landing page for logged-in users.
- **Add Bike Part**: Ability to add new bike parts.
- **Edit Bike Part**: Modify existing bike part details.
- **Change User Role**: Allows altering user roles.
- **Delete User**: Capability to remove a user.
- **Create Orders**: Functionality for order creation.
- **About Us Page**: Details about the project or team.
- **User Profile**: View and manage user profiles.
- **View All Orders (Admin Menu)**: Admin dashboard to oversee all orders.
- **Thank You Page**: A page expressing gratitude for the user's order.
- **Currency Selector**: Feature to select currencies (USD, EUR, BGN) updated hourly through an exchange currency rate service and scheduler.
- **Configurable Update Time**: Updating currency rates every X seconds is configurable via application.yml. ***(Default is 1 hour)***

## Testing
- **JUnit and Mockito**: Used for unit tests.
- **MockMvc**: Employed for integration tests.

## CI/CD and DevOps
- **CodeQL**: Performs comprehensive code analysis, specifically focusing on identifying security vulnerabilities, bugs, and code quality issues within the project's codebase.
- **CircleCI**: Responsible for building and testing the project.
- **Codecov**: Monitors and maintains code coverage.
- **GitHub Actions**: Automates building and publishing of the Docker image to GitHub Packages.
- **Dependabot**: Used for dependency updates.
- **WatchTower**: Used to poll Docker Image from GitHub Packages and restart your running containers with the new image upon changes. (Included in ``deployment/docker-compose.yml``)
- **SonarCloud**: Used to analyze project for bugs, code smells, maintainability issues and such
## Running the Project (Demo API Key Given)
***If you want to run it with your own key use https://freecurrencyapi.com/***

### Run with Environment Variables in Command
```bash
docker run -p 8080:8080 \
  -e DB_HOST=localhost \
  -e DB_PORT=3306 \
  -e DB_DATABASE=bikepartstore \
  -e DB_USERNAME=root \
  -e DB_PASSWORD= \
  -e FCA_KEY=fca_live_yqvzPxx4bAhGTYaAGMSnUPv952bLTwk0v3oxyV7Y \
  ghcr.io/rumenbl/bikepartshop-softuni2023:master
```

### Run with a .env file
```bash
docker run -p 8080:8080 \
  --env-file .env \
  ghcr.io/rumenbl/bikepartshop-softuni2023:master
```

### Running with Docker Compose (Recommended)

- The app will be available at [http://localhost:8080](http://localhost:8080).
- No additional requirements are needed.
- The database is included in the compose file.
- Automatic container restart is enabled upon changes pushed to the repository. Watchtower polls the Docker Image for changes from GitHub Packages every 30 seconds.
- Health check for the database is included, which will restart on failure.
- Container will restart on failure.

To start the deployment:
```bash
cd deployment
docker-compose up -d --force-recreate
```

To tear down the deployment:
```bash
cd deployment
docker-compose down
```
