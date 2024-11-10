# Task Management System

The Task Management System backend is a standalone application designed to handle the logic and data management
for managing tasks. It provides a set of APIs to perform CRUD operations on tasks and includes authentication,
authorization, validation, and other features.

## System Archticure 

### DataBase Relation 
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
![ER](/Image/rrelationDigram.png)

