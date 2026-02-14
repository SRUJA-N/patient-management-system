# Patient Management API - Flow Visualization

This document provides a detailed flow visualization of each HTTP command in the Patient Management API.

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | http://localhost:4000/patient | Create a new patient |
| GET | http://localhost:4000/patient | Get all patients |
| PUT | http://localhost:4000/patient/{id} | Update a patient |
| DELETE | http://localhost:4000/patient/{id} | Delete a patient |

---

## Flow Diagrams

### 1. CREATE PATIENT (POST /patient)

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                           CREATE PATIENT FLOW                                   │
└─────────────────────────────────────────────────────────────────────────────────┘

HTTP Request (POST /patient)
         │
         ▼
┌─────────────────────────────────────────────────────────────────────────────────┐
│  PatientController                                                             │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │ Method: @PostMapping (assumed - not visible in controller)              │   │
│  │ Class: PatientController.java                                          │   │
│  │ Handler: createPatient(PatientRequestDTO)                             │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────────────────────────────────────────────┐
│  PatientService                                                                │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │ Class: PatientService.java                                             │   │
│  │ Method: createPatient(PatientRequestDTO patientRequestDTO)            │   │
│  │                                                                          │   │
│  │ 1. Check if email exists:                                              │   │
│  │    patientRepository.existsByEmail(email)                              │   │
│  │         │                                                               │   │
│  │         ▼ (if true)                                                    │   │
│  │    ┌─────────────────────┐                                              │   │
│  │    │EmailAlreadyExist    │                                              │   │
│  │    │Exception            │                                              │   │
│  │    └─────────────────────┘                                              │   │
│  │         │                                                               │   │
│  │         ▼                                                               │   │
│  │    GlobalExceptionHandler                                              │   │
│  │    ─────────────────────                                                │   │
│  │    Returns: 400 Bad Request                                            │   │
│  │                                                                          │   │
│  │ 2. If email doesn't exist:                                             │   │
│  │    PatientMapper.toModel(patientRequestDTO)                            │   │
│  │         │                                                               │   │
│  │         ▼                                                               │   │
│  │    patientRepository.save(patient)                                     │   │
│  │         │                                                               │   │
│  │         ▼                                                               │   │
│  │    PatientMapper.toDto(patient)                                        │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────────────────────────────────────────────┐
│  PatientRepository                                                             │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │ Interface: PatientRepository extends JpaRepository<Patient, Long>     │   │
│  │ Methods Used:                                                           │   │
│  │   - existsByEmail(String email)                                         │   │
│  │   - save(Patient patient)                                              │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────────────────────────────────────────────┐
│  Database (H2)                                                                 │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │ Table: patient                                                         │   │
│  │ Columns: id, name, email, phoneNumber, priority, dateOfBirth,         │   │
│  │          registerDate                                                   │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────────┘
         │
         ▼
HTTP Response (200 OK)
{
    "name": "Srujan",
    "id": "1",
    "email": "codewithsrujan@email.com",
    "phoneNumber": "8310081087",
    "priority": "1",
    "dateOfBirth": "2006-07-21",
    "registerDate": "2024-02-02"
}
```

### Classes Involved in CREATE PATIENT:

| Class | Type | Description |
|-------|------|-------------|
| PatientController | Controller | Handles HTTP requests |
| PatientService | Service | Business logic |
| PatientRepository | Repository | Database operations |
| Patient | Model | JPA Entity |
| PatientRequestDTO | DTO | Request data transfer object |
| PatientResponseDTO | DTO | Response data transfer object |
| PatientMapper | Mapper | Converts between DTO and Model |
| EmailAlreadyExistException | Exception | Thrown when email already exists |
| GlobalExceptionHandler | Exception Handler | Handles exceptions |

---

### 2. GET ALL PATIENTS (GET /patient)

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                           GET ALL PATIENTS FLOW                                │
└─────────────────────────────────────────────────────────────────────────────────┘

HTTP Request (GET /patient)
         │
         ▼
┌─────────────────────────────────────────────────────────────────────────────────┐
│  PatientController                                                             │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │ Method: @GetMapping                                                    │   │
│  │ Class: PatientController.java                                          │   │
│  │ Handler: getPatients()                                                │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────────────────────────────────────────────┐
│  PatientService                                                                │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │ Class: PatientService.java                                             │   │
│  │ Method: getAllPatient()                                               │   │
│  │                                                                          │   │
│  │ patientRepository.findAll()                                            │   │
│  │         │                                                               │   │
│  │         ▼                                                               │   │
│  │ Stream.map(patient -> PatientMapper.toDto(patient))                   │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────────────────────────────────────────────┐
│  PatientRepository                                                             │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │ Interface: PatientRepository extends JpaRepository<Patient, Long>     │   │
│  │ Methods Used:                                                           │   │
│  │   - findAll()                                                          │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────────────────────────────────────────────┐
│  Database (H2)                                                                 │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │ Table: patient                                                         │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────────┘
         │
         ▼
HTTP Response (200 OK)
[
    {
        "name": "Patient 1",
        "id": "1",
        "email": "patient1@email.com",
        "phoneNumber": "1234567890",
        "priority": 1,
        "dateOfBirth": "1990-01-01",
        "registerDate": "2024-01-01"
    },
    ...
]
```

### Classes Involved in GET ALL PATIENTS:

| Class | Type | Description |
|-------|------|-------------|
| PatientController | Controller | Handles HTTP requests |
| PatientService | Service | Business logic |
| PatientRepository | Repository | Database operations |
| Patient | Model | JPA Entity |
| PatientResponseDTO | DTO | Response data transfer object |
| PatientMapper | Mapper | Converts between Model and DTO |

---

### 3. UPDATE PATIENT (PUT /patient/{id})

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                           UPDATE PATIENT FLOW                                   │
└─────────────────────────────────────────────────────────────────────────────────┘

HTTP Request (PUT /patient/{id})
         │
         ▼
┌─────────────────────────────────────────────────────────────────────────────────┐
│  PatientController                                                             │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │ Method: @PutMapping("/{id}")                                          │   │
│  │ Class: PatientController.java                                          │   │
│  │ Handler: updatePatient(@PathVariable Long id,                         │   │
│  │                       @RequestBody PatientRequestDTO)                 │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────────────────────────────────────────────┐
│  PatientService                                                                │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │ Class: PatientService.java                                             │   │
│  │ Method: updatePatient(Long id, PatientRequestDTO)                     │   │
│  │                                                                          │   │
│  │ 1. Find patient by ID:                                                 │   │
│  │    patientRepository.findById(id)                                      │   │
│  │         │                                                               │   │
│  │         ▼ (if not found)                                               │   │
│  │    ┌─────────────────────┐                                              │   │
│  │    │PatientNotFound       │                                              │   │
│  │    │Exception            │                                              │   │
│  │    └─────────────────────┘                                              │   │
│  │         │                                                               │   │
│  │         ▼                                                               │   │
│  │    GlobalExceptionHandler                                              │   │
│  │    ─────────────────────                                                │   │
│  │    Returns: 400 Bad Request                                            │   │
│  │                                                                          │   │
│  │ 2. If patient found, check email:                                       │   │
│  │    patientRepository.existsByEmail(email)                              │   │
│  │         │                                                               │   │
│  │         ▼ (if true)                                                    │   │
│  │    ┌─────────────────────┐                                              │   │
│  │    │EmailAlreadyExist    │                                              │   │
│  │    │Exception            │                                              │   │
│  │    └─────────────────────┘                                              │   │
│  │         │                                                               │   │
│  │         ▼                                                               │   │
│  │    GlobalExceptionHandler                                              │   │
│  │    ─────────────────────                                                │   │
│  │    Returns: 400 Bad Request                                            │   │
│  │                                                                          │   │
│  │ 3. If email is unique:                                                  │   │
│  │    patient.setName(email)                                              │   │
│  │    patient.setEmail(email)                                             │   │
│  │    patientRepository.save(patient)                                      │   │
│  │         │                                                               │   │
│  │         ▼                                                               │   │
│  │    PatientMapper.toDto(patient)                                        │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────────────────────────────────────────────┐
│  PatientRepository                                                             │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │ Interface: PatientRepository extends JpaRepository<Patient, Long>     │   │
│  │ Methods Used:                                                           │   │
│  │   - findById(Long id)                                                  │   │
│  │   - existsByEmail(String email)                                        │   │
│  │   - save(Patient patient)                                              │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────────────────────────────────────────────┐
│  Database (H2)                                                                 │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │ Table: patient                                                         │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────────┘
         │
         ▼
HTTP Response (200 OK)
{
    "name": "Updated Name",
    "id": "1",
    "email": "updated@email.com",
    "phoneNumber": "8310081087",
    "priority": "2",
    "dateOfBirth": "2006-07-21",
    "registerDate": "2024-02-02"
}
```

### Classes Involved in UPDATE PATIENT:

| Class | Type | Description |
|-------|------|-------------|
| PatientController | Controller | Handles HTTP requests |
| PatientService | Service | Business logic |
| PatientRepository | Repository | Database operations |
| Patient | Model | JPA Entity |
| PatientRequestDTO | DTO | Request data transfer object |
| PatientResponseDTO | DTO | Response data transfer object |
| PatientMapper | Mapper | Converts between DTO and Model |
| PatientNotFoundException | Exception | Thrown when patient not found |
| EmailAlreadyExistException | Exception | Thrown when email already exists |
| GlobalExceptionHandler | Exception Handler | Handles exceptions |

---

## Exception Handling Flow

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                          EXCEPTION HANDLING FLOW                                │
└─────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────┐
│  Exceptions                                                                    │
│  ┌─────────────────────┐  ┌─────────────────────┐                             │
│  │ PatientNotFound     │  │ EmailAlreadyExist   │                             │
│  │ Exception           │  │ Exception           │                             │
│  └─────────────────────┘  └─────────────────────┘                             │
│           │                         │                                          │
│           └───────────┬─────────────┘                                          │
│                       ▼                                                        │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │ GlobalExceptionHandler (@RestControllerAdvice)                        │   │
│  │                                                                          │   │
│  │ @ExceptionHandler(PatientNotFoundException.class)                     │   │
│  │   → Returns: 400 Bad Request                                           │   │
│  │   → Body: {"message": "Patient not found"}                             │   │
│  │                                                                          │   │
│  │ @ExceptionHandler(EmailAlreadyExistException.class)                    │   │
│  │   → Returns: 400 Bad Request                                           │   │
│  │   → Body: {"message": "Email address already exists"}                  │   │
│  │                                                                          │   │
│  │ @ExceptionHandler(MethodArgumentNotValidException.class)               │   │
│  │   → Returns: 400 Bad Request                                           │   │
│  │   → Body: {"field": "error message", ...}                              │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## Complete Class Diagram

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                           CLASS DIAGRAM                                         │
└─────────────────────────────────────────────────────────────────────────────────┘

                            ┌─────────────────────┐
                            │   HTTP Request      │
                            │   (REST Client)     │
                            └──────────┬──────────┘
                                       │
                                       ▼
                            ┌─────────────────────┐
                            │  PatientController │◄────────┐
                            │  (@RestController) │         │
                            ├─────────────────────┤         │
                            │ + getPatients()    │         │
                            │ + updatePatient()  │         │
                            └──────────┬──────────┘         │
                                       │                    │
                                       ▼                    │
                            ┌─────────────────────┐         │
                            │   PatientService    │         │
                            │   (@Service)        │         │
                            ├─────────────────────┤         │
                            │ + getAllPatient()  │         │
                            │ + createPatient()  │         │
                            │ + updatePatient()  │         │
                            └──────────┬──────────┘         │
                                       │                    │
                    ┌──────────────────┼──────────────────┐ │
                    │                  ▼                  │ │
                    │   ┌─────────────────────────────┐  │ │
                    │   │     PatientRepository      │  │ │
                    │   │     (JpaRepository)        │  │ │
                    │   ├─────────────────────────────┤  │ │
                    │   │ + findAll()                │  │ │
                    │   │ + findById()               │  │ │
                    │   │ + save()                   │  │ │
                    │   │ + existsByEmail()          │  │ │
                    │   └──────────────┬──────────────┘  │ │
                    │                  │                  │ │
                    ▼                  ▼                  ▼ │
          ┌─────────────────┐  ┌─────────────────┐  ┌──────────────┐
          │ PatientNotFound │  │EmailAlready    │  │  Patient     │
          │ Exception       │  │ExistException  │  │  (Entity)     │
          └─────────────────┘  └─────────────────┘  └──────────────┘
                    │                  │                  │
                    │                  │                  │
                    └────────┬─────────┴──────────────────┘
                             ▼
                  ┌─────────────────────┐
                  │GlobalExceptionHandler│
                  │(@RestControllerAdvice)
                  └─────────────────────┘

    ┌───────────────────────────────────────────────────────────────────────────┐
    │                           DATA FLOW                                       │
    └───────────────────────────────────────────────────────────────────────────┘

    PatientRequestDTO ──────► PatientMapper.toModel() ──────► Patient (Entity)
                                                                      │
                                                                      ▼
    PatientResponseDTO ◄───── PatientMapper.toDto() ◄────── Patient (Entity)
```

---

## Technology Stack

| Component | Technology |
|-----------|------------|
| Framework | Spring Boot |
| Database | H2 (In-Memory) |
| ORM | Spring Data JPA / Hibernate |
| Validation | Jakarta Validation |
| Build Tool | Maven |
| Server Port | 4000 |

---

## Summary

This API follows the standard Spring Boot MVC architecture:

1. **Controller Layer** - Handles HTTP requests and responses
2. **Service Layer** - Contains business logic
3. **Repository Layer** - Manages database operations
4. **Model/Entity** - Represents database tables
5. **DTOs** - Data Transfer Objects for request/response
6. **Mapper** - Converts between DTOs and Entities
7. **Exception Handling** - Global exception handling via `@RestControllerAdvice`

Each HTTP request flows through these layers, and appropriate responses are returned to the client.
