# HDFC Learning Academy

A Spring Boot REST API application for managing training courses and employee enrollments at HDFC Learning Academy.

## Technologies Used

* Java 17
* Spring Boot
* Spring Web
* Spring Validation
* Maven
* Java Streams
* Postman
* GitHub

## Project Overview

The application manages:

* Courses
* Employee enrollments
* Enrollment status
* Course capacity
* Course and enrollment searches
* Analytics

The application uses **in-memory storage** with Java `Map` collections. No database, JPA, or JDBC is used.

```text
Map<Integer, Course>
Map<Integer, Enrollment>
```

> Note: Since data is stored in memory, all data will be lost when the application is restarted.

## Project Structure

```text
com.hdfc
│
├── controller
│   ├── CourseController.java
│   ├── EnrollmentController.java
│   └── AnalyticsController.java
│
├── service
│   ├── CourseService.java
│   ├── CourseServiceImpl.java
│   ├── EnrollmentService.java
│   └── EnrollmentServiceImpl.java
│
├── repository
│   ├── CourseRepository.java
│   └── EnrollmentRepository.java
│
├── dto
│   ├── CourseRequestDto.java
│   ├── CourseResponseDto.java
│   ├── EnrollmentRequestDto.java
│   └── EnrollmentResponseDto.java
│
├── entity
│   ├── Course.java
│   └── Enrollment.java
│
├── mapper
│   ├── CourseMapper.java
│   └── EnrollmentMapper.java
│
├── exception
│   ├── CourseNotFoundException.java
│   ├── EnrollmentNotFoundException.java
│   ├── DuplicateEnrollmentException.java
│   ├── CourseCapacityFullException.java
│   ├── ErrorResponse.java
│   └── GlobalExceptionHandler.java
│
└── CourseEnrollmentApplication.java
```

## Course APIs

| Method | Endpoint                         | Description                            |
| ------ | -------------------------------- | -------------------------------------- |
| POST   | `/courses`                       | Create a course                        |
| GET    | `/courses`                       | Get all courses                        |
| GET    | `/courses/{id}`                  | Get course by ID                       |
| PUT    | `/courses/{id}`                  | Update course                          |
| DELETE | `/courses/{id}`                  | Delete course                          |
| GET    | `/courses/trainer/{trainerName}` | Get courses by trainer                 |
| GET    | `/courses/fees/{amount}`         | Get courses with fees less than amount |

## Enrollment APIs

| Method | Endpoint                             | Description                 |
| ------ | ------------------------------------ | --------------------------- |
| POST   | `/enrollments`                       | Enroll an employee          |
| GET    | `/enrollments`                       | Get all enrollments         |
| GET    | `/enrollments/{id}`                  | Get enrollment by ID        |
| PUT    | `/enrollments/{id}/cancel`           | Cancel enrollment           |
| PUT    | `/enrollments/{id}/complete`         | Complete enrollment         |
| GET    | `/enrollments/status/{status}`       | Get enrollments by status   |
| GET    | `/enrollments/employee/{employeeId}` | Get enrollments by employee |

## Analytics APIs

| Method | Endpoint                         | Description                     |
| ------ | -------------------------------- | ------------------------------- |
| GET    | `/analytics/course-count`        | Get total number of courses     |
| GET    | `/analytics/enrollment-count`    | Get total number of enrollments |
| GET    | `/analytics/most-popular-course` | Get the most popular course     |

Java Streams are used for the analytics functionality as required by the assignment.

## Business Rules

1. An employee cannot enroll in a course that does not exist.
2. Course capacity cannot be exceeded.
3. An employee cannot enroll twice in the same course.
4. Course fees must be greater than zero.
5. Course duration must be greater than zero.
6. Enrollment status can be:

   * `ENROLLED`
   * `COMPLETED`
   * `CANCELLED`

## Validation

Bean Validation is implemented using:

```text
@NotBlank
@NotNull
@Positive
@Size
@Valid
```

## Exception Handling

Global exception handling is implemented using:

```java
@RestControllerAdvice
```

Custom exceptions include:

* `CourseNotFoundException`
* `EnrollmentNotFoundException`
* `DuplicateEnrollmentException`
* `CourseCapacityFullException`

## Example: Create Course

### Request

```http
POST http://localhost:8080/courses
```

```json
{
    "courseName": "Java Backend",
    "trainerName": "Rahul Sharma",
    "durationInDays": 30,
    "maxCapacity": 3,
    "fees": 15000
}
```

## Example: Enroll Employee

### Request

```http
POST http://localhost:8080/enrollments
```

```json
{
    "employeeId": 101,
    "employeeName": "Ratan",
    "courseId": 1
}
```

## How to Run

### Using Spring Tools for Eclipse

1. Import the project into Spring Tools for Eclipse.
2. Update Maven dependencies.
3. Run `CourseEnrollmentApplication.java` as a Spring Boot Application.
4. The application starts on port `8080`.

### Using Maven

```bash
./mvnw clean install
```

Run the application:

```bash
./mvnw spring-boot:run
```

Application URL:

```text
http://localhost:8080
```

## API Testing

The APIs can be tested using Postman.

The project includes a Postman collection:

```text
HDFC-Learning-Academy.postman_collection.json
```

## Author

**Vinay L.R**

