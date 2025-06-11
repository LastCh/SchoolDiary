# SchoolDiary

## Project Description

**SchoolDiary** is a Spring MVC-based web application for managing student data. It provides a REST API for:

- adding, updating, deleting, and viewing students;
- storing data in a CSV file (`students.csv`);
- role-based access control (`TEACHER`, `STUDENT`);
- logging operations and errors;
- automatic API documentation via Swagger UI.

---

## Technologies

- Java 11
- Spring MVC
- Gradle (Kotlin DSL)
- Apache Tomcat 9
- AspectJ (AOP)
- Swagger (SpringFox 3.0.0)
- CSV file as database

---

## Requirements

- Java 11+
- Tomcat 9+
- Gradle (uses `./gradlew` wrapper)
- `students.csv` in `src/main/resources/`

---

## Installation and Launch

### 1. Prepare the CSV file

Create file: src/main/resources/students.csv

Content:
id;firstName;lastName;tokens  
1;Alice;Smith;85  
2;Bob;Johnson;90

> On the first run, the application will create a working copy in `data/students.csv`. Header is required. Tokens must be between 0 and 100.

---

### 2. Build the project

In the root directory, run:

./gradlew build

---

### 3. Create WAR file

Run:

./gradlew war

The file `SchoolDiary-1.0.war` will be created in `build/libs/`.

---

### 4. Deploy to Tomcat 9

#### 1. Copy `SchoolDiary-1.0.war` to:

<tomcat>/webapps/

#### 2. Create folders in `tomcat/bin/` if they don’t exist:

logs/ — contains app.log (operation logs)  
data/ — contains students.csv (student data)

---

### 5. Start Tomcat

cd <tomcat>/bin  
startup.bat    # Windows  
./startup.sh   # Linux/macOS

After launch, the application will be available at:

http://localhost:8080/SchoolDiary-1.0/

---

### 6. Swagger UI

Open Swagger UI to interact with the API:

http://localhost:8080/SchoolDiary-1.0/swagger-ui/index.html

---

## API Usage

### Authorization Header

Send the header:

X-Role: TEACHER

The `STUDENT` role (or missing/invalid roles) only has read access.

---

### Example Requests

Method | Path                    | Description             | Role  
GET    | /api/v1/students        | Get all students        | Any  
POST   | /api/v1/students        | Add a new student       | TEACHER  
GET    | /api/v1/students/{id}   | Get student by ID       | Any  
PUT    | /api/v1/students/{id}   | Update tokens           | TEACHER  
DELETE | /api/v1/students/{id}   | Delete student          | TEACHER

---

## Logging

All operations and errors are logged to:

<tomcat>/logs/app.log

### Logs include:

1. Successful service calls
2. Exceptions and errors (including SecurityException, IllegalArgumentException, etc.)

---

## Possible Errors

Code | Description  
-----|------------  
400  | Invalid data (e.g. tokens < 0 or >100)  
403  | Insufficient permissions (missing `X-Role`)  
404  | Student not found

---

## Project Structure

SchoolDiary/  
├── src/  
│   ├── main/  
│   │   ├── java/ru/bmstu/diary/  
│   │   │   ├── controller/  
│   │   │   ├── service/  
│   │   │   ├── repository/  
│   │   │   ├── model/  
│   │   │   ├── config/  
│   │   │   ├── aspect/  
│   │   │   ├── util/
│   │   │   └── annotation/
│   │   └── resources/  
│   │       ├── students.csv  
│   │       └── application.properties  
├── build.gradle.kts  
└── README.md

---

## License

This project is licensed under the MIT License. See the LICENSE file for details.

---

## Author

Last Ch  
GitHub: https://github.com/LastCh

---

## Feedback

If you have any questions or suggestions — please open an issue on GitHub.

Good luck using the app! 🚀
