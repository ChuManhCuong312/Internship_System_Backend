# Task API Documentation

Base URL: `http://localhost:8080`

---

## TaskController (`/api/tasks`)

### 1. Create Task

**POST** `/api/tasks`

**Request Body:**
```json
{
  "programId": 1,
  "title": "Task Title",
  "description": "Task description",
  "assignedBy": "MENTOR",
  "status": "TODO",
  "deadline": "2024-12-31T23:59:59",
  "priority": "HIGH",
  "mentorId": 1
}
```

**Response:** `201 Created`
```json
{
  "taskId": 1,
  "programId": 1,
  "title": "Task Title",
  "description": "Task description",
  "assignedBy": "MENTOR",
  "status": "TODO",
  "created_at": "2024-12-01T10:00:00",
  "deadline": "2024-12-31T23:59:59",
  "due_soon": false,
  "priority": "HIGH",
  "mentorId": 1
}
```

---

### 2. Get All Tasks

**GET** `/api/tasks`

**Query Parameters:**
| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| sortBy | string | No | - | Sort field: `taskId`, `mentorId`, `programId`, `title`, `status`, `priority`, `deadline` |
| direction | string | No | `asc` | Sort direction: `asc` or `desc` |
| page | integer | No | - | Page number (0-indexed) |
| size | integer | No | - | Page size |

**Response (without pagination):** `200 OK`
```json
[
  {
    "taskId": 1,
    "programId": 1,
    "programName": "Internship Program 2024",
    "title": "Task Title",
    "description": "Task description",
    "assignedBy": "MENTOR",
    "status": "TODO",
    "createdAt": "2024-12-01T10:00:00",
    "deadline": "2024-12-31T23:59:59",
    "dueSoon": false,
    "priority": "HIGH",
    "mentorId": 1,
    "mentorName": "John Doe"
  }
]
```

**Response (with pagination):** `200 OK`
```json
{
  "content": [
    {
      "taskId": 1,
      "programId": 1,
      "programName": "Internship Program 2024",
      "title": "Task Title",
      "description": "Task description",
      "assignedBy": "MENTOR",
      "status": "TODO",
      "createdAt": "2024-12-01T10:00:00",
      "deadline": "2024-12-31T23:59:59",
      "dueSoon": false,
      "priority": "HIGH",
      "mentorId": 1,
      "mentorName": "John Doe"
    }
  ],
  "totalElements": 100,
  "totalPages": 10,
  "currentPage": 0,
  "pageSize": 10
}
```

---

### 3. Get Task by ID

**GET** `/api/tasks/{id}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| id | integer | Task ID |

**Response:** `200 OK`
```json
{
  "taskId": 1,
  "programId": 1,
  "title": "Task Title",
  "description": "Task description",
  "assignedBy": "MENTOR",
  "status": "TODO",
  "created_at": "2024-12-01T10:00:00",
  "deadline": "2024-12-31T23:59:59",
  "due_soon": false,
  "priority": "HIGH",
  "mentorId": 1
}
```

**Response:** `404 Not Found` (if task not found)

---

### 4. Get Tasks by Mentor ID

**GET** `/api/tasks/mentor/{mentorId}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| mentorId | integer | Mentor ID |

**Query Parameters:**
| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| sortBy | string | No | - | Sort field |
| direction | string | No | `asc` | Sort direction |
| page | integer | No | - | Page number |
| size | integer | No | - | Page size |

**Response:** `200 OK` (same as Get All Tasks)

---

### 5. Get Tasks by Program ID

**GET** `/api/tasks/program/{programId}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| programId | integer | Program ID |

**Query Parameters:**
| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| sortBy | string | No | - | Sort field |
| direction | string | No | `asc` | Sort direction |
| page | integer | No | - | Page number |
| size | integer | No | - | Page size |

**Response:** `200 OK` (same as Get All Tasks)

---

### 6. Get Tasks by Intern ID

**GET** `/api/tasks/intern/{internId}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| internId | integer | Intern ID |

**Response:** `200 OK`
```json
[
  {
    "taskId": 1,
    "programId": 1,
    "programName": "Internship Program 2024",
    "title": "Task Title",
    "description": "Task description",
    "assignedBy": "MENTOR",
    "status": "TODO",
    "createdAt": "2024-12-01T10:00:00",
    "deadline": "2024-12-31T23:59:59",
    "dueSoon": false,
    "priority": "HIGH",
    "mentorId": 1,
    "mentorName": "John Doe"
  }
]
```

---

### 7. Update Task

**PUT** `/api/tasks/{id}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| id | integer | Task ID |

**Request Body:**
```json
{
  "programId": 1,
  "title": "Updated Task Title",
  "description": "Updated description",
  "assignedBy": "MENTOR",
  "status": "IN_PROGRESS",
  "deadline": "2024-12-31T23:59:59",
  "priority": "MEDIUM",
  "mentorId": 1
}
```

**Response:** `200 OK`
```json
{
  "taskId": 1,
  "programId": 1,
  "title": "Updated Task Title",
  "description": "Updated description",
  "assignedBy": "MENTOR",
  "status": "IN_PROGRESS",
  "created_at": "2024-12-01T10:00:00",
  "deadline": "2024-12-31T23:59:59",
  "due_soon": false,
  "priority": "MEDIUM",
  "mentorId": 1
}
```

**Response:** `404 Not Found` (if task not found)

---

### 8. Update Task Status

**PATCH** `/api/tasks/{id}/status`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| id | integer | Task ID |

**Request Body:**
```json
"DONE"
```

**Response:** `200 OK`
```json
{
  "taskId": 1,
  "programId": 1,
  "title": "Task Title",
  "description": "Task description",
  "assignedBy": "MENTOR",
  "status": "DONE",
  "created_at": "2024-12-01T10:00:00",
  "deadline": "2024-12-31T23:59:59",
  "due_soon": false,
  "priority": "HIGH",
  "mentorId": 1
}
```

---

### 9. Filter Tasks

**GET** `/api/tasks/filter/search`

**Query Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| mentorId | integer | No | Filter by mentor ID |
| programId | integer | No | Filter by program ID |
| status | string | No | Filter by status: `TODO`, `IN_PROGRESS`, `DONE`, `REVIEWED` |
| priority | string | No | Filter by priority: `LOW`, `MEDIUM`, `HIGH` |
| startDate | string | No | Filter by start date (ISO format: `2024-12-01T00:00:00`) |
| endDate | string | No | Filter by end date (ISO format: `2024-12-31T23:59:59`) |
| page | integer | No | Page number |
| size | integer | No | Page size |

**Response:** `200 OK` (same as Get All Tasks)

---

### 10. Delete Task

**DELETE** `/api/tasks/{id}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| id | integer | Task ID |

**Response:** `204 No Content`

---

---

## TaskManagementController (`/api/task-management`)

---

## Task Progress Endpoints

### 1. Create Task Progress

**POST** `/api/task-management/progress`

**Request Body:**
```json
{
  "taskId": 1,
  "percentComplete": 0,
  "notes": "Initial progress"
}
```

**Response:** `201 Created`
```json
{
  "progressId": 1,
  "taskId": 1,
  "percentComplete": 0,
  "notes": "Initial progress",
  "updatedAt": "2024-12-01T10:00:00"
}
```

---

### 2. Get All Task Progress

**GET** `/api/task-management/progress`

**Response:** `200 OK`
```json
[
  {
    "progressId": 1,
    "taskId": 1,
    "taskTitle": "Task Title",
    "percentComplete": 50,
    "notes": "Progress notes",
    "updatedAt": "2024-12-01T10:00:00"
  }
]
```

---

### 3. Get Task Progress by Progress ID

**GET** `/api/task-management/progress/{progressId}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| progressId | integer | Progress ID |

**Response:** `200 OK`
```json
{
  "progressId": 1,
  "taskId": 1,
  "percentComplete": 50,
  "notes": "Progress notes",
  "updatedAt": "2024-12-01T10:00:00"
}
```

---

### 4. Get Task Progress by Task ID

**GET** `/api/task-management/progress/task/{taskId}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| taskId | integer | Task ID |

**Response:** `200 OK`
```json
{
  "progressId": 1,
  "taskId": 1,
  "taskTitle": "Task Title",
  "percentComplete": 50,
  "notes": "Progress notes",
  "updatedAt": "2024-12-01T10:00:00"
}
```

---

### 5. Update Task Progress

**PUT** `/api/task-management/progress/{progressId}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| progressId | integer | Progress ID |

**Request Body:**
```json
{
  "taskId": 1,
  "percentComplete": 75,
  "notes": "Updated progress"
}
```

**Response:** `200 OK`
```json
{
  "progressId": 1,
  "taskId": 1,
  "percentComplete": 75,
  "notes": "Updated progress",
  "updatedAt": "2024-12-01T12:00:00"
}
```

---

### 6. Update Progress Percentage

**PATCH** `/api/task-management/progress/{progressId}/percentage?percentage={value}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| progressId | integer | Progress ID |

**Query Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| percentage | integer | Yes | Percentage value (0-100) |

**Response:** `200 OK`
```json
{
  "progressId": 1,
  "taskId": 1,
  "percentComplete": 80,
  "notes": "Progress notes",
  "updatedAt": "2024-12-01T12:00:00"
}
```

**Response:** `400 Bad Request` (if percentage < 0 or > 100)

---

### 7. Delete Task Progress

**DELETE** `/api/task-management/progress/{progressId}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| progressId | integer | Progress ID |

**Response:** `204 No Content`

---

## Task Files Endpoints

### 1. Create Task File

**POST** `/api/task-management/files`

**Request Body:**
```json
{
  "taskId": 1,
  "fileName": "document.pdf",
  "filePath": "/uploads/tasks/1/document.pdf",
  "fileType": "application/pdf",
  "fileSize": 1024
}
```

**Response:** `201 Created`
```json
{
  "taskFilesId": 1,
  "taskId": 1,
  "fileName": "document.pdf",
  "filePath": "/uploads/tasks/1/document.pdf",
  "fileType": "application/pdf",
  "fileSize": 1024,
  "uploadedAt": "2024-12-01T10:00:00"
}
```

---

### 2. Get All Task Files

**GET** `/api/task-management/files`

**Response:** `200 OK`
```json
[
  {
    "taskFilesId": 1,
    "taskId": 1,
    "taskTitle": "Task Title",
    "fileName": "document.pdf",
    "filePath": "/uploads/tasks/1/document.pdf",
    "fileType": "application/pdf",
    "fileSize": 1024,
    "uploadedAt": "2024-12-01T10:00:00"
  }
]
```

---

### 3. Get Task File by File ID

**GET** `/api/task-management/files/{fileId}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| fileId | integer | File ID |

**Response:** `200 OK`
```json
{
  "taskFilesId": 1,
  "taskId": 1,
  "fileName": "document.pdf",
  "filePath": "/uploads/tasks/1/document.pdf",
  "fileType": "application/pdf",
  "fileSize": 1024,
  "uploadedAt": "2024-12-01T10:00:00"
}
```

---

### 4. Get Files by Task ID

**GET** `/api/task-management/files/task/{taskId}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| taskId | integer | Task ID |

**Response:** `200 OK`
```json
[
  {
    "taskFilesId": 1,
    "taskId": 1,
    "taskTitle": "Task Title",
    "fileName": "document.pdf",
    "filePath": "/uploads/tasks/1/document.pdf",
    "fileType": "application/pdf",
    "fileSize": 1024,
    "uploadedAt": "2024-12-01T10:00:00"
  }
]
```

---

### 5. Update Task File

**PUT** `/api/task-management/files/{fileId}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| fileId | integer | File ID |

**Request Body:**
```json
{
  "taskId": 1,
  "fileName": "updated_document.pdf",
  "filePath": "/uploads/tasks/1/updated_document.pdf",
  "fileType": "application/pdf",
  "fileSize": 2048
}
```

**Response:** `200 OK`
```json
{
  "taskFilesId": 1,
  "taskId": 1,
  "fileName": "updated_document.pdf",
  "filePath": "/uploads/tasks/1/updated_document.pdf",
  "fileType": "application/pdf",
  "fileSize": 2048,
  "uploadedAt": "2024-12-01T10:00:00"
}
```

---

### 6. Delete Task File

**DELETE** `/api/task-management/files/{fileId}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| fileId | integer | File ID |

**Response:** `204 No Content`

---

## Task Team Assignment Endpoints

### 1. Create Task Team Assignment

**POST** `/api/task-management/team-assignments`

**Request Body:**
```json
{
  "taskId": 1,
  "teamId": 1
}
```

**Response:** `201 Created`
```json
{
  "id": 1,
  "taskId": 1,
  "teamId": 1,
  "assignedAt": "2024-12-01T10:00:00"
}
```

---

### 2. Get All Task Team Assignments

**GET** `/api/task-management/team-assignments`

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "taskId": 1,
    "teamId": 1,
    "assignedAt": "2024-12-01T10:00:00"
  }
]
```

---

### 3. Get Task Team Assignment by ID

**GET** `/api/task-management/team-assignments/{assignmentId}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| assignmentId | integer | Assignment ID |

**Response:** `200 OK`
```json
{
  "id": 1,
  "taskId": 1,
  "teamId": 1,
  "assignedAt": "2024-12-01T10:00:00"
}
```

---

### 4. Get Assignments by Task ID

**GET** `/api/task-management/team-assignments/task/{taskId}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| taskId | integer | Task ID |

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "taskId": 1,
    "teamId": 1,
    "assignedAt": "2024-12-01T10:00:00"
  }
]
```

---

### 5. Get Assignments by Team ID

**GET** `/api/task-management/team-assignments/team/{teamId}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| teamId | integer | Team ID |

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "taskId": 1,
    "teamId": 1,
    "assignedAt": "2024-12-01T10:00:00"
  }
]
```

---

### 6. Update Task Team Assignment

**PUT** `/api/task-management/team-assignments/{assignmentId}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| assignmentId | integer | Assignment ID |

**Request Body:**
```json
{
  "taskId": 1,
  "teamId": 2
}
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "taskId": 1,
  "teamId": 2,
  "assignedAt": "2024-12-01T10:00:00"
}
```

---

### 7. Delete Task Team Assignment

**DELETE** `/api/task-management/team-assignments/{assignmentId}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| assignmentId | integer | Assignment ID |

**Response:** `204 No Content`

---

## Error Responses

All endpoints may return the following error responses:

| Status Code | Description |
|-------------|-------------|
| `400 Bad Request` | Invalid request parameters |
| `404 Not Found` | Resource not found |
| `500 Internal Server Error` | Server error |

---

## Status Values

**Task Status:**
- `TODO` - Not started
- `IN_PROGRESS` - In progress
- `DONE` - Completed
- `REVIEWED` - Reviewed

**Task Priority:**
- `LOW` - Low priority
- `MEDIUM` - Medium priority
- `HIGH` - High priority
