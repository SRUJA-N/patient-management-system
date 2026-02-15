# Patient Management API - Data Flow Documentation

This document provides a detailed data flow visualization of each HTTP operation in the Patient Management API. It traces how data moves through the entire system from request to response.

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | http://localhost:4000/patient | Get all patients |
| GET | http://localhost:4000/patient/{id} | Get patient by ID |
| POST | http://localhost:4000/patient | Create a new patient |
| PUT | http://localhost:4000/patient/{id} | Update a patient |
| DELETE | http://localhost:4000/patient/{id} | Delete a patient |

---

## Data Flow Architecture

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                        DATA FLOW ARCHITECTURE                                   │
└─────────────────────────────────────────────────────────────────────────────────┘

        ┌─────────────┐
        │   Client    │
        │ (HTTP Req)  │
        └──────┬──────┘
               │
               ▼
        ┌─────────────┐
        │ Controller │◄────── @RestController, @RequestMapping("/patient")
        └──────┬──────┘
               │ receives request, validates input, delegates to service
               ▼
        ┌─────────────┐
        │  Service   │◄────── @Service, contains business logic
        └──────┬──────┘
               │ orchestrates operations, validates business rules
       ┌────────┼────────┐
       ▼        ▼        ▼
   ┌───────┐ ┌───────┐ ┌────────┐
   │Mapper │ │Repository│ │Exceptions│
   └───┬───┘ └───┬───┘ └────┬────┘
       │         │          │
       ▼         ▼          ▼
   ┌─────────────────────────────┐
   │      Database (H2)         │
   └─────────────────────────────┘
```

---

## Flow Diagrams

### 1. CREATE PATIENT (POST /patient)

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                           CREATE PATIENT FLOW                                   │
└─────────────────────────────────────────────────────────────────────────────────┘

HTTP Request (POST /patient)
Body: { "name": "John", "email": "john@email.com", "phoneNumber": "1234567890", 
        "priority": 1, "dateOfBirth": "1990-01-01", "registerDate": "2024-01-01" }
         │
         ▼
┌─────────────────────────────────────────────────────────────────────────────────┐
│  PatientController                                                             │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │ @PostMapping                                                            │   │
│  │ createPatient(@Validated @RequestBody PatientRequestDTO)              │   │
│  │                                                                          │   │
│  │ Input: PatientRequestDTO                                               │   │
│  │   - name (required)                                                    │   │
│  │   - email (required, valid email format)                               │   │
│  │   - phoneNumber (required)                                             │   │
│  │   - priority (required, 1-3)                                           │   │
│  │   - dateOfBirth (required)                                             │   │
│  │   - registerDate (required, validated by CreatePatientValidationgroup)│   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
│  Calls: patientService.createPatient(patientRequestDTO)                        │
└─────────────────────────────────────────────────────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────────────────────────────────────────────┐
│  PatientService                                                                │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │ createPatient(PatientRequestDTO patientRequestDTO)                     │   │
│  │                                                                          │   │
│  │ Step 1: Email Validation                                               │   │
│  │   ├─ patientRepository.existsByEmail(email)                            │   │
│  │   │                                                                    │   │
│  │   │  if TRUE → throw EmailAlreadyExistException                       │   │
│  │   │        │                                                            │   │
│  │   │        ▼                                                            │   │
│  │   │  ┌──────────────────────────────────────┐                         │   │
│  │   │  │ GlobalExceptionHandler               │                         │   │
│  │   │  │ @ExceptionHandler                   │                         │   │
│  │   │  │ Returns: 400 Bad Request            │                         │   │
│  │   │  │ Body: {"message": "Email already exists"}                    │   │
│  │   │  └──────────────────────────────────────┘                         │   │
│  │   │                                                                    │   │
│  │   └─ if FALSE → continue                                              │   │
│  │                                                                        │   │
│  │ Step 2: Convert DTO to Entity                                         │   │
│  │   └─ PatientMapper.toModel(patientRequestDTO) → Patient               │   │
│  │                                                                        │   │
│  │ Step 3: Save to Database                                              │   │
│  │   └─ patientRepository.save(patient) → returns saved Patient         │   │
│  │                                                                        │   │
│  │ Step 4: Convert Entity to Response DTO                                 │   │
│  │   └─ PatientMapper.toDto(patient) → PatientResponseDTO               │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────────────────────────────────────────────┐
│  PatientRepository (Data Access Layer)                                          │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │ extends JpaRepository<Patient, Long>                                   │   │
│  │                                                                          │   │
│  │ Methods:                                                               │   │
│  │   - existsByEmail(String email) → boolean                             │   │
│  │   - save(Patient patient) → Patient (saved entity with ID)            │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────────────────────────────────────────────┐
│  Database (H2 In-Memory)                                                       │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │ Table: patient                                                         │   │
│  │ ───────────────────────────────────────────────────────────────────   │   │
│  │ id | name  | email           | phone   | priority | dob      | regDate │   │
│  │ ───────────────────────────────────────────────────────────────────   │   │
│  │ 1  | John  | john@email.com | 1234567 | 1         | 1990-01- | 2024-01-│   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────────┘
         │
         ▼
HTTP Response (201 CREATED)
{
    "name": "John",
    "id": "1",
    "email": "john@email.com",
    "phoneNumber": "1234567890",
    "priority": 1,
    "dateOfBirth": "1990-01-01",
    "registerDate": "2024-01-01"
}
```

### Classes Involved in CREATE PATIENT:

| Class | Type | Role |
|-------|------|------|
| PatientController | Controller | Handles HTTP POST request, validates input |
| PatientService | Service | Business logic, email validation |
| PatientRepository | Repository | Database operations |
| Patient | Entity | JPA entity mapped to database table |
| PatientRequestDTO | DTO | Request data transfer object |
| PatientResponseDTO | DTO | Response data transfer object |
| PatientMapper | Mapper | Converts PatientRequestDTO ↔ Patient ↔ PatientResponseDTO |
| EmailAlreadyExistException | Exception | Thrown when email already exists |
| GlobalExceptionHandler | Exception Handler | Catches and handles exceptions |
| CreatePatientValidationgroup | Validation Group | Custom validation for create operations |

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

### 4. GET PATIENT BY ID (GET /patient/{id})

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                        GET PATIENT BY ID FLOW                                   │
└─────────────────────────────────────────────────────────────────────────────────┘

HTTP Request (GET /patient/{id})
         │
         ▼
┌─────────────────────────────────────────────────────────────────────────────────┐
│  PatientController                                                             │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │ Method: @GetMapping("/{id}")                                           │   │
│  │ Class: PatientController.java                                          │   │
│  │ Handler: getPatientById(@PathVariable Long id)                        │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────────────────────────────────────────────┐
│  PatientService                                                                │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │ Class: PatientService.java                                             │   │
│  │ Method: getPatientById(Long id)                                       │   │
│  │                                                                          │   │
│  │ Step 1: Find patient by ID                                            │   │
│  │   ├─ patientRepository.findById(id)                                   │   │
│  │   │                                                                    │   │
│  │   │  if NOT FOUND → throw PatientNotFoundException                  │   │
│  │   │        │                                                            │   │
│  │   │        ▼                                                            │   │
│  │   │  ┌──────────────────────────────────────┐                         │   │
│  │   │  │ GlobalExceptionHandler               │                         │   │
│  │   │  │ @ExceptionHandler                   │                         │   │
│  │   │  │ Returns: 404 Not Found              │                         │   │
│  │   │  │ Body: {"message": "Patient not found"}                       │   │
│  │   │  └──────────────────────────────────────┘                         │   │
│  │   │                                                                    │   │
│  │   └─ if FOUND → continue                                              │   │
│  │                                                                        │   │
│  │ Step 2: Convert Entity to Response DTO                                 │   │
│  │   └─ PatientMapper.toDto(patient) → PatientResponseDTO               │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────────────────────────────────────────────┐
│  PatientRepository                                                             │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │ Interface: PatientRepository extends JpaRepository<Patient, Long>     │   │
│  │ Methods Used:                                                           │   │
│  │   - findById(Long id) → Optional<Patient>                             │   │
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
    "name": "John",
    "id": "1",
    "email": "john@email.com",
    "phoneNumber": "1234567890",
    "priority": 1,
    "dateOfBirth": "1990-01-01",
    "registerDate": "2024-01-01"
}
```

### Classes Involved in GET PATIENT BY ID:

| Class | Type | Description |
|-------|------|-------------|
| PatientController | Controller | Handles HTTP requests |
| PatientService | Service | Business logic |
| PatientRepository | Repository | Database operations |
| Patient | Model | JPA Entity |
| PatientResponseDTO | DTO | Response data transfer object |
| PatientMapper | Mapper | Converts between Model and DTO |
| PatientNotFoundException | Exception | Thrown when patient not found |
| GlobalExceptionHandler | Exception Handler | Handles exceptions |

---

### 5. DELETE PATIENT (DELETE /patient/{id})

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                           DELETE PATIENT FLOW                                   │
└─────────────────────────────────────────────────────────────────────────────────┘

HTTP Request (DELETE /patient/{id})
         │
         ▼
┌─────────────────────────────────────────────────────────────────────────────────┐
│  PatientController                                                             │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │ Method: @DeleteMapping("/{id}")                                        │   │
│  │ Class: PatientController.java                                          │   │
│  │ Handler: deletePatient(@PathVariable Long id)                         │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────────────────────────────────────────────┐
│  PatientService                                                                │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │ Class: PatientService.java                                             │   │
│  │ Method: deletePatient(Long id)                                        │   │
│  │                                                                          │   │
│  │ Step 1: Check if patient exists                                       │   │
│  │   ├─ patientRepository.existsById(id)                                │   │
│  │   │                                                                    │   │
│  │   │  if FALSE → throw PatientNotFoundException                        │   │
│  │   │        │                                                            │   │
│  │   │        ▼                                                            │   │
│  │   │  ┌──────────────────────────────────────┐                         │   │
│  │   │  │ GlobalExceptionHandler               │                         │   │
│  │   │  │ @ExceptionHandler                   │                         │   │
│  │   │  │ Returns: 404 Not Found              │                         │   │
│  │   │  │ Body: {"message": "Patient not found"}                       │   │
│  │   │  └──────────────────────────────────────┘                         │   │
│  │   │                                                                    │   │
│  │   └─ if TRUE → continue                                               │   │
│  │                                                                        │   │
│  │ Step 2: Delete patient                                                │   │
│  │   └─ patientRepository.deleteById(id)                                 │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────────────────────────────────────────────┐
│  PatientRepository                                                             │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │ Interface: PatientRepository extends JpaRepository<Patient, Long>     │   │
│  │ Methods Used:                                                           │   │
│  │   - existsById(Long id) → boolean                                     │   │
│  │   - deleteById(Long id)                                               │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────────────────────────────────────────────┐
│  Database (H2)                                                                 │
│  ┌─────────────────────────────────────────────────────────────────────────┐   │
│  │ Table: patient (record deleted)                                        │   │
│  └─────────────────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────────────────┘
         │
         ▼
HTTP Response (204 NO CONTENT)
```

### Classes Involved in DELETE PATIENT:

| Class | Type | Description |
|-------|------|-------------|
| PatientController | Controller | Handles HTTP requests |
| PatientService | Service | Business logic |
| PatientRepository | Repository | Database operations |
| Patient | Model | JPA Entity |
| PatientNotFoundException | Exception | Thrown when patient not found |
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
