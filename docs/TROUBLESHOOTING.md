# Troubleshooting Guide

## Common Issues and Solutions

### Backend (Spring Boot) Issues

#### Issue 1: Maven Build Fails
```
Error: BUILD FAILURE
```

**Solution:**
```bash
# Clear Maven cache
mvn clean install -U

# If still failing, check Java version
java -version  # Should be Java 21

# Update JAVA_HOME environment variable if needed
# Windows: setx JAVA_HOME "C:\Program Files\Java\jdk-21"
```

#### Issue 2: Cannot Connect to Database
```
Error: HikariPool-1 - Exception during pool initialization
com.mysql.cj.jdbc.exceptions.CommunicationsException: Communications link failure
```

**Solution:**
1. Verify MySQL is running:
   ```bash
   # Windows: Services → MySQL80 → Check if Started
   # Or use MySQL Shell: mysqld
   ```

2. Check connection string in `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/research_journal_db
   spring.datasource.username=root
   spring.datasource.password=root  # Change to your password
   ```

3. Verify database exists:
   ```bash
   mysql -u root -p
   SHOW DATABASES;
   ```

#### Issue 3: Port 8080 Already in Use
```
Error: Address already in use: bind
```

**Solution:**
```bash
# Find process using port 8080 (Windows)
netstat -ano | findstr :8080

# Kill the process
taskkill /PID <PID> /F

# Or change port in application.properties
server.port=8081
```

#### Issue 4: Lombok Not Working
```
Error: cannot find symbol class Data
```

**Solution:**
```bash
# Make sure IDE has Lombok plugin installed
# IntelliJ: File → Settings → Plugins → Search "Lombok"
# VS Code: Extension Pack for Java includes Lombok support

# Or regenerate IDE files
mvn clean install
# Then rebuild IDE cache
```

#### Issue 5: JWT Token Errors
```
Error: JWT token expired
Error: JWT token invalid
```

**Solution:**
```properties
# In application.properties:
jwt.secret=researchJournalManagementSecretKeyFor256BitAESEncryption2024
jwt.expiration=86400000  # 24 hours in milliseconds

# Increase expiration if needed for development
jwt.expiration=604800000  # 7 days
```

### Frontend (React) Issues

#### Issue 1: Port 3000 Already in Use
```
Error: EADDRINUSE: address already in use :::3000
```

**Solution:**
```bash
# Run on different port
npm run dev -- --port 3001

# Or kill process using port 3000 (Windows)
netstat -ano | findstr :3000
taskkill /PID <PID> /F
```

#### Issue 2: Cannot Find Module
```
Error: Module not found: Can't resolve 'react'
```

**Solution:**
```bash
# Reinstall dependencies
rm -r node_modules
npm install

# Or if using npm cache issue:
npm cache clean --force
npm install
```

#### Issue 3: API Connection Failed
```
Error: 404 Not Found
Error: Cannot connect to backend
```

**Solution:**
1. Check backend is running on port 8080
2. Verify proxy in `vite.config.js`:
   ```javascript
   proxy: {
     '/api': {
       target: 'http://localhost:8080',
       changeOrigin: true
     }
   }
   ```
3. Check network tab in browser DevTools
4. Verify CORS is enabled on backend

#### Issue 4: Axios Request Fails
```
Error: Network error
Error: CORS policy error
```

**Solution:**
1. Add CORS configuration to Spring Boot:
   ```java
   @Configuration
   public class CorsConfig {
       @Bean
       public WebMvcConfigurer corsConfigurer() {
           return new WebMvcConfigurer() {
               @Override
               public void addCorsMappings(CorsRegistry registry) {
                   registry.addMapping("/api/**")
                       .allowedOrigins("http://localhost:3000")
                       .allowedMethods("*")
                       .allowedHeaders("*");
               }
           };
       }
   }
   ```

2. Ensure token is sent in requests:
   ```javascript
   axios.defaults.headers.common['Authorization'] = 
       `Bearer ${localStorage.getItem('token')}`;
   ```

#### Issue 5: Vite Build Fails
```
Error: Plugin @vitejs/plugin-react failed
```

**Solution:**
```bash
# Clear Vite cache
rm -r .vite

# Reinstall dependencies
npm install

# Rebuild
npm run build
```

### Database (MySQL) Issues

#### Issue 1: Cannot Access MySQL
```
Error: Access denied for user 'root'@'localhost'
```

**Solution:**
```bash
# Try without password
mysql -u root

# If you forgot password, reset it (Windows)
# 1. Stop MySQL service
# 2. Start with skip-grant-tables
mysqld --skip-grant-tables

# 3. Connect and reset
mysql -u root
FLUSH PRIVILEGES;
ALTER USER 'root'@'localhost' IDENTIFIED BY 'new_password';
```

#### Issue 2: Database Not Found
```
Error: Unknown database 'research_journal_db'
```

**Solution:**
```bash
# Create database
mysql -u root -p
CREATE DATABASE research_journal_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# Import schema
mysql -u root -p research_journal_db < database/schema.sql

# Verify
mysql -u root -p research_journal_db -e "SHOW TABLES;"
```

#### Issue 3: Tables Not Created
```
Error: Table 'research_journal_db.users' doesn't exist
```

**Solution:**
```bash
# Check if tables exist
mysql -u root -p research_journal_db -e "SHOW TABLES;"

# If empty, run schema
mysql -u root -p research_journal_db < database/schema.sql

# Verify tables created
mysql -u root -p research_journal_db -e "DESCRIBE users;"
```

#### Issue 4: Foreign Key Constraint Error
```
Error: Cannot add or update a child row: a foreign key constraint fails
```

**Solution:**
```sql
-- Check foreign key constraints
SHOW CREATE TABLE papers;

-- If needed, temporarily disable constraints (be careful!)
SET FOREIGN_KEY_CHECKS=0;
-- Run your operation
SET FOREIGN_KEY_CHECKS=1;

-- Or remove and recreate the constraint
ALTER TABLE papers DROP FOREIGN KEY papers_ibfk_1;
```

### Network & Connectivity Issues

#### Issue 1: Localhost Not Resolving
```
Error: Cannot connect to http://localhost:3000
```

**Solution:**
```bash
# Try using 127.0.0.1 instead
http://127.0.0.1:3000

# Or using IP address
ipconfig  # Get your machine IP
http://<your-ip>:3000
```

#### Issue 2: CORS Error in Browser
```
Cross-Origin Request Blocked
```

**Solution:**
1. Add CORS headers to Spring Boot response
2. Use proxy in development (already configured)
3. Check backend is allowing localhost:3000

#### Issue 3: Request Timeout
```
Error: Request timeout
```

**Solution:**
1. Increase timeout in axios:
   ```javascript
   axios.defaults.timeout = 30000; // 30 seconds
   ```

2. Check backend is responding:
   ```bash
   curl http://localhost:8080/api/auth/login
   ```

### Authentication Issues

#### Issue 1: JWT Token Not Stored
```
localStorage.getItem('token') returns null
```

**Solution:**
1. Check if login response includes token:
   ```javascript
   // In browser console
   fetch('/api/auth/login', {...})
       .then(r => r.json())
       .then(d => console.log(d))
   ```

2. Verify token is saved:
   ```javascript
   localStorage.setItem('token', response.data.token);
   ```

#### Issue 2: Token Not Sent in Requests
```
Error: 401 Unauthorized
```

**Solution:**
1. Verify token is in localStorage:
   ```javascript
   const token = localStorage.getItem('token');
   console.log(token);
   ```

2. Add token to headers:
   ```javascript
   axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
   ```

#### Issue 3: Token Expired
```
Error: JWT token has expired
```

**Solution:**
- Increase expiration in `application.properties`:
  ```properties
  jwt.expiration=604800000  # 7 days
  ```
- Or implement token refresh logic

### Performance Issues

#### Issue 1: Slow Backend Response
```
Response takes more than 10 seconds
```

**Solution:**
1. Check database queries:
   ```properties
   logging.level.org.hibernate.SQL=DEBUG
   spring.jpa.properties.hibernate.format_sql=true
   ```

2. Add database indexes
3. Optimize JPA queries
4. Use pagination for large datasets

#### Issue 2: High Memory Usage
```
OutOfMemoryError: Java heap space
```

**Solution:**
```bash
# Increase heap size
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xmx1024m"

# Or set in IDE
# VM options: -Xmx1024m -Xms512m
```

#### Issue 3: Frontend Build is Slow
```
Vite build takes too long
```

**Solution:**
```bash
# Clear cache
rm -r node_modules/.vite

# Update dependencies
npm update

# Use production build
npm run build  # Optimized build
```

## Debugging Tips

### Enable Debug Mode

**Backend:**
```properties
# In application.properties
logging.level.root=DEBUG
logging.level.com.research.journal=DEBUG
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate.SQL=DEBUG
```

**Frontend:**
```javascript
// In browser console
localStorage.debug = 'axios'
```

### Browser DevTools

1. **Console Tab**
   - Check for JavaScript errors
   - Test API calls

2. **Network Tab**
   - Monitor HTTP requests
   - Check response status and headers

3. **Application Tab**
   - Check localStorage for tokens
   - Check cookies

4. **Sources Tab**
   - Set breakpoints
   - Debug JavaScript

### Backend Logs

```bash
# Watch logs in real-time
mvn spring-boot:run | grep -i error

# Save logs to file
mvn spring-boot:run > app.log 2>&1
```

### Database Logs

```sql
-- Enable query logging
SET GLOBAL general_log = 'ON';
SET GLOBAL log_output = 'TABLE';

-- View logs
SELECT * FROM mysql.general_log;

-- Disable when done
SET GLOBAL general_log = 'OFF';
```

## Useful Commands

### Maven
```bash
mvn clean              # Clean build files
mvn install            # Install dependencies
mvn compile            # Compile code
mvn test               # Run tests
mvn package            # Create JAR
mvn -DskipTests        # Skip tests during build
```

### NPM
```bash
npm install            # Install dependencies
npm update             # Update packages
npm run dev            # Development server
npm run build          # Production build
npm run preview        # Preview build
npm cache clean --force # Clear cache
```

### MySQL
```bash
mysql -u root -p       # Connect to MySQL
SHOW DATABASES;        # List databases
USE db_name;           # Select database
SHOW TABLES;           # List tables
DESCRIBE table_name;   # Show table structure
SELECT * FROM table;   # View data
```

### Git
```bash
git init               # Initialize repo
git add .              # Stage changes
git commit -m "msg"    # Commit changes
git push               # Push to remote
git status             # Check status
```

## Getting Help

1. **Check Logs**: Always check the error messages and logs first
2. **Google Error**: Copy the error message and search
3. **Stack Overflow**: Many common issues are answered
4. **Official Docs**: Spring Boot, React, MySQL documentation
5. **Repository Issues**: Create an issue with:
   - Error message
   - Steps to reproduce
   - System information
   - Screenshots/logs

## Checklist Before Reporting Issue

- [x] Restarted the application
- [x] Cleared cache (browser, Maven, npm)
- [x] Checked log files
- [x] Verified all prerequisites installed
- [x] Searched for similar issues
- [x] Tried different port numbers
- [x] Verified database connection
- [x] Checked environment variables

---

**Remember**: Most issues are configuration-related. Double-check your setup first!

Last Updated: November 25, 2025
