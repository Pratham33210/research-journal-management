# Quick Start Guide

## 🚀 Getting Started

This guide will help you set up and run the Research Journal Management System locally.

## Prerequisites

Before you begin, ensure you have the following installed:

- **Java 21 JDK** ([Download](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html))
- **Node.js 18+** ([Download](https://nodejs.org/))
- **MySQL 8.0+** ([Download](https://dev.mysql.com/downloads/mysql/))
- **Maven 3.6+** ([Download](https://maven.apache.org/download.cgi))
- **Git** ([Download](https://git-scm.com/))

## Step 1: Database Setup

### 1.1 Start MySQL Server

```bash
# Windows
mysql --version  # Check if MySQL is installed

# Start MySQL service
# Windows: Services → MySQL80 → Start
# Or use MySQL Shell: mysqld
```

### 1.2 Create Database

```bash
# Open MySQL Command Line Client
mysql -u root -p

# In MySQL shell, create the database:
CREATE DATABASE research_journal_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# View databases to confirm
SHOW DATABASES;

# Exit
exit
```

### 1.3 Import Schema

```bash
# From project root directory
mysql -u root -p research_journal_db < database/schema.sql

# Verify tables were created
mysql -u root -p research_journal_db -e "SHOW TABLES;"
```

## Step 2: Backend Setup

### 2.1 Configure Database Credentials

Edit `backend/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/research_journal_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root  # Change this to your MySQL password
```

### 2.2 Build and Run Backend

```bash
# Navigate to backend directory
cd backend

# Clean and install dependencies
mvn clean install

# Run the application
mvn spring-boot:run
```

**Expected Output:**
```
...
Started ResearchJournalManagementApplication in 10.123 seconds
```

Backend is now running on: `http://localhost:8080`

### 2.3 Verify Backend

Open browser and navigate to:
- API Docs: `http://localhost:8080/api/swagger-ui.html`
- Health Check: `http://localhost:8080/api/actuator/health` (if enabled)

## Step 3: Frontend Setup

### 3.1 Install Dependencies

```bash
# Navigate to frontend directory
cd frontend

# Install npm packages
npm install
```

### 3.2 Configure Environment

Create `frontend/.env` file (copy from `.env.example`):

```bash
# Copy example file
cp .env.example .env
```

Edit `.env` if needed:
```
VITE_API_URL=http://localhost:8080/api
VITE_APP_ENV=development
```

### 3.3 Run Frontend

```bash
# Start Vite development server
npm run dev
```

**Expected Output:**
```
  VITE v5.0.7  ready in 123 ms

  ➜  Local:   http://localhost:3000/
  ➜  press h to show help
```

Frontend is now running on: `http://localhost:3000`

## Step 4: Test the Application

### 4.1 Register a New User

1. Open `http://localhost:3000` in your browser
2. Click "Register"
3. Fill in the registration form:
   - First Name: John
   - Last Name: Doe
   - Email: john@example.com
   - Affiliation: University of Example
   - Role: Author
   - Password: Test@123
4. Click "Register"

### 4.2 Login

1. You'll be redirected to login page
2. Enter:
   - Email: john@example.com
   - Password: Test@123
3. Click "Login"

### 4.3 Test Features

- **Submit Paper**: Click "Submit Paper" and fill in paper details
- **View Dashboard**: See your submitted papers
- **View Paper**: Click on a paper to see full details

## Troubleshooting

### Backend Issues

**Port 8080 already in use:**
```bash
# Windows - Find process using port 8080
netstat -ano | findstr :8080

# Change port in application.properties
server.port=8081
```

**Database Connection Failed:**
```
Error: Connection refused
Solution: 
1. Verify MySQL is running
2. Check credentials in application.properties
3. Verify database name is correct
```

**Compilation Errors:**
```bash
# Clean cache and rebuild
mvn clean install -U
```

### Frontend Issues

**Port 3000 already in use:**
```bash
# Run on different port
npm run dev -- --port 3001
```

**Cannot connect to API:**
1. Verify backend is running on port 8080
2. Check proxy configuration in vite.config.js
3. Clear browser cache

**Module not found error:**
```bash
# Reinstall dependencies
rm -rf node_modules package-lock.json
npm install
```

### MySQL Issues

**Cannot connect to MySQL:**
```bash
# Verify MySQL is running
mysql -u root -p

# If it asks for password but you have none, try:
mysql -u root

# Create a new user if needed
CREATE USER 'research'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON research_journal_db.* TO 'research'@'localhost';
FLUSH PRIVILEGES;
```

## Common Commands

### Backend Commands

```bash
# Build without running
mvn clean install

# Run tests
mvn test

# Package as JAR
mvn clean package

# Run specific profile
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

### Frontend Commands

```bash
# Development server
npm run dev

# Build for production
npm run build

# Preview production build
npm run preview

# Run linter
npm run lint
```

## Project Architecture

```
┌─────────────────────────────────────────────────────┐
│               Frontend (React)                      │
│         http://localhost:3000                       │
└──────────────────────┬──────────────────────────────┘
                       │
                   HTTP/REST
                       │
┌──────────────────────▼──────────────────────────────┐
│         Backend (Spring Boot)                       │
│         http://localhost:8080/api                   │
└──────────────────────┬──────────────────────────────┘
                       │
                    JDBC
                       │
┌──────────────────────▼──────────────────────────────┐
│      Database (MySQL)                               │
│      research_journal_db                            │
└─────────────────────────────────────────────────────┘
```

## Next Steps

1. **Explore API**: Visit `http://localhost:8080/api/swagger-ui.html`
2. **Read Documentation**: Check `README.md` for detailed information
3. **Create Test Data**: Register multiple users and submit test papers
4. **Customize**: Modify colors, add more features as needed

## Support

For issues or questions:
1. Check this guide first
2. Review error messages carefully
3. Check backend logs for API errors
4. Check browser console for frontend errors
5. Create an issue in the repository

## Learning Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [React Documentation](https://react.dev)
- [MySQL Documentation](https://dev.mysql.com/doc/)
- [REST API Best Practices](https://restfulapi.net)

Happy developing! 🎉
