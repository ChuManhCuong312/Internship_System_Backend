-- ======================================
-- DATABASE: internship_system
-- ======================================
CREATE DATABASE internship_system;
USE internship_system;

-- ======================================
-- 1. USERS & ROLES
-- ======================================

CREATE TABLE roles (
    role_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255)
);

CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    status ENUM('ACTIVE','INACTIVE','REJECTED','PENDING_APPROVAL') DEFAULT 'ACTIVE',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    role_id INT,
    FOREIGN KEY (role_id) REFERENCES roles(role_id)
);

CREATE TABLE permissions (
    permission_id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    description VARCHAR(255)
);

CREATE TABLE role_permissions (
    role_id INT,
    permission_id INT,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES roles(role_id),
    FOREIGN KEY (permission_id) REFERENCES permissions(permission_id)
);

-- ======================================
-- 2. ROLE-SPECIFIC TABLES
-- ======================================

CREATE TABLE admin_users (
    admin_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    position VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE hr_users (
    hr_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    department VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE mentor_users (
    mentor_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    department VARCHAR(100),
    expertise VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE intern_users (
    intern_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    school VARCHAR(150),
    major VARCHAR(150),
    dob DATE,
    address VARCHAR(255),
    cv_path VARCHAR(255),
    status ENUM('PENDING','APPROVED','REJECTED','ACTIVE','COMPLETED') DEFAULT 'PENDING',
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- ======================================
-- 3. PROGRAM MANAGEMENT
-- ======================================

CREATE TABLE programs (
    program_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    department VARCHAR(100),
    start_date DATE,
    end_date DATE,
    max_interns INT DEFAULT 10,
    created_by INT,
    FOREIGN KEY (created_by) REFERENCES hr_users(hr_id)
);

CREATE TABLE intern_program (
    program_id INT,
    intern_id INT,
    assigned_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (program_id, intern_id),
    FOREIGN KEY (program_id) REFERENCES programs(program_id),
    FOREIGN KEY (intern_id) REFERENCES intern_users(intern_id)
);

-- ======================================
-- 4. TASK MANAGEMENT
-- ======================================

CREATE TABLE tasks (
    task_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200),
    description TEXT,
    assigned_by INT,
    mentor_id INT,
    intern_id INT,
    priority ENUM('LOW','MEDIUM','HIGH') DEFAULT 'MEDIUM',
    status ENUM('TODO','IN_PROGRESS','DONE','REVIEWED') DEFAULT 'TODO',
    deadline DATE,
    FOREIGN KEY (mentor_id) REFERENCES mentor_users(mentor_id),
    FOREIGN KEY (intern_id) REFERENCES intern_users(intern_id)
);

CREATE TABLE task_progress (
    progress_id INT AUTO_INCREMENT PRIMARY KEY,
    task_id INT,
    percent_complete INT CHECK (percent_complete BETWEEN 0 AND 100),
    note TEXT,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (task_id) REFERENCES tasks(task_id)
);

-- ======================================
-- 5. ATTENDANCE & ALLOWANCE
-- ======================================

CREATE TABLE attendance (
    attendance_id INT AUTO_INCREMENT PRIMARY KEY,
    intern_id INT,
    date DATE NOT NULL,
    check_in TIME,
    check_out TIME,
    location VARCHAR(150),
    FOREIGN KEY (intern_id) REFERENCES intern_users(intern_id)
);

CREATE TABLE allowances (
    allowance_id INT AUTO_INCREMENT PRIMARY KEY,
    intern_id INT,
    type ENUM('TRANSPORT','MEAL','BONUS') DEFAULT 'MEAL',
    amount DECIMAL(10,2),
    date_applied DATE,
    FOREIGN KEY (intern_id) REFERENCES intern_users(intern_id)
);

-- ======================================
-- 6. SUPPORT & EVALUATION
-- ======================================

CREATE TABLE support_requests (
    request_id INT AUTO_INCREMENT PRIMARY KEY,
    intern_id INT,
    type ENUM('TECHNICAL','ADMIN','HR','OTHER') DEFAULT 'OTHER',
    description TEXT,
    file_path VARCHAR(255),
    status ENUM('OPEN','IN_PROGRESS','RESOLVED','REJECTED') DEFAULT 'OPEN',
    response TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (intern_id) REFERENCES intern_users(intern_id)
);

CREATE TABLE evaluations (
    evaluation_id INT AUTO_INCREMENT PRIMARY KEY,
    intern_id INT,
    mentor_id INT,
    technical INT CHECK (technical BETWEEN 0 AND 10),
    communication INT CHECK (communication BETWEEN 0 AND 10),
    discipline INT CHECK (discipline BETWEEN 0 AND 10),
    attitude INT CHECK (attitude BETWEEN 0 AND 10),
    note TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (intern_id) REFERENCES intern_users(intern_id),
    FOREIGN KEY (mentor_id) REFERENCES mentor_users(mentor_id)
);

-- ======================================
-- 7. DOCUMENTS & AUDIT
-- ======================================

CREATE TABLE documents (
    document_id INT AUTO_INCREMENT PRIMARY KEY,
    intern_id INT,
    type ENUM('CV','REPORT','OTHERS') DEFAULT 'CV',
    file_path VARCHAR(255),
    status ENUM('PENDING','APPROVED','REJECTED') DEFAULT 'PENDING',
    reviewed_by INT,
    reviewed_at DATETIME,
    review_note TEXT,
    FOREIGN KEY (intern_id) REFERENCES intern_users(intern_id),
    FOREIGN KEY (reviewed_by) REFERENCES hr_users(hr_id)
);

CREATE TABLE audit_logs (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    action VARCHAR(255),
    ip_address VARCHAR(45),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);


-- Bổ sung bảng Quan hệ phân công Mentor ↔ Intern
CREATE TABLE mentor_assignments (
    assignment_id INT AUTO_INCREMENT PRIMARY KEY,
    mentor_id INT NOT NULL,
    intern_id INT NOT NULL,
    assigned_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    status ENUM('ACTIVE','COMPLETED','CANCELLED') DEFAULT 'ACTIVE',
    FOREIGN KEY (mentor_id) REFERENCES mentor_users(mentor_id),
    FOREIGN KEY (intern_id) REFERENCES intern_users(intern_id)
);

-- Thêm bảng Profiles để lưu profiles thực tập sinh
CREATE TABLE profiles (
    profile_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    photo_path VARCHAR(255),
    full_name VARCHAR(100) NOT NULL,
    gender ENUM('Nam','Nữ','Khác') DEFAULT 'Khác',
    dob DATE,
    school VARCHAR(150),
    major VARCHAR(150),
    gpa DECIMAL(3,2) CHECK (gpa BETWEEN 0 AND 4.00),
    phone VARCHAR(20),
    address VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);