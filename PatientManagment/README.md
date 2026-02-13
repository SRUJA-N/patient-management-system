# 🏥 Patient Management System

A comprehensive Spring Boot application for managing patient information in a healthcare setting. This project demonstrates modern Java enterprise development with Spring Boot, following best practices like layered architecture, DTO pattern, and JPA for database operations.

---

## 📋 Table of Contents

1. [Project Overview](#project-overview)
2. [Architecture Explanation](#architecture-explanation)
3. [Technology Stack](#technology-stack)
4. [Project Structure](#project-structure)
5. [How Each Component Connects](#how-each-component-connects)
6. [Why This Architecture?](#why-this-architecture)
7. [Database Schema](#database-schema)
8. [API Endpoints](#api-endpoints)
9. [Getting Started](#getting-started)
10. [Running the Application](#running-the-application)
11. [Testing the API](#testing-the-api)
12. [Configuration Details](#configuration-details)
13. [Sample Data](#sample-data)
14. [Key Concepts Explained](#key-concepts-explained)
15. [Future Enhancements](#future-enhancements)

---

## 📊 Project Overview

This is a **Patient Management System** built with **Spring Boot** that allows healthcare facilities to manage patient records. The application provides a RESTful API to:

- **Retrieve all patients** - Get a list of all registered patients
- **Add new patients** - Register new patients with their details
- **Update patient information** - Modify existing patient records
- **Delete patient records** - Remove patient data from the system
- **Search patients** - Find patients by various criteria

### Current Implementation Status
- ✅ Retrieve all patients (GET `/patient`)
- 📝 Add new patients (POST `/patient`) - Can be added
- 📝 Update patient (PUT `/patient/{id}`) - Can be added
- 📝 Delete patient (DELETE `/patient/{id}`) - Can be added

---

## 🏗️ Architecture Explanation

This project follows the **Layered Architecture** (also known as N-Tier Architecture) which is a fundamental design pattern in enterprise Java applications. Let's break down each layer:

```
┌─────────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                        │
│  (Controller - PatientController.java)                     │
│  - Handles HTTP requests                                     │
│  - Returns HTTP responses                                    │
│  - Input validation (basic)                                  │
└─────────────────────────────┬───────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                     SERVICE LAYER                            │
│  (PatientService.java)                                       │
│  - Business logic                                            │
│  - Data transformation                                       │
│  - Transaction management                                    │
└─────────────────────────────┬───────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      DATA LAYER                              │
│  (Repository - PatientRepository.java)                       │
│  - Database operations                                       │
│  - JPA/Hibernate queries                                     │
│  - Data persistence                                          │
└─────────────────────────────┬───────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                   DATABASE (H2 In-Memory)                    │
│  - Stores patient records                                    │
│  - Auto-generated schema                                     │
└─────────────────────────────────────────────────────────────┘
```

### The Flow of a Request

1. **Client** sends an HTTP request (e.g., GET `/patient`)
2. **Controller** receives the request and delegates to the Service layer
3. **Service** contains business logic and calls the Repository
4. **Repository** interacts with the Database using JPA/Hibernate
5. **Database** returns the data
6. **Repository** passes data back to Service
7. **Service** transforms the data (Entity → DTO) if needed
8. **Controller** returns the HTTP response to the Client

---

## 💻 Technology Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| **Java** | 21 | Programming Language |
| **Spring Boot** | 4.0.2 | Application Framework |
| **Spring Data JPA** | - | ORM / Data Access |
| **Hibernate** | - | ORM Implementation |
| **H2 Database** | - | In-Memory Database |
| **Lombok** | - | Code Generation |
| **Maven** | - | Build Tool |
| **Spring Validation** | - | Input Validation |
| **Spring Web MVC** | - | REST API |

### Dependencies Explained

```
xml
<!-- Spring Boot Starter Data JPA -->
<!-- Provides: Repository support, JPA, Hibernate integration -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- Spring Boot Starter Web MVC -->
<!-- Provides: REST controllers, HTTP request/response handling -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webmvc</artifactId>
</dependency>

<!-- H2 Database -->
<!-- Provides: In-memory database for development/testing -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
</dependency>

<!-- Lombok -->
<!-- Provides: Automatic getters, setters, constructors -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>

<!-- Spring Boot Validation -->
<!-- Provides: Bean Validation annotations -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>

<!-- H2 Console -->
<!-- Provides: Web-based database console -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-h2console</artifactId>
</dependency>
```

---

## 📁 Project Structure

```
PatientManagment/
├── pom.xml                          # Maven configuration & dependencies
├── src/
│   ├── main/
│   │   ├── java/com/patientmanagement/demo/
│   │   │   ├── PatientManagementApplication.java    # Main entry point
│   │   │   ├── controller/
│   │   │   │   └── PatientController.java           # REST Controller
│   │   │   ├── service/
│   │   │   │   └── PatientService.java               # Business logic
│   │   │   ├── repository/
│   │   │   │   └── PatientRepository.java            # Data access
│   │   │   ├── model/
│   │   │   │   └── Patient.java                      # Entity (DB table)
│   │   │   ├── dto/
│   │   │   │   ├── PatientRequestDTO.java            # Request DTO (for POST)
│   │   │   │   └── PatientResponseDTO.java           # Response DTO (for GET)
│   │   │   └── mapper/
│   │   │       └── PatientMapper.java                # Entity ↔ DTO converter
│   │   └── resources/
│   │       ├── application.properties                 # App configuration
│   │       └── data.sql                              # Initial data
│   └── test/
│       └── java/                                      # Unit tests
└── api/
    └── patient-services/
        └── get_patient.http                          # HTTP test file
```

---

## 🔗 How Each Component Connects

### 1. PatientManagementApplication.java (Entry Point)

```
java
@SpringBootApplication
public class PatientManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(PatientManagementApplication.class, args);
    }
}
```

**What it does:**
- Entry point of the Spring Boot application
- `@SpringBootApplication` is a combination of three annotations:
  - `@Configuration` - Marks class as bean definition source
  - `@EnableAutoConfiguration` - Enables Spring Boot's auto-configuration
  - `@ComponentScan` - Enables component scanning

**Why it exists:**
- Bootstraps the entire application
- Starts the embedded server (Tomcat by default)
- Initializes all Spring beans

---

### 2. Patient.java (Model/Entity)

```
java
@Entity
@Data 
@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;
    
    @NotNull
    private String name;
    
    @NotNull
    @Email
    @Column(unique=true)
    private String email;
    
    @NotNull
    private String phoneNumber;
    
    @NotNull
    private int priority;
    
    @NotNull
    private LocalDate dateOfBirth;
    
    @NotNull
    private LocalDate registerDate;
}
```

**What it does:**
- Represents the `patient` database table
- Each field maps to a column in the database
- Uses JPA annotations for ORM mapping

**Annotations explained:**
| Annotation | Purpose |
|------------|---------|
| `@Entity` | Marks class as JPA entity (database table) |
| `@Data` | Lombok - generates getters, setters, toString, equals, hashCode |
| `@NoArgsConstructor` | Lombok - generates no-argument constructor |
| `@AllArgsConstructor` | Lombok - generates all-argument constructor |
| `@Builder` | Lombok - enables builder pattern for object creation |
| `@Id` | Marks field as primary key |
| `@GeneratedValue` | Auto-generates ID (IDENTITY = auto-increment) |
| `@NotNull` | Validation - field cannot be null |
| `@Email` | Validation - must be valid email format |
| `@Column` | Column configuration (unique=true) |

**Why it exists:**
- Defines the data structure
- Maps Java objects to database tables
- Enables ORM (Object-Relational Mapping)

**Connection to other components:**
- Used by `PatientRepository` for database operations
- Used by `PatientMapper` for DTO conversion
- Used by `PatientService` for business logic

---

### 3. PatientRepository.java (Data Access Layer)

```
java
@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {
}
```

**What it does:**
- Interface that extends `JpaRepository`
- Provides CRUD operations out-of-the-box

**Methods inherited from JpaRepository:**
| Method | Description |
|--------|-------------|
| `save(S)` | Save or update an entity |
| `findById(ID)` | Find by primary key |
| `findAll()` | Find all entities |
| `deleteById(ID)` | Delete by primary key |
| `count()` | Count total entities |
| `existsById(ID)` | Check if entity exists |

**Why it exists:**
- Abstracts database operations
- No need to write SQL queries (JPA handles this)
- Repository pattern implementation

**Connection to other components:**
- Used by `PatientService` to access database
- Works with `Patient` entity

---

### 4. PatientService.java (Business Logic Layer)

```
java
@Service
public class PatientService {
    private final PatientRepository patientRepository;
    
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }
    
    public List<PatientResponseDTO> getAllPatient() {
        List<Patient> patients = patientRepository.findAll();
        List<PatientResponseDTO> patientResponseDTOs = 
            patients.stream()
                .map(patient -> PatientMapper.toDto(patient))
                .toList();
        return patientResponseDTOs;
    }
}
```

**What it does:**
- Contains business logic
- Coordinates between Controller and Repository
- Transforms data using Mapper

**Annotations:**
| Annotation | Purpose |
|------------|---------|
| `@Service` | Marks class as Spring bean (service layer) |

**Why it exists:**
- Separates business logic from presentation
- Single responsibility principle
- Easier testing and maintenance

**Connection to other components:**
- Called by `PatientController`
- Uses `PatientRepository` for data access
- Uses `PatientMapper` for transformation

---

### 5. PatientController.java (Presentation Layer)

```
java
@RestController
@RequestMapping("/patient")
public class PatientController {
    private final PatientService patientService;
    
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }
    
    @GetMapping
    public ResponseEntity<List<PatientResponseDTO>> getPatients() {
        List<PatientResponseDTO> patients = patientService.getAllPatient();
        return ResponseEntity.ok().body(patients);
    }
}
```

**What it does:**
- Handles HTTP requests
- Defines REST endpoints
- Returns HTTP responses

**Annotations:**
| Annotation | Purpose |
|------------|---------|
| `@RestController` | Combines @Controller + @ResponseBody (REST API) |
| `@RequestMapping` | Defines base URL path for all endpoints |
| `@GetMapping` | Handles GET HTTP requests |

**Why it exists:**
- Entry point for HTTP requests
- Maps URLs to business logic
- Returns JSON responses

**Connection to other components:**
- Receives HTTP requests from clients
- Calls `PatientService` for business logic

---

### 6. PatientResponseDTO.java (Data Transfer Object)

```
java
@Data
public class PatientResponseDTO {
    private String name;
    private String id;
    private String phoneNumber;
    private String dateOfBirth;
    private String registerDate;
    private int priority;
    private String email;
}
```

**What it does:**
- Data Transfer Object for API responses
- Defines what data is sent to clients

**Why DTOs?**
1. **Security** - Hide internal implementation details
2. **Performance** - Send only required fields
3. **Versioning** - Easy to modify without breaking clients
4. **Decoupling** - Separate API contract from database schema

**Connection to other components:**
- Created by `PatientMapper` from `Patient` entity
- Returned by `PatientController` to clients

---

### 7. PatientMapper.java (Mapper/Converter)

```
java
public class PatientMapper {
    public static PatientResponseDTO toDto(Patient patient) {
        PatientResponseDTO patientDto = new PatientResponseDTO();
        patientDto.setId(patient.getId().toString());
        patientDto.setName(patient.getName());
        patientDto.setEmail(patient.getEmail());
        patientDto.setPhoneNumber(patient.getPhoneNumber());
        patientDto.setDateOfBirth(patient.getDateOfBirth().toString());
        patientDto.setRegisterDate(patient.getRegisterDate().toString());
        patientDto.setPriority(patient.getPriority());
        return patientDto;
    }
}
```

**What it does:**
- Converts `Patient` entity to `PatientResponseDTO`
- Handles data transformation

**Why separate Mapper?**
1. Single responsibility - conversion logic in one place
2. Reusable across the application
3. Easy to test and modify
4. Keeps Service layer clean

**Connection to other components:**
- Used by `PatientService`
- Transforms `Patient` → `PatientResponseDTO`

---

## ❓ Why This Architecture?

### 1. Layered Architecture Benefits

| Benefit | Explanation |
|---------|-------------|
| **Separation of Concerns** | Each layer has a specific responsibility |
| **Maintainability** | Easy to modify one layer without affecting others |
| **Testability** | Each layer can be tested independently |
| **Scalability** | Layers can be scaled independently |
| **Reusability** | Components can be reused across the application |

### 2. Why JPA/Hibernate?

| Benefit | Explanation |
|---------|-------------|
| **Object-Relational Mapping** | Map Java objects to database tables |
| **Database Independence** | Write code once, works with multiple databases |
| **Automatic SQL Generation** | No need to write SQL manually |
| **Transaction Management** | Automatic transaction handling |
| **Caching** | Built-in caching support |

### 3. Why DTO Pattern?

| Benefit | Explanation |
|---------|-------------|
| **Data Hiding** | Hide internal database structure |
| **Network Efficiency** | Send only required data |
| **API Versioning** | Easy to evolve APIs |
| **Type Safety** | Compile-time type checking |

### 4. Why Lombok?

| Benefit | Explanation |
|---------|-------------|
| **Reduced Boilerplate** | No need to write getters/setters |
| **Cleaner Code** | Focus on business logic |
| **Consistency** | Consistent code generation |
| **Readability** | More readable entity classes |

### 5. Why H2 Database?

| Benefit | Explanation |
|---------|-------------|
| **In-Memory** | Fast, no disk I/O |
| **Zero Configuration** | Works out of the box |
| **Easy Testing** | Perfect for development |
| **SQL Compatible** | Standard SQL syntax |

---

## 🗄️ Database Schema

### Patient Table

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| `id` | UUID | PRIMARY KEY, AUTO_INCREMENT | Unique patient ID |
| `name` | VARCHAR(255) | NOT NULL | Patient's full name |
| `email` | VARCHAR(255) | NOT NULL, UNIQUE | Patient's email address |
| `phone_number` | VARCHAR(255) | NOT NULL | Contact number |
| `priority` | INT | NOT NULL | Priority level (1=Highest) |
| `date_of_birth` | DATE | NOT NULL | Date of birth |
| `register_date` | DATE | NOT NULL | Registration date |

### Entity to Table Mapping

```
java
// Java Field          →  Database Column
private UUID id       →  id (PRIMARY KEY)
private String name   →  name
private String email  →  email (UNIQUE)
private String phoneNumber → phone_number
private int priority  →  priority
private LocalDate dateOfBirth → date_of_birth
private LocalDate registerDate → register_date
```

**Note:** Spring Data JPA automatically converts camelCase to snake_case (e.g., `phoneNumber` → `phone_number`).

---

## 🌐 API Endpoints

### Current Endpoints

| Method | Endpoint | Description | Response |
|--------|----------|-------------|----------|
| **GET** | `/patient` | Get all patients | JSON Array of patients |
| **GET** | `/patient/{id}` | Get patient by ID | JSON object of patient |
| **POST** | `/patient` | Create new patient | Created patient object |
| **PUT** | `/patient/{id}` | Update patient | Updated patient object |
| **DELETE** | `/patient/{id}` | Delete patient | 204 No Content |

### Example: Get All Patients

**Request:**
```
GET http://localhost:4000/patient
```

**Response:**
```
json
[
  {
    "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "name": "SRUJAN (EMERGENCY)",
    "email": "srujan.emergency@bit.com",
    "phoneNumber": "9876543210",
    "priority": 1,
    "dateOfBirth": "2004-05-15",
    "registerDate": "2024-01-15"
  },
  {
    "id": "b2c3d4e5-f6a7-8901-bcde-f12345678901",
    "name": "Abhinaya",
    "email": "abhinaya@gmail.com",
    "phoneNumber": "9887766554",
    "priority": 2,
    "dateOfBirth": "2005-01-20",
    "registerDate": "2024-01-15"
  }
]
```

---

## 🚀 Getting Started

### Prerequisites

| Requirement | Version |
|-------------|---------|
| Java JDK | 21 or higher |
| Maven | 3.8+ |
| IDE | IntelliJ IDEA, VS Code, or Eclipse |

### Installation Steps

1. **Clone the repository**
   
```
bash
   git clone <repository-url>
   cd PatientManagment
   
```

2. **Open in IDE**
   - IntelliJ IDEA: File → Open → Select PatientManagment folder
   - VS Code: Open folder → Select PatientManagment

3. **Build the project**
   
```
bash
   ./mvnw clean install
   
```

---

## ▶️ Running the Application

### Option 1: Using Maven

```
bash
cd PatientManagment
./mvnw spring-boot:run
```

### Option 2: Using IDE

1. Open `PatientManagementApplication.java`
2. Right-click → Run
3. Or press `Shift + F10` (IntelliJ)

### Option 3: Running the JAR

```
bash
./mvnw clean package
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

### Expected Output

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v4.0.2)

2024-01-15T10:30:00.000+0000 INFO  - Starting PatientManagementApplication...
2024-01-15T10:30:01.000+0000 INFO  - Started PatientManagementApplication in 2.5 seconds
2024-01-15T10:30:01.000+0000 INFO  - Application is running on port 4000
```

---

## 🧪 Testing the API

### Using curl

```
bash
# Get all patients
curl http://localhost:4000/patient
```

### Using HTTP Client (VS Code)

There's already an HTTP test file at `api/patient-services/get_patient.http`. Update it to:

```
http
GET http://localhost:4000/patient
Accept: application/json
```

Or use the VS Code REST Client extension.

### Using Browser

Navigate to: `http://localhost:4000/patient`

### Using H2 Console

1. Navigate to: `http://localhost:4000/h2-console`
2. Use these settings:
   - **JDBC URL:** `jdbc:h2:mem:testdb`
   - **Username:** `admin`
   - **Password:** `123456789`
3. Click "Connect"

---

## ⚙️ Configuration Details

### application.properties

```
properties
# Application Name
spring.application.name=Patient_Management

# H2 Console Configuration
spring.h2.console.path=/h2-console
spring.h2.console.enabled=true

# Database Configuration
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=admin
spring.datasource.password=123456789

# JPA/Hibernate Configuration
spring.jpa.defer-datasource-initialization=true
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update

# SQL Initialization
spring.sql.init.mode=always

# Server Configuration
server.port=4000
```

### Configuration Explained

| Property | Value | Explanation |
|----------|-------|-------------|
| `spring.application.name` | Patient_Management | Application name |
| `spring.h2.console.path` | /h2-console | H2 web console URL |
| `spring.h2.console.enabled` | true | Enable H2 console |
| `spring.datasource.url` | jdbc:h2:mem:testdb | In-memory database |
| `spring.datasource.username` | admin | Database username |
| `spring.datasource.password` | 123456789 | Database password |
| `spring.jpa.hibernate.ddl-auto` | update | Auto-create/update tables |
| `spring.sql.init.mode` | always | Run data.sql on startup |
| `server.port` | 4000 | Server port number |

---

## 📝 Sample Data

The application initializes with 5 sample patients:

```
sql
-- Patient 1: Emergency case (Priority 1)
INSERT INTO patient (name, email, phone_number, priority, date_of_birth, register_date) 
VALUES ('SRUJAN (EMERGENCY)', 'srujan.emergency@bit.com', '9876543210', 1, '2004-05-15', CURRENT_TIMESTAMP);

-- Patient 2: High priority (Priority 2)
INSERT INTO patient (name, email, phone_number, priority, date_of_birth, register_date) 
VALUES ('Abhinaya', 'abhinaya@gmail.com', '9887766554', 2, '2005-01-20', CURRENT_TIMESTAMP);

-- Patient 3: Normal priority (Priority 3)
INSERT INTO patient (name, email, phone_number, priority, date_of_birth, register_date) 
VALUES ('Darshan', 'darshan.bit@outlook.com', '9112233445', 3, '2004-11-30', CURRENT_TIMESTAMP);

-- Patient 4: Critical case (Priority 1)
INSERT INTO patient (name, email, phone_number, priority, date_of_birth, register_date) 
VALUES ('AYUSH (CRITICAL)', 'ayush.c@gmail.com', '8889990001', 1, '2003-08-12', CURRENT_TIMESTAMP);

-- Patient 5: High priority (Priority 2)
INSERT INTO patient (name, email, phone_number, priority, date_of_birth, register_date) 
VALUES ('Samarth', 'samarth.dev@gmail.com', '7778889992', 2, '2005-03-25', CURRENT_TIMESTAMP);
```

---

## 📚 Key Concepts Explained

### 1. Dependency Injection (DI)

The application uses **Constructor Injection** for dependencies:

```
java
private final PatientService patientService;

public PatientController(PatientService patientService) {
    this.patientService = patientService;
}
```

**Why Constructor Injection?**
- Immutability - dependencies can't be changed after construction
- Required dependencies are clear
- Easy to test with mocks
- Spring guarantees all dependencies are available

### 2. RESTful API

REST (Representational State Transfer) principles used:
- **Resource-based URLs:** `/patient` (not `/getPatients`)
- **HTTP methods:** GET (read), POST (create), PUT (update), DELETE (delete)
- **Status codes:** 200 (OK), 201 (Created), 204 (No Content), 404 (Not Found)
- **Stateless:** Each request contains all information

### 3. JPA Repository Pattern

Spring Data JPA provides repository interfaces:

```
java
public interface PatientRepository extends JpaRepository<Patient, UUID> {
}
```

**Methods available:**
- `findAll()` - Get all records
- `findById(id)` - Get by ID
- `save(entity)` - Save or update
- `deleteById(id)` - Delete by ID

### 4. DTO Pattern

Data Transfer Objects separate API from database:

```
Database (Patient Entity)
    │
    ▼
PatientMapper
    │
    ▼
PatientResponseDTO (API Response)
    │
    ▼
Client
```

### 5. Builder Pattern

Lombok's `@Builder` allows fluent object creation:

```
java
Patient patient = Patient.builder()
    .name("John Doe")
    .email("john@example.com")
    .phoneNumber("1234567890")
    .priority(1)
    .dateOfBirth(LocalDate.of(1990, 1, 1))
    .registerDate(LocalDate.now())
    .build();
```

### 6. Stream API

Used for data transformation:

```
java
List<PatientResponseDTO> patientResponseDTOs = 
    patients.stream()
        .map(patient -> PatientMapper.toDto(patient))
        .toList();
```

---

## 🔮 Future Enhancements

Here are some features that can be added to make this application more complete:

### 1. CRUD Operations
- [ ] POST `/patient` - Create new patient
- [ ] PUT `/patient/{id}` - Update patient
- [ ] DELETE `/patient/{id}` - Delete patient
- [ ] GET `/patient/{id}` - Get patient by ID

### 2. Search & Filtering
- [ ] Search by name
- [ ] Filter by priority
- [ ] Filter by date range
- [ ] Pagination support

### 3. Validation
- [ ] Email format validation
- [ ] Phone number format validation
- [ ] Date validation (DOB not in future)
- [ ] Custom validation messages

### 4. Error Handling
- [ ] Global exception handler
- [ ] Custom error responses
- [ ] Logging
- [ ] API documentation

### 5. Security
- [ ] JWT Authentication
- [ ] Role-based access control
- [ ] Password encryption
- [ ] Rate limiting

### 6. Database
- [ ] Switch to PostgreSQL/MySQL
- [ ] Database migrations (Flyway/Liquibase)
- [ ] Database indexing
- [ ] Caching with Redis

### 7. Testing
- [ ] Unit tests for Service layer
- [ ] Integration tests for Controller
- [ ] Test coverage reports

### 8. Documentation
- [ ] Swagger/OpenAPI documentation
- [ ] Postman collection
- [ ] API versioning

---

## 📊 Flow Diagram

```
                                    HTTP Request
                                          │
                                          ▼
                                   ┌───────────────┐
                                   │   Controller  │
                                   │ PatientControl│
                                   └───────┬───────┘
                                           │
                                           ▼
                                   ┌───────────────┐
                                   │    Service    │
                                   │PatientService │
                                   └───────┬───────┘
                                           │
                    ┌──────────────────────┼──────────────────────┐
                    │                      │                      │
                    ▼                      ▼                      ▼
           ┌───────────────┐     ┌───────────────┐     ┌───────────────┐
           │  Repository   │     │     Mapper    │     │   Validation  │
           │PatientRepo   │     │PatientMapper  │     │   (Spring)    │
           └───────┬───────┘     └───────────────┘     └───────────────┘
                   │
                   ▼
           ┌───────────────┐
           │   Database    │
           │      H2       │
           └───────────────┘
```

---

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

This project is for educational purposes.

---

## 🙏 Acknowledgments

- Spring Boot Documentation
- Hibernate ORM Documentation
- Lombok Project
- H2 Database

---

## 📞 Contact

For questions or suggestions, please open an issue in the repository.

---

**Happy Coding! 🚀**
