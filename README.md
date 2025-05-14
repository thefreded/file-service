# Quarkus File Service

A simple RESTful API service built with Quarkus that provides file operations for the Task Service.

## Environment Variables

This service requires the following environment variables:

| Variable | Description                                    |
|----------|------------------------------------------------|
| `KEYCLOAK_URL` | Base URL of your Keycloak server               |
| `KEYCLOAK_REALM` | Keycloak realm name                            |
| `KEYCLOAK_CLIENT_ID` | Client ID for your application in Keycloak     |
| `KEYCLOAK_CLIENT_SECRET` | Client secret for your application in Keycloak |
| `KEYCLOAK_SCOPE` | OAuth scope for the client                     |
| `KEYCLOAK_AUDIENCE` | Target audience for the client                 |
| `DB_TYPE` | Database type (only postgresql support)        |
| `DB_URL` | JDBC connection URL for your database          |
| `DB_USERNAME` | Database username                              |
| `DB_PASSWORD` | Database password                              |
| `TASK_API_URL` | URL of the Task Service API                    |

## Dependencies

- Task Service must be running and accessible at the URL specified in `TASK_API_URL`

## Quick Start

1. Set the environment variables listed above
2. Ensure Task Service is running
3. Build the application: `./mvnw clean package`
4. Run the application: `./mvnw quarkus:dev`

## API Endpoints

| Method | Endpoint                      | Description |
|--------|-------------------------------|-------------|
| POST | `/api/document/task/{taskId}` | Upload a file to a specific task |
| GET | `/api/document/task/{taskId}` | Get all files associated with a task |
| GET | `/api/document/{taskFileId}`  | Get details of a specific file |


All endpoints require authentication via Keycloak.