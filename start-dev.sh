#!/bin/bash
echo "Starting EDUGO API in Development Mode..."
echo "======================================"
echo "This will bypass all authentication for easier development"
echo

# Set environment variable for development mode
export DEV_MODE=true

# Start the Spring Boot application
./mvnw spring-boot:run

echo
echo "Application stopped."