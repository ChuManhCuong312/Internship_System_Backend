INSERT INTO roles (role_id, name, description) VALUES
(1, 'ADMIN', 'Quản trị viên hệ thống'),
(2, 'HR', 'Bộ phận nhân sự'),
(3, 'MENTOR', 'Người hướng dẫn thực tập'),
(4, 'INTERN', 'Thực tập sinh');

INSERT INTO users (user_id, email, password_hash, full_name, phone, status, role_id) VALUES
(1, 'admin@intern.vn', '$2a$12$uIY3CJXTaEkuiMqIhesO6ejgxazDdq8F58Qqo4tLSI0udaGfICCRW', 'Nguyễn Quang Huy', '0987654321', 'ACTIVE', 1),
(2, 'hr01@intern.vn', '$2a$12$uIY3CJXTaEkuiMqIhesO6ejgxazDdq8F58Qqo4tLSI0udaGfICCRW', 'Trần Thu Hà', '0912000111', 'ACTIVE', 2),
(3, 'hr02@intern.vn', '$2a$12$uIY3CJXTaEkuiMqIhesO6ejgxazDdq8F58Qqo4tLSI0udaGfICCRW', 'Phạm Văn Cường', '0905999888', 'ACTIVE', 2),
(4, 'mentor01@intern.vn', '$2a$12$uIY3CJXTaEkuiMqIhesO6ejgxazDdq8F58Qqo4tLSI0udaGfICCRW', 'Đặng Minh Tâm', '0977222111', 'ACTIVE', 3),
(5, 'mentor02@intern.vn', '$2a$12$uIY3CJXTaEkuiMqIhesO6ejgxazDdq8F58Qqo4tLSI0udaGfICCRW', 'Lê Thị Hạnh', '0909888777', 'ACTIVE', 3),
(6, 'mentor03@intern.vn', '$2a$12$uIY3CJXTaEkuiMqIhesO6ejgxazDdq8F58Qqo4tLSI0udaGfICCRW', 'Phạm Tuấn Anh', '0912456789', 'ACTIVE', 3),
(7, 'tts01@cmcuni.edu.vn', '$2a$12$uIY3CJXTaEkuiMqIhesO6ejgxazDdq8F58Qqo4tLSI0udaGfICCRW', 'Nguyễn Hữu Nam', '0356789123', 'ACTIVE', 4),
(8, 'tts02@cmcuni.edu.vn', '$2a$12$uIY3CJXTaEkuiMqIhesO6ejgxazDdq8F58Qqo4tLSI0udaGfICCRW', 'Trần Thị Mai', '0389988776', 'ACTIVE', 4),
(9, 'tts03@cmcuni.edu.vn', '$2a$12$uIY3CJXTaEkuiMqIhesO6ejgxazDdq8F58Qqo4tLSI0udaGfICCRW', 'Phạm Minh Quân', '0366999888', 'REJECTED', 4),
(10, 'tts04@cmcuni.edu.vn', '$2a$12$uIY3CJXTaEkuiMqIhesO6ejgxazDdq8F58Qqo4tLSI0udaGfICCRW', 'Nguyễn Thị Hằng', '0345678912', 'PENDING_APPROVAL', 4),
(11, 'tts05@cmcuni.edu.vn', '$2a$12$uIY3CJXTaEkuiMqIhesO6ejgxazDdq8F58Qqo4tLSI0udaGfICCRW', 'Lê Văn Bình', '0329988777', 'PENDING_APPROVAL', 4),
(12, 'tts06@cmcuni.edu.vn', '$2a$12$uIY3CJXTaEkuiMqIhesO6ejgxazDdq8F58Qqo4tLSI0udaGfICCRW', 'Trần Văn Đạt', '0377111222', 'PENDING_APPROVAL', 4),
(13, 'tts07@cmcuni.edu.vn', '$2a$12$uIY3CJXTaEkuiMqIhesO6ejgxazDdq8F58Qqo4tLSI0udaGfICCRW', 'Phan Bảo Ngọc', '0399222111', 'REJECTED', 4),
(14, 'tts08@cmcuni.edu.vn', '$2a$12$uIY3CJXTaEkuiMqIhesO6ejgxazDdq8F58Qqo4tLSI0udaGfICCRW', 'Hoàng Minh Đức', '0334455667', 'INACTIVE', 4),
(15, 'tts09@cmcuni.edu.vn', '$2a$12$uIY3CJXTaEkuiMqIhesO6ejgxazDdq8F58Qqo4tLSI0udaGfICCRW', 'Nguyễn Đức Anh', '0377123456', 'ACTIVE', 4),
(16, 'tts10@cmcuni.edu.vn', '$2a$12$uIY3CJXTaEkuiMqIhesO6ejgxazDdq8F58Qqo4tLSI0udaGfICCRW', 'Nguyễn Đức Hà', '0377123488', 'ACTIVE', 4),
(17, 'tts11@cmcuni.edu.vn', '$2a$12$uIY3CJXTaEkuiMqIhesO6ejgxazDdq8F58Qqo4tLSI0udaGfICCRW', 'Nguyễn Tuấn Bảo', '0357356787', 'INACTIVE', 4),
(18, 'tts12@cmcuni.edu.vn', '$2a$12$uIY3CJXTaEkuiMqIhesO6ejgxazDdq8F58Qqo4tLSI0udaGfICCRW', 'Phạm Gia Huy', '0127465497', 'PENDING_APPROVAL', 4),
(19, 'tts13@cmcuni.edu.vn', '$2a$12$uIY3CJXTaEkuiMqIhesO6ejgxazDdq8F58Qqo4tLSI0udaGfICCRW', 'Trần Minh Đức', '0477326788', 'ACTIVE', 4),
(20, 'tts14@cmcuni.edu.vn', '$2a$12$uIY3CJXTaEkuiMqIhesO6ejgxazDdq8F58Qqo4tLSI0udaGfICCRW', 'Lê Thị Anh', '0379296795', 'REJECTED', 4),
(21, 'tts15@cmcuni.edu.vn', '$2a$12$uIY3CJXTaEkuiMqIhesO6ejgxazDdq8F58Qqo4tLSI0udaGfICCRW', 'Phan Nguyên Chương', '0521163478', 'ACTIVE', 4);
INSERT INTO admin_users (admin_id, user_id, position) VALUES
(1, 1, 'Quản trị hệ thống chính');

INSERT INTO hr_users (hr_id, user_id, department) VALUES
(1, 2, 'Phòng Nhân sự'),
(2, 3, 'Phòng Tuyển dụng');

INSERT INTO mentor_users (mentor_id, user_id, department, expertise) VALUES
(1, 4, 'CNTT', 'Lập trình Web - ReactJS, Java Spring'),
(2, 5, 'Kinh doanh', 'Digital Marketing, SEO, Google Ads'),
(3, 6, 'Phân tích dữ liệu', 'Python, Power BI, SQL nâng cao');

INSERT INTO intern_users ( intern_id, user_id, school, major, gpa, dob, gender,address, intern_image_path, internship_application_path, cv_path, status, rejection_reason) VALUES
(1, 7, 'CMC University', 'Công nghệ thông tin', 3.2, '2003-01-15', 'MALE', 'Hà Đông, Hà Nội', 'img_nam.jpg', 'app_nam.pdf', 'cv_nam.pdf', 'APPROVED', NULL),
(2, 8, 'CMC University', 'Thiết kế đồ họa', 3.0, '2003-03-22', 'FEMALE', 'Hà Đông, Hà Nội', 'img_mai.jpg', 'app_mai.pdf', 'cv_mai.pdf', 'APPROVED', NULL),
(3, 9, 'CMC University', 'Công nghệ thông tin', 2.8, '2003-02-10', 'MALE', 'Hà Đông, Hà Nội', 'img_quan.jpg', 'app_quan.pdf', 'cv_quan.pdf', 'APPROVED', NULL),
(4, 10, 'CMC University', 'Kinh tế số', 3.1, '2002-12-05', 'FEMALE', 'Hà Đông, Hà Nội', 'img_hang.jpg', 'app_hang.pdf', 'cv_hang.pdf', 'PENDING', NULL),
(5, 11, 'CMC University', 'Phân tích dữ liệu', 3.5, '2003-05-19', 'MALE', 'Hà Đông, Hà Nội', 'img_binh.jpg', 'app_binh.pdf', 'cv_binh.pdf', 'APPROVED', NULL),
(6, 12, 'CMC University', 'Công nghệ thông tin', 3.4, '2003-04-08', 'FEMALE', 'Hà Đông, Hà Nội', 'img_dat.jpg', 'app_dat.pdf', 'cv_dat.pdf', 'APPROVED', NULL),
(7, 13, 'CMC University', 'Thiết kế đồ họa', 2.9, '2003-07-14', 'MALE', 'Hà Đông, Hà Nội', 'img_ngoc.jpg', 'app_ngoc.pdf', 'cv_ngoc.pdf', 'REJECTED', 'Thiếu hồ sơ bổ sung'),
(8, 14, 'CMC University', 'Phân tích dữ liệu', 3.3, '2003-09-23', 'FEMALE', 'Hà Đông, Hà Nội', 'img_duc.jpg', 'app_duc.pdf', 'cv_duc.pdf', 'NO_FILE', NULL),
(9, 15, 'CMC University', 'Công nghệ thông tin', 3.6, '2003-06-01', 'MALE', 'Hà Đông, Hà Nội', 'img_anh.jpg', 'app_anh.pdf', 'cv_anh.pdf', 'APPROVED', NULL),
(10, 16, 'CMC University', 'Khoa học máy tính', 3.2, '2003-08-01', 'MALE', 'Hà Đông, Hà Nội', 'img_ha.jpg', 'app_ha.pdf', 'cv_ha.pdf', 'APPROVED', NULL),
(11, 17, 'CMC University', 'Khoa học máy tính', 3.1, '2003-09-11', 'MALE', 'Hà Đông, Hà Nội', 'img_bao.jpg', 'app_bao.pdf', 'cv_bao.pdf', 'NO_FILE', NULL),
(12, 18, 'CMC University', 'An toàn thông tin', 2.7, '2003-07-07', 'MALE', 'Hà Đông, Hà Nội', 'img_huy.jpg', 'app_huy.pdf', 'cv_huy.pdf', 'REJECTED', 'Thiếu hồ sơ bổ sung'),
(13, 19, 'CMC University', 'Kỹ sư phần mềm', 3.7, '2003-02-21', 'MALE', 'Hà Đông, Hà Nội', 'img_duc.jpg', 'app_duc.pdf', 'cv_duc.pdf', 'PENDING', NULL),
(14, 20, 'CMC University', 'Hệ thống thông tin', 3.1, '2003-11-28', 'FEMALE', 'Hà Đông, Hà Nội', 'img_anh.jpg', 'app_anh.pdf', 'cv_anh.pdf', 'APPROVED', NULL),
(15, 21, 'CMC University', 'An toàn thông tin', 3.4, '2003-12-31', 'MALE', 'Hà Đông, Hà Nội', 'img_chuong.jpg', 'app_chuong.pdf', 'cv_chuong.pdf', 'APPROVED', NULL);


INSERT INTO contract_documents (document_id, intern_id, file_path, contract_status, intern_confirm_status, confirm_at, note) VALUES
(1, 1, 'contracts/nam_contract.pdf', 'UPLOAD', 'APPROVED', NOW(), 'Hợp đồng đã ký'),
(2, 2, 'contracts/mai_contract.pdf', 'UPLOAD', 'PENDING', NULL, 'Đang chờ xác nhận'),
(3, 3, 'contracts/quan_contract.pdf', 'NOT_UPLOAD', 'PENDING', NULL, NULL),
(4, 5, 'contracts/binh_contract.pdf', 'UPLOAD', 'APPROVED', NOW(), 'Hợp đồng đã ký'),
(5, 6, 'contracts/dat_contract.pdf', 'UPLOAD', 'APPROVED', NOW(), 'Hợp đồng đã ký'),
(6, 9, 'contracts/anh_contract.pdf', 'UPLOAD', 'APPROVED', NOW(), 'Hợp đồng đã ký'),
(7, 10, 'contracts/ha_contract.pdf', 'UPLOAD', 'APPROVED', NOW(), 'Hợp đồng đã ký');

INSERT INTO programs (program_id, name, department, start_date, end_date, program_status, max_interns) VALUES
(1, 'Thực tập lập trình Web', 'CNTT', '2025-01-10', '2025-04-10', 'ON_GOING', 10),
(2, 'Thực tập Digital Marketing', 'Kinh doanh', '2025-02-01', '2025-05-01', 'UPCOMING', 8),
(3, 'Thực tập phân tích dữ liệu', 'Data Science', '2025-03-01', '2025-06-01', 'UPCOMING', 6);

-- Gán mentor cho program
INSERT INTO mentor_program (mentor_program_id, program_id, mentor_id, assigned_date) VALUES
(1, 1, 1, NOW()),
(2, 2, 2, NOW()),
(3, 3, 3, NOW());

-- Tạo teams (thay thế intern_program)
INSERT INTO teams (team_id, program_id, mentor_id, assigned_date) VALUES
(1, 1, 1, NOW()), -- Team lập trình Web
(2, 2, 2, NOW()), -- Team Marketing
(3, 3, 3, NOW()); -- Team Data

-- Gán intern vào teams
INSERT INTO team_intern (team_intern_id, team_id, intern_id, assigned_date) VALUES
(1, 1, 1, NOW()), -- Nam - Team Web
(2, 1, 3, NOW()), -- Quân - Team Web
(3, 1, 6, NOW()), -- Đạt - Team Web
(4, 1, 9, NOW()), -- Anh - Team Web
(5, 2, 4, NOW()), -- Hằng - Team Marketing
(6, 2, 7, NOW()), -- Ngọc - Team Marketing
(7, 3, 5, NOW()), -- Bình - Team Data
(8, 3, 8, NOW()); -- Đức - Team Data

INSERT INTO tasks (program_id, title, description, assigned_by, priority, status, deadline, due_soon)
VALUES
(1, 'Xây dựng trang đăng nhập', 'Dùng React + Spring Boot', 1, 'HIGH', 'IN_PROGRESS', '2025-03-01', FALSE),
(1, 'Dashboard', 'Biểu đồ thống kê', 1, 'MEDIUM', 'TODO', '2025-03-10', FALSE),
(2, 'Landing Page', 'Thiết kế giao diện', 2, 'HIGH', 'DONE', '2025-02-25', FALSE);

INSERT INTO task_team_assignments (task_id, team_id)
VALUES
(1, 1),
(2, 1),
(3, 2);

INSERT INTO attendance (intern_id, date, check_in, check_out, location) VALUES
(1, '2025-02-10', '08:05:00', '17:00:00', 'CMC Tower Hà Đông'),
(3, '2025-02-10', '08:15:00', '17:05:00', 'CMC Tower Hà Đông'),
(5, '2025-02-10', '08:00:00', '17:10:00', 'CMC Tower Hà Đông'),
(6, '2025-02-10', '08:10:00', '17:00:00', 'CMC Tower Hà Đông'),
(7, '2025-02-10', '08:00:00', '16:55:00', 'CMC Tower Hà Đông'),
(8, '2025-02-10', '08:20:00', '17:00:00', 'CMC Tower Hà Đông');

INSERT INTO allowances (intern_id, type, amount, date_applied) VALUES
(1, 'MEAL', 50000, '2025-02-10'),
(3, 'TRANSPORT', 30000, '2025-02-10'),
(5, 'BONUS', 100000, '2025-02-15'),
(6, 'MEAL', 50000, '2025-02-11'),
(7, 'TRANSPORT', 30000, '2025-02-11');

INSERT INTO support_requests (intern_id, description, file_path, status, created_at) VALUES
(1, 'Không truy cập được hệ thống', NULL, 'OPEN', NOW()),
(2, 'Yêu cầu sửa thông tin', NULL, 'IN_PROGRESS', NOW()),
(5, 'Thắc mắc về phụ cấp tháng 2',NULL, 'RESOLVED', NOW()),
(7, 'Xin nghỉ phép 1 ngày',NULL, 'OPEN', NOW());

INSERT INTO evaluations (intern_id, mentor_id, technical, communication, discipline, attitude, note) VALUES
(1, 1, 9, 8, 9, 9, 'Thực tập sinh chăm chỉ, hoàn thành tốt công việc'),
(3, 1, 8, 8, 9, 8, 'Tốt, cần cải thiện tốc độ xử lý'),
(5, 3, 9, 9, 8, 9, 'Hiểu dữ liệu tốt, cần luyện kỹ năng trình bày'),
(7, 2, 8, 8, 8, 9, 'Thực hiện đầy đủ yêu cầu của mentor');

INSERT INTO admin_logs (details) VALUES
(' Admin tạo HR01 với role HR'),
(' Admin tạo HR02 với role HR'),
(' Admin cập nhật thông tin mentor Đặng Minh Tâm'),
(' Admin xóa thực tập sinh Nguyễn Đức Anh');
INSERT INTO hr_logs (hr_id, details) VALUES
(1, 'HR duyệt hồ sơ intern Nguyễn Hữu Nam'),
(1, 'HR upload hợp đồng cho intern Trần Thị Mai'),
(2, 'HR gửi email thông báo hợp đồng cho intern Phạm Minh Quân'),
(2, 'HR duyệt hồ sơ intern Nguyễn Thị Hằng'),
(1, 'HR upload hợp đồng cho intern Lê Văn Bình');
INSERT INTO intern_logs (intern_id, details) VALUES
(1, ' Intern xác nhận hợp đồng'),
(2, ' Intern upload CV bổ sung'),
(3, ' Intern cập nhật số điện thoại'),
(4, ' Intern gửi yêu cầu hỗ trợ về hợp đồng'),
(5, ' Intern xác nhận hợp đồng');
select * from users;
