# Research Journal Management System

A full-stack web application for managing research journal submissions, peer reviews, and publication workflows. Built with React, Spring Boot, and MySQL.

## Project Structure

```
ResearchJournalmanagement/
├── backend/              # Spring Boot backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/research/journal/
│   │   │   │   ├── ResearchJournalManagementApplication.java
│   │   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   │   ├── entity/              # JPA Entities
│   │   │   │   ├── repository/          # Data Access Layer
│   │   │   │   ├── service/             # Business Logic
│   │   │   │   └── security/            # Authentication & Controllers
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   └── pom.xml                          # Maven Configuration
├── frontend/            # React frontend
│   ├── src/
│   │   ├── pages/                       # React Pages
│   │   ├── styles/                      # CSS Stylesheets
│   │   ├── App.jsx
│   │   └── main.jsx
│   ├── package.json
│   ├── vite.config.js
│   └── index.html
├── database/            # Database files
│   └── schema.sql       # MySQL schema
└── README.md
```

## Features

### Core Functionality
- **User Management**: Author, Editor, Reviewer, and Admin roles
- **Paper Submission**: Authors can submit research papers with abstract and content
- **Plagiarism Detection**: Built-in plagiarism checking before review
- **Peer Review System**: Multiple reviewers can review papers with detailed ratings
- **Revision Tracking**: Track paper revisions and author responses
- **Status Management**: Track paper status from submission to publication
- **Publishing Workflow**: Accept, reject, or request revisions on papers

### User Roles
- **Authors**: Submit papers, respond to reviews, track submission status
- **Reviewers**: Review assigned papers, provide ratings and feedback
- **Editors**: Manage the review process, make final decisions
- **Admins**: Manage users and system settings

## Technology Stack

### Backend
- **Java 21** LTS (Latest Long-Term Support)
- **Spring Boot 3.3.5**
- **Spring Data JPA** - ORM framework
- **Spring Security** - Authentication & Authorization
- **JWT** - Token-based authentication
- **MySQL 8.0+** - Relational database
- **Maven** - Build tool
- **Lombok** - Reduce boilerplate code
- **Springdoc OpenAPI** - API documentation (Swagger)

### Frontend
- **React 18.2** - UI library
- **React Router 6** - Client-side routing
- **Axios** - HTTP client
- **Vite** - Build tool
- **Tailwind CSS** - CSS framework
- **Zustand** - State management (optional)

### Database
- **MySQL 8.0+**
- Relational schema with proper indexing
- Support for LONGTEXT for large documents

## Prerequisites

- Java 21 JDK
- Node.js 18+ and npm
- MySQL 8.0+
- Maven 3.6+

## Installation & Setup

### 1. Database Setup

```bash
# Connect to MySQL
mysql -u root -p

# Create database
CREATE DATABASE research_journal_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# Import schema
mysql -u root -p research_journal_db < database/schema.sql
```

Update `application.properties` with your MySQL credentials:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/research_journal_db
spring.datasource.username=root
spring.datasource.password=your_password
```

### 2. Backend Setup

```bash
cd backend

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

API Documentation: `http://localhost:8080/api/swagger-ui.html`

### 3. Frontend Setup

```bash
cd frontend

# Install dependencies
npm install

# Run development server
npm run dev
```

The frontend will be available on `http://localhost:3000`

## API Endpoints

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration

### Papers
- `GET /api/papers` - Get all papers (or user's papers)
- `GET /api/papers/{id}` - Get paper details
- `POST /api/papers` - Submit new paper
- `PUT /api/papers/{id}` - Update paper
- `DELETE /api/papers/{id}` - Delete paper

### Reviews
- `GET /api/reviews` - Get all reviews
- `POST /api/reviews/{paperId}` - Submit review for paper
- `PUT /api/reviews/{id}` - Update review

### Revisions
- `GET /api/revisions/{paperId}` - Get paper revisions
- `POST /api/revisions/{paperId}` - Submit revision

### Users
- `GET /api/users` - Get all users (admin only)
- `GET /api/users/{id}` - Get user details
- `PUT /api/users/{id}` - Update user

## Configuration

### Environment Variables

**Backend** (`application.properties`):
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/research_journal_db
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
jwt.secret=your_jwt_secret_key
jwt.expiration=86400000
server.port=8080
```

**Frontend** (Vite proxy in `vite.config.js`):
```javascript
proxy: {
  '/api': {
    target: 'http://localhost:8080',
    changeOrigin: true
  }
}
```

## Building for Production

### Backend
```bash
cd backend
mvn clean package
java -jar target/research-journal-management-1.0.0.jar
```

### Frontend
```bash
cd frontend
npm run build
# Serve the dist folder
npm install -g serve
serve -s dist
```

## Project Workflow

1. **Author Submission** → Author submits paper with title, abstract, and content
2. **Plagiarism Check** → System automatically checks plagiarism score
3. **Assign Reviewers** → Editor assigns 2-3 reviewers to the paper
4. **Peer Review** → Reviewers provide ratings and feedback
5. **Decision** → Editor makes decision (Accept/Reject/Request Revision)
6. **Revision** → If required, author submits revisions
7. **Publication** → Accepted papers are published and archived

## Key Classes

### Entities
- `User` - User account information
- `Paper` - Submitted research paper
- `Review` - Peer review details
- `Revision` - Paper revision history

### DTOs
- `UserDTO` - User data transfer
- `PaperDTO` - Paper data transfer
- `ReviewDTO` - Review data transfer
- `RevisionDTO` - Revision data transfer

### Repositories
- `UserRepository` - User data access
- `PaperRepository` - Paper data access
- `ReviewRepository` - Review data access
- `RevisionRepository` - Revision data access

## Security Features

- JWT-based authentication
- Password encryption with BCrypt
- Role-based access control (RBAC)
- CORS configuration for frontend
- Input validation and sanitization
- SQL injection prevention via JPA

## Performance Optimizations

- Database indexing on frequently queried columns
- Lazy loading for JPA relationships
- Query optimization with custom JPQL queries
- Frontend lazy loading with React Router
- Vite for fast build times

## Future Enhancements

- [ ] Email notifications for paper status updates
- [ ] Advanced plagiarism detection integration
- [ ] Paper categorization and tagging
- [ ] Search and filtering capabilities
- [ ] Admin dashboard with analytics
- [ ] PDF export functionality
- [ ] Two-factor authentication
- [ ] Rate limiting and throttling
- [ ] Automated backup system
- [ ] Docker containerization

## Troubleshooting

### Backend Issues
- **Port 8080 already in use**: Change `server.port` in `application.properties`
- **Database connection failed**: Verify MySQL is running and credentials are correct
- **Dependencies not found**: Run `mvn clean install -U`

### Frontend Issues
- **Port 3000 already in use**: Run `npm run dev -- --port 3001`
- **API connection failed**: Verify backend is running on port 8080
- **Module not found**: Run `npm install` again

## Contributing

1. Create a feature branch (`git checkout -b feature/AmazingFeature`)
2. Commit changes (`git commit -m 'Add AmazingFeature'`)
3. Push to branch (`git push origin feature/AmazingFeature`)
4. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For issues and questions, please create an issue in the repository.

## Author

Pratham Heble

## Last Updated

November 25, 2025
"# research-journal-management" 
