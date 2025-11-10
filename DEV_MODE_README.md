# Development Mode Instructions

## How to Enable Development Mode

There are several ways to enable development mode which bypasses authentication:

### Option 1: Using Spring Profiles (Recommended)
Start the application with the "dev" profile:
```bash
java -jar your-application.jar --spring.profiles.active=dev
```

Or if using Maven:
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### Option 2: Using Environment Variable
Set the DEV_MODE environment variable to "true":
```bash
export DEV_MODE=true
java -jar your-application.jar
```

On Windows:
```cmd
set DEV_MODE=true
java -jar your-application.jar
```

### Option 3: Using System Property
Start the application with the system property:
```bash
java -DDEV_MODE=true -jar your-application.jar
```

### Option 4: Using the Provided Scripts
- On Windows: Run `start-dev.bat`
- On Linux/Mac: Make the script executable with `chmod +x start-dev.sh` then run `./start-dev.sh`

## What Development Mode Does

When development mode is active:
- All authentication is bypassed
- All endpoints become accessible without JWT tokens
- No role checking is performed
- This is intended for development and testing only

## Security Warning

⚠️ **Never use development mode in production!** ⚠️
This mode completely bypasses all security measures and should only be used during development.