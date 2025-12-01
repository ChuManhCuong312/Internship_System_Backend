-- ======================================
-- DATABASE: internship_system
-- ======================================
CREATE DATABASE internship_system;
USE internship_system;
-- drop database internship_system

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
    phone VARCHAR(20) UNIQUE,
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
    gpa DOUBLE,
    dob DATE,
    gender ENUM('MALE', 'FEMALE'),
    address VARCHAR(255),
    intern_image_path VARCHAR(255),
    university_confirm VARCHAR(255),
    internship_application_path VARCHAR(255),
    cv_path VARCHAR(255),
    status ENUM('PENDING','APPROVED','REJECTED','NO_FILE') DEFAULT 'NO_FILE',
    rejection_reason  VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
CREATE TABLE contract_documents (
    document_id INT AUTO_INCREMENT PRIMARY KEY,
    intern_id INT,
    file_path VARCHAR(255),
    contract_status ENUM('NOT_UPLOAD','UPLOAD') DEFAULT 'NOT_UPLOAD',
    intern_confirm_status ENUM('APPROVED','PENDING') DEFAULT 'PENDING',
    confirm_at DATETIME,
    note TEXT,
    FOREIGN KEY (intern_id) REFERENCES intern_users(intern_id)
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
    program_status ENUM('UPCOMING','ON_GOING','FINISHED') DEFAULT 'UPCOMING',
    detail TEXT,
    max_interns INT DEFAULT 50
);

CREATE TABLE mentor_program (
	mentor_program_id INT AUTO_INCREMENT PRIMARY KEY,
    program_id INT,
    mentor_id INT,
    assigned_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (program_id) REFERENCES programs(program_id),
    FOREIGN KEY (mentor_id) REFERENCES mentor_users(mentor_id)
);
CREATE TABLE program_events (
    event_id        INT AUTO_INCREMENT PRIMARY KEY,
    program_id      INT NOT NULL,                              -- FK tới programs
    title           VARCHAR(200) NOT NULL,                     -- tên sự kiện
    location        VARCHAR(200),                              -- địa điểm (tuỳ chọn)
    event_date      DATE NOT NULL,                    -- ngày diễn ra
    start_time      TIME NOT NULL,                    -- giờ bắt đầu
    end_time        TIME NOT NULL,                    -- giờ kết thúc (cùng ngày)
    description     TEXT,                                      -- mô tả chi tiết

    -- Ràng buộc khóa ngoại: khi xóa chương trình thì xóa luôn sự kiện (CASCADE)
    CONSTRAINT fk_program_events_program
        FOREIGN KEY (program_id) REFERENCES programs(program_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    -- Tránh trùng lặp tiêu đề trong cùng một chương trình
    CONSTRAINT uq_program_event_title UNIQUE (program_id, title)
);
CREATE TABLE teams (
	team_id INT AUTO_INCREMENT PRIMARY KEY,
    program_id INT,
    mentor_id INT,
    assigned_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (program_id) REFERENCES programs(program_id),
    FOREIGN KEY (mentor_id) REFERENCES mentor_users(mentor_id)
);

CREATE TABLE team_intern(
    team_intern_id INT AUTO_INCREMENT PRIMARY KEY,
    team_id INT,
    intern_id INT,
    assigned_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (team_id) REFERENCES teams(team_id),
    FOREIGN KEY (intern_id) REFERENCES intern_users(intern_id)
);

-- ======================================
-- 4. TASK MANAGEMENT
-- ======================================

CREATE TABLE tasks (
    task_id INT AUTO_INCREMENT PRIMARY KEY,
    program_id INT,
    title VARCHAR(200),
    description TEXT,
    assigned_by INT,
    priority ENUM('LOW','MEDIUM','HIGH') DEFAULT 'MEDIUM',
    status ENUM('TODO','IN_PROGRESS','DONE','REVIEWED') DEFAULT 'TODO',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deadline DATE,
    due_soon BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (assigned_by) REFERENCES mentor_users(mentor_id),
    FOREIGN KEY (program_id) REFERENCES programs(program_id)
);

CREATE TABLE task_team_assignments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    task_id INT NOT NULL,
    team_id INT NOT NULL,
    FOREIGN KEY (task_id) REFERENCES tasks(task_id),
    FOREIGN KEY (team_id) REFERENCES teams(team_id)
);
CREATE TABLE tasks_files (
    task_files_id INT AUTO_INCREMENT PRIMARY KEY,
	task_id INT,
    link_file VARCHAR(255),
    FOREIGN KEY (task_id) REFERENCES tasks(task_id)
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
    FOREIGN KEY (intern_id) REFERENCES intern_users(intern_id)
);
CREATE TABLE leave_requests (
    leave_id INT AUTO_INCREMENT PRIMARY KEY,
    intern_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    reason VARCHAR(255),
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    rejection_reason VARCHAR(255),
    request_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    processed_by INT,
    FOREIGN KEY (processed_by) REFERENCES hr_users(hr_id),
    FOREIGN KEY (intern_id) REFERENCES intern_users(intern_id)
);
CREATE TABLE allowances (
    allowance_id INT AUTO_INCREMENT PRIMARY KEY,
    intern_id INT,
    type VARCHAR(50),
    amount DECIMAL(10,2),
    date_applied DATE,
    note TEXT,
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
    mentor_evaluate_id INT,
    title VARCHAR(255) NOT NULL,
    technical INT CHECK (technical BETWEEN 0 AND 10),
    communication INT CHECK (communication BETWEEN 0 AND 10),
    discipline INT CHECK (discipline BETWEEN 0 AND 10),
    attitude INT CHECK (attitude BETWEEN 0 AND 10),
    weight INT CHECK (weight BETWEEN 0 AND 100),
    note TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (intern_id) REFERENCES intern_users(intern_id),
    FOREIGN KEY (mentor_evaluate_id) REFERENCES mentor_users(mentor_id)
);

-- ======================================
-- 7. DOCUMENTS & AUDIT
-- ======================================

CREATE TABLE admin_logs (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    details TEXT, -- CREATE_USER/UPDATE_USER/DELETE_USER
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE hr_logs (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    hr_id INT NOT NULL,
    details TEXT, --  'APPROVE_INTERN', 'REJECT_INTERN', 'UPLOAD_CONTRACT', 'SEND_EMAIL'
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (hr_id) REFERENCES hr_users(hr_id)
);
CREATE TABLE intern_logs (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    intern_id INT NOT NULL,
    details TEXT, -- 'CONFIRM_CONTRACT','UPLOAD_DOCUMENT','UPDATE_PROFILE','SEND_SUPPORT_REQUEST'
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (intern_id) REFERENCES intern_users(intern_id)
);

-- ======================================
-- 8. NOTIFICATIONS
-- ======================================
-- Bảng thông báo cho Intern
CREATE TABLE notifications (
    notification_id INT AUTO_INCREMENT PRIMARY KEY,
    intern_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    message TEXT,
    type VARCHAR(50), -- 'ALLOWANCE', 'TASK', 'LEAVE', 'EVALUATION', etc.
    is_read BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (intern_id) REFERENCES intern_users(intern_id)
);
-- Bảng thông báo cho HR
CREATE TABLE HRNotifications (
    notification_id INT AUTO_INCREMENT PRIMARY KEY,
    hr_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    message TEXT,
    type VARCHAR(50), -- 'ALLOWANCE', 'TASK', 'LEAVE', 'EVALUATION', etc.
    is_read BOOLEAN DEFAULT FALSE, -- trong MySQL là TINYINT(1)
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (hr_id) REFERENCES hr_users(hr_id)
);
-- Bảng thông báo cho Mentor
CREATE TABLE MentorNotifications (
    notification_id INT AUTO_INCREMENT PRIMARY KEY,
    mentor_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    message TEXT,
    type VARCHAR(50), -- 'ALLOWANCE', 'TASK', 'LEAVE', 'EVALUATION', etc.
    is_read BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (mentor_id) REFERENCES mentor_users(mentor_id)
);
