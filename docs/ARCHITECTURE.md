# Architecture Documentation

## System Overview

The Research Journal Management System is a three-tier full-stack application:

```
┌──────────────────┐
│  Presentation    │
│  Layer (React)   │
│  localhost:3000  │
└────────┬─────────┘
         │ HTTP/REST
         │
┌────────▼──────────────────────────┐
│  Application Layer (Spring Boot)   │
│  - Controllers                     │
│  - Services                        │
│  - DTOs                           │
│  localhost:8080                    │
└────────┬──────────────────────────┘
         │ JDBC/Hibernate
         │
┌────────▼──────────────────┐
│  Data Layer (MySQL)        │
│  - Tables                  │
│  - Indexes                 │
│  - Constraints            │
└───────────────────────────┘
```

## Backend Architecture

### Technology Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| Framework | Spring Boot | 3.3.5 |
| Java | Java SE | 21 LTS |
| ORM | Spring Data JPA | 3.3.5 |
| Security | Spring Security + JWT | 6.1.x |
| Database | MySQL | 8.0+ |
| Build Tool | Maven | 3.6+ |

### Package Structure

```
com.research.journal/
├── ResearchJournalManagementApplication.java (Entry Point)
│
├── dto/                          (Data Transfer Objects)
│   ├── UserDTO
│   ├── PaperDTO
│   ├── ReviewDTO
│   └── RevisionDTO
│
├── entity/                       (JPA Entities)
│   ├── User
│   ├── Paper
│   ├── Review
│   ├── Revision
│   ├── UserRole (Enum)
│   ├── PaperStatus (Enum)
│   └── ReviewStatus (Enum)
│
├── repository/                   (Data Access Layer)
│   ├── UserRepository
│   ├── PaperRepository
│   ├── ReviewRepository
│   └── RevisionRepository
│
├── service/                      (Business Logic Layer)
│   ├── UserService
│   ├── PaperService
│   ├── ReviewService
│   └── RevisionService
│
└── security/                     (Authentication & Controllers)
    ├── AuthController
    ├── PaperController
    ├── ReviewController
    ├── JwtTokenProvider
    └── SecurityConfig
```

### Data Models

#### User Entity
```
User
├── id: Long (PK)
├── email: String (UNIQUE)
├── password: String (Encrypted)
├── firstName: String
├── lastName: String
├── role: UserRole (AUTHOR, EDITOR, REVIEWER, ADMIN)
├── affiliation: String
├── bio: String
├── isActive: Boolean
├── createdAt: LocalDateTime
└── updatedAt: LocalDateTime
```

#### Paper Entity
```
Paper
├── id: Long (PK)
├── title: String
├── abstractText: String
├── content: String (LONGTEXT)
├── author: User (FK)
├── status: PaperStatus
├── plagiarismScore: Double
├── plagiarismChecked: Boolean
├── reviews: List<Review> (1:N)
├── revisions: List<Revision> (1:N)
├── submittedAt: LocalDateTime
├── acceptedAt: LocalDateTime
├── rejectedAt: LocalDateTime
├── publishedAt: LocalDateTime
├── createdAt: LocalDateTime
└── updatedAt: LocalDateTime
```

#### Review Entity
```
Review
├── id: Long (PK)
├── paper: Paper (FK)
├── reviewer: User (FK)
├── status: ReviewStatus
├── comments: String
├── overallRating: Integer (1-10)
├── technicalQualityRating: Integer (1-10)
├── clarityRating: Integer (1-10)
├── originalityRating: Integer (1-10)
├── significanceRating: Integer (1-10)
├── submittedAt: LocalDateTime
├── createdAt: LocalDateTime
└── updatedAt: LocalDateTime
```

#### Revision Entity
```
Revision
├── id: Long (PK)
├── paper: Paper (FK)
├── revisionNumber: Integer
├── content: String (LONGTEXT)
├── changesSummary: String
├── submittedAt: LocalDateTime
├── createdAt: LocalDateTime
└── updatedAt: LocalDateTime
```

### API Layer

#### REST Endpoints

**Authentication**
```
POST   /api/auth/login           - User login
POST   /api/auth/register        - User registration
POST   /api/auth/logout          - User logout
```

**Papers**
```
GET    /api/papers               - List papers
GET    /api/papers/:id           - Get paper details
POST   /api/papers               - Submit paper
PUT    /api/papers/:id           - Update paper
DELETE /api/papers/:id           - Delete paper
```

**Reviews**
```
GET    /api/reviews              - List reviews
POST   /api/reviews/:paperId     - Submit review
PUT    /api/reviews/:id          - Update review
GET    /api/papers/:id/reviews   - Get paper reviews
```

**Revisions**
```
GET    /api/revisions/:paperId   - Get revisions
POST   /api/revisions/:paperId   - Submit revision
```

**Users**
```
GET    /api/users                - List users (admin)
GET    /api/users/:id            - Get user (admin)
PUT    /api/users/:id            - Update user
GET    /api/users/role/:role     - Get users by role
```

### Security Architecture

```
┌─────────────────────────────────────────┐
│  HTTP Request                           │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│  Authentication Filter                  │
│  - Extract JWT Token                    │
│  - Validate Token                       │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│  Authorization Filter                   │
│  - Check User Roles                     │
│  - Verify Permissions                   │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│  Controller                             │
│  - Process Request                      │
│  - Call Service                         │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│  Service Layer                          │
│  - Business Logic                       │
│  - Access Repository                    │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│  Repository Layer                       │
│  - Database Access                      │
│  - JPA Queries                          │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│  Database                               │
└─────────────────────────────────────────┘
```

## Frontend Architecture

### Technology Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| Framework | React | 18.2 |
| Router | React Router | 6.20 |
| HTTP Client | Axios | 1.6 |
| CSS Framework | Tailwind CSS | 3.4 |
| Build Tool | Vite | 5.0 |
| State Management | Zustand (optional) | 4.4 |

### Component Structure

```
src/
├── pages/                        (Page Components)
│   ├── Login.jsx                (User authentication)
│   ├── Register.jsx             (User registration)
│   ├── Dashboard.jsx            (Main dashboard)
│   ├── SubmitPaper.jsx          (Paper submission)
│   ├── PaperDetails.jsx         (Paper details view)
│   └── ReviewPanel.jsx          (Review interface)
│
├── styles/                       (CSS Files)
│   ├── Auth.css                 (Authentication styles)
│   └── components.css           (Component styles)
│
├── App.jsx                       (Main App Component)
├── App.css                       (Global styles)
├── main.jsx                      (Entry point)
└── index.css                     (Global CSS)
```

### User Flow

```
┌──────────────────────────────────────────┐
│  Login/Register Page                     │
│  - User authentication                   │
│  - JWT token stored in localStorage      │
└────────────────┬─────────────────────────┘
                 │
┌────────────────▼─────────────────────────┐
│  Dashboard Page                          │
│  - View submitted papers                 │
│  - View paper status                     │
│  - Access controls based on role         │
└────────────┬───────────┬──────────────────┘
             │           │
    ┌────────▼────┐  ┌────▼───────────┐
    │ Submit Paper│  │ View Paper     │
    │             │  │                │
    │ - Add title │  │ - View details │
    │ - Add text  │  │ - See reviews  │
    │ - Submit    │  │ - See status   │
    └────┬────────┘  └────┬───────────┘
         │                │
         └────────┬───────┘
                  │
         ┌────────▼───────────┐
         │ Review Panel       │
         │ (for reviewers)    │
         │ - Rate paper       │
         │ - Add comments     │
         │ - Submit review    │
         └────────────────────┘
```

## Database Schema

### Tables

#### users
```sql
CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  email VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  role ENUM('AUTHOR','EDITOR','REVIEWER','ADMIN'),
  affiliation VARCHAR(255),
  bio TEXT,
  is_active BOOLEAN DEFAULT true,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE,
  INDEX idx_email (email),
  INDEX idx_role (role)
);
```

#### papers
```sql
CREATE TABLE papers (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(255) NOT NULL,
  abstract_text LONGTEXT,
  content LONGTEXT,
  author_id BIGINT NOT NULL,
  status ENUM(...),
  plagiarism_score DECIMAL(5,2),
  plagiarism_checked BOOLEAN DEFAULT false,
  submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE,
  FOREIGN KEY (author_id) REFERENCES users(id),
  INDEX idx_author_id (author_id),
  INDEX idx_status (status)
);
```

#### reviews
```sql
CREATE TABLE reviews (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  paper_id BIGINT NOT NULL,
  reviewer_id BIGINT NOT NULL,
  status ENUM('PENDING','SUBMITTED','ACCEPTED','DECLINED'),
  comments LONGTEXT,
  overall_rating INT,
  technical_quality_rating INT,
  clarity_rating INT,
  originality_rating INT,
  significance_rating INT,
  submitted_at TIMESTAMP,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE,
  FOREIGN KEY (paper_id) REFERENCES papers(id),
  FOREIGN KEY (reviewer_id) REFERENCES users(id),
  UNIQUE KEY unique_review (paper_id, reviewer_id)
);
```

#### revisions
```sql
CREATE TABLE revisions (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  paper_id BIGINT NOT NULL,
  revision_number INT NOT NULL,
  content LONGTEXT,
  changes_summary LONGTEXT,
  submitted_at TIMESTAMP NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE,
  FOREIGN KEY (paper_id) REFERENCES papers(id)
);
```

## Deployment Architecture

### Local Development
- Backend: Spring Boot embedded Tomcat on port 8080
- Frontend: Vite dev server on port 3000
- Database: MySQL on localhost:3306

### Production Deployment

```
┌─────────────────────────────────────┐
│  Web Browser (Client)               │
└──────────────┬──────────────────────┘
               │ HTTPS
┌──────────────▼──────────────────────┐
│  Nginx / Apache (Reverse Proxy)     │
│  - Route to Backend                 │
│  - Serve Frontend Files             │
└──────────────┬──────────────────────┘
               │
     ┌─────────┴─────────┐
     │                   │
┌────▼──────┐    ┌────────▼───┐
│  Backend  │    │  Frontend  │
│  JAR App  │    │  Static    │
│  Port 8080│    │  Files     │
└────┬──────┘    └────────┬───┘
     │                    │
     │                    │
┌────▼────────────────────▼────┐
│  MySQL Database              │
│  (Separate server)           │
└──────────────────────────────┘
```

## Security Considerations

### Authentication Flow

```
1. User submits credentials → Login endpoint
2. Backend validates credentials
3. Backend generates JWT token
4. Token sent to frontend
5. Frontend stores token in localStorage
6. All subsequent requests include token in Authorization header
7. Backend validates token on each request
8. Access granted/denied based on role
```

### Role-Based Access Control (RBAC)

```
Admin
├── Full system access
└── Manage users, system settings

Editor
├── Review submissions
├── Assign reviewers
├── Make publication decisions
└── Manage papers

Reviewer
├── Review assigned papers
├── Provide feedback and ratings
└── Submit reviews

Author
├── Submit papers
├── View own submissions
├── Respond to reviews
└── Submit revisions
```

## Performance Optimization

### Backend Optimization
- Database indexing on frequently queried columns
- Lazy loading for JPA relationships
- Query optimization with custom JPQL
- Connection pooling with HikariCP

### Frontend Optimization
- Code splitting with React Router
- Lazy loading components
- Caching API responses
- Vite's fast build time
- Tree-shaking unused code

### Database Optimization
- Proper indexes on foreign keys
- LONGTEXT for large documents
- Partitioning for large tables (future)
- Query result pagination

## Monitoring and Logging

### Backend Logging
```properties
logging.level.root=INFO
logging.level.com.research.journal=DEBUG
logging.pattern.console=%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n
```

### Frontend Logging
- Console logs for debugging
- Network tab for API calls
- Local storage inspection

## Future Enhancements

1. **Microservices Architecture**: Separate services for papers, reviews, users
2. **Message Queue**: RabbitMQ/Kafka for async processing
3. **Caching**: Redis for frequently accessed data
4. **CDN**: Cloud distribution for static assets
5. **Monitoring**: Prometheus + Grafana for metrics
6. **Load Balancing**: Multiple backend instances
7. **Containerization**: Docker + Kubernetes
8. **CI/CD Pipeline**: GitHub Actions or Jenkins

---

Last Updated: November 25, 2025
