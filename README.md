# Task Management System

The Task Management System backend is a standalone application designed to handle the logic and data management
for managing tasks. It provides a set of APIs to perform CRUD operations on tasks and includes authentication,
authorization, validation, and other features.

## System Archticure 

### DataBase Archticure 
This project defines the structure of a Task Management System using three main tables: app_user, tasks, and task_history. Below is a detailed explanation of the tables and their relationships, along with a visual representation.

#### 1. app_user Table
- **id**: Primary Key, a unique identifier for each user.
- **username**: A string representing the user's name. This field is required.
- **password**: A string storing the user's password. This field is required.
- **role**: A string defining the user's role, such as 'ADMIN' or 'USER'. This field is required.
- **email**: A string representing the user's email address. This field is required.
#### 2. tasks Table
 - **id**: Primary Key, a unique identifier for each task.
- **title**: The title of the task. This field is required.
- **description**: A detailed description of the task, up to 1000 characters.
- **status**: An enum field indicating the status of the task. Possible values: TODO, IN_PROGRESS, DONE.
- **priority***: An enum field indicating the task's priority level. Possible values: LOW, MEDIUM, HIGH.
- **dueDate**: The due date for the task.
- **createdDate**: The date when the task was created.
- **user_id**: A Foreign Key referencing app_user(id), representing the user assigned to the task.
- **created_user_id**: A Foreign Key referencing app_user(id), indicating the user who created the task.
#### 3. task_history Table
- **id**: Primary Key, a unique identifier for each history record.
- **user_id**: A Foreign Key referencing app_user(id), representing the user associated with this history entry.
- taskId: An identifier for the task associated with this history entry.
- **description**: A detailed description of the history event, up to 1000 characters.
- **date**: The date and time when the history entry was created.
- **action**: A string describing the action taken (e.g., 'CREATE', 'UPDATE','DELETE').
#### Relationships
- Each task must be associated with a user (user_id), and a creator (created_user_id), both referencing app_user(id).
- The task_history table references app_user(id) to log which user performed an action on a task.
#### ER Diagram
![ER](/Image/relationDigram.png)
#### Database Script
This script sets up the initial database schema and populates it with sample data.

```sql
-- Creating the User table
CREATE TABLE app_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
);

-- Creating the Task table
CREATE TABLE tasks (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    status ENUM('TODO', 'IN_PROGRESS', 'DONE') NOT NULL,
    priority ENUM('LOW', 'MEDIUM', 'HIGH') NOT NULL,
    dueDate DATETIME,
    createdDate DATETIME,
    user_id BIGINT NOT NULL,
    created_user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES app_user(id),
    FOREIGN KEY (created_user_id) REFERENCES app_user(id)
);

-- Creating the History table
CREATE TABLE task_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    taskId BIGINT,
    description VARCHAR(1000),
    date DATETIME,
    action VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES app_user(id)
);
```
### API Archticure
This API allows users to manage thier account and thier tasks.

#### Base URL 
/api

#### Authentication

This API uses Bearer Token authentication. Include your token in the `Authorization` header:

Authorization: Bearer YOUR_ACCESS_TOKEN

#### Endpoints

- Authnication API
```plaintext
POST /api/auth
```
**Description**: Api will return the authentication token and user roles if successful authentication

**Request Body**:
```json
{
    "username":"username",
    "password":"password"
    
}
```

**Response**:
```json
{
    "token": "YOUR_ACCESS_TOKEN",
    "role": "user"
}
```
- Register User API
```plaintext
POST /api/user/register
```
**Description**: user can register in application using this API 

**Request Body**:
```json
{
    "username":"username",
    "password":"password",
    "role":"user",
    "email":"mail@test.com"
}
```

**Response**:
```json
User registered successfully!
```
- Create Task API
```plaintext
POST /api/user/task
```
**Description**: user can create task and assign user. 

**Request Body**:
```json
{
  "title": "task title", //String 
  "description": "task description", //String 
  "status": "IN_PROGRESS", // Enum values(TODO,IN_PROGRESS,DONE)
  "priority": "MEDIUM", // Enum (LOW,MEDIUM,HIGH)
  "dueDate": "2024-11-12 20:15:03", //Date Time 
  "user_id": "1" // Long assgin user Id 
}
```

**Response**:
```json
{
  "id":1
  "title": "task title",
  "description": "task description",
  "status": "IN_PROGRESS", 
  "priority": "MEDIUM",
  "dueDate": "2024-11-12 20:15:03",
  "user_id": "1"
}
```
- Update Task API
```plaintext
PUT /api/user/task
```
**Description**: user can update task. 

**Request Body**:
```json
{
  "title": "task title", //String 
  "description": "task description", //String 
  "status": "IN_PROGRESS", // Enum values(TODO,IN_PROGRESS,DONE)
  "priority": "MEDIUM", // Enum (LOW,MEDIUM,HIGH)
  "dueDate": "2024-11-12 20:15:03", //Date Time 
  "user_id": "1" // Long assgin user Id
  "id":1 //Id of updated task
}
```

**Response**:
```json
{
  "id":1
  "title": "task title",
  "description": "task description",
  "status": "IN_PROGRESS", 
  "priority": "MEDIUM",
  "dueDate": "2024-11-12 20:15:03",
  "user_id": "1"
}
```
- Delete Task API
```plaintext
DELETE api/admin/task/{taskId}
```
**Description**: user who has role admin can delete the task. 


**Response**:
```json
{
  "id":1
  "title": "task title",
  "description": "task description",
  "status": "IN_PROGRESS", 
  "priority": "MEDIUM",
  "dueDate": "2024-11-12 20:15:03",
  "user_id": "1"
}
```
- Get Task API
```plaintext
GET api/user/task/{taskId}
```
**Description**: This endpoint retrieves a list of tasks based on optional filtering criteria provided as query parameters.

**Query Parameters:**

**title** (String, optional): Filter tasks by title.

**status** (Task.Status, optional): Filter tasks by their status (e.g., PENDING, COMPLETED).

**priority** (Task.Priority, optional): Filter tasks by their priority level (e.g., HIGH, MEDIUM, LOW).

**userId** (Long, optional): Filter tasks assigned to a specific user.

**description** (String, optional): Filter tasks containing a specific description.

**dueDateFrom** (LocalDateTime, optional): Filter tasks with a due date starting from this date. Format: yyyy-MM-dd HH:mm:ss.

**dueDateTo** (LocalDateTime, optional): Filter tasks with a due date up to this date. Format: yyyy-MM-dd HH:mm:ss.

**createDateFrom** (LocalDateTime, optional): Filter tasks created from this date. Format: yyyy-MM-dd HH:mm:ss.

**createDateTo** (LocalDateTime, optional): Filter tasks created up to this date. Format: yyyy-MM-dd HH:mm:ss.

**page** (int, optional, default: 0): The page number for paginated results.

**size** (int, optional, default: 10): The number of tasks per page.

**Response**:
```json
[
  {
    "id": 0,
    "title": "string",
    "description": "string",
    "status": "TODO",
    "priority": "LOW",
    "assignUser": "string",
    "createdUser": "string",
    "dueDate": "2024-11-10T19:05:27.284Z",
    "createdDate": "2024-11-10T19:05:27.284Z"
  }
]
```
### Instlation in local machine
- Using H2 database to easy install in local machine (we can use any database, just change configurations in application.priorities and add derviver connector to pom file)
- Application will automatically create schema
- Set environment variables
 
   - DATABASE_URL
   
   - DATABASE_DRIVER_CLASS_NAME
   
   - DATABASE_USER_NAME
   
   - DATABASE_PASSWORD
   
   - EMAIL_SERVER_HOST
   
   - EMAIL_SERVER_PORT
   
   - EMAIL_USER_NAME
   
   - EMAIL_PASSWORD
   
 - Run Application 
 ```plaintext
 java -jar taskManagmentSystem-1.0.0.jar
```
- Use API and make sure you can call defferent API 

### Instlation using docker 
```bash

docker build -t task_managment_system .

docker run -p 8090:8090 -e DATABASE_URL=jdbc:h2:mem:testdb     -e DATABASE_DRIVER_CLASS_NAME=org.h2.Driver      -e DATABASE_USER_NAME=sa      -e DATABASE_PASSWORD=password      -e EMAIL_SERVER_HOST=smtp.example.com     -e EMAIL_SERVER_PORT=587     -e EMAIL_USER_NAME=emailUser    -e EMAIL_PASSWORD=emailPassword    task_managment_system
```

   
   