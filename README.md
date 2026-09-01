# HireHub

HireHub is a Job Recruitment REST API built using Spring Boot. It allows job seekers to search and apply for jobs, recruiters to create and manage job postings, and administrators to manage the platform.

## Features

### User Management

- User registration
- User authentication using Spring Security
- Password encryption
- Update user profile
- Delete user account
- Role-based access control

### Job Management

- Create job postings
- View all jobs
- Search jobs by title
- Search jobs by location
- Search jobs by salary
- Search jobs by job type
- Search jobs by company
- Update job details
- Delete jobs
- Close job postings

### Job Application Management

- Apply for jobs
- Prevent duplicate job applications
- View applications
- Update application status
- Delete applications
- Recruiters can manage applications for their own jobs
- Job seekers can view their own applications

## User Roles

### ADMIN

- Manage users
- Manage all jobs
- Access all job applications

### RECRUITER

- Create job postings
- Update their own jobs
- Delete their own jobs
- Close their own jobs
- View applications for their own jobs
- Update application status

### JOB_SEEKER

- View jobs
- Search jobs
- Apply for jobs
- View their own applications
- Manage their own profile

## Technologies Used

- Java
- Spring Boot
- Spring Data JPA
- Spring Security
- MySQL
- Maven
- Hibernate
- Swagger / OpenAPI
- Lombok

## Security

The application uses Spring Security with Basic Authentication.

Passwords are encrypted using `PasswordEncoder`.

The API implements role-based authorization for:

- ADMIN
- RECRUITER
- JOB_SEEKER

Ownership validation is implemented to ensure:

- Recruiters can manage only their own jobs.
- Job seekers can access only their own applications.
- Users cannot access unauthorized resources.

## API Documentation

Swagger UI is available at:

http://localhost:8081/swagger-ui/index.html

Swagger can be used to explore and test the available API endpoints.

## Exception Handling

The application uses global exception handling with custom exceptions:

- ResourceNotFoundException
- AlreadyExistsException
- AccessDeniedException

## Future Improvements

- JWT authentication
- Email notifications
- Resume upload functionality
- Advanced job filtering
- Improved pagination and sorting
- Frontend integration
- Cloud deployment
- Email verification during registration
- Password reset functionality

## Author

Manjunath Kesarur