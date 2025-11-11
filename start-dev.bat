@echo off
echo Starting EDUGO API in Development Mode...
echo ======================================
echo This will bypass all authentication for easier development
echo.

REM Set environment variable for development mode
set DEV_MODE=true

REM Start the Spring Boot application
mvnw spring-boot:run

echo.
echo Application stopped.
pause