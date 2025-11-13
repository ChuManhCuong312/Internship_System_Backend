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
(15, 'tts09@cmcuni.edu.vn', '$2a$12$uIY3CJXTaEkuiMqIhesO6ejgxazDdq8F58Qqo4tLSI0udaGfICCRW', 'Nguyễn Đức Anh', '0377123456', 'ACTIVE', 4);

INSERT INTO admin_users (admin_id, user_id, position) VALUES
(1, 1, 'Quản trị hệ thống chính');

INSERT INTO hr_users (hr_id, user_id, department) VALUES
(1, 2, 'Phòng Nhân sự'),
(2, 3, 'Phòng Tuyển dụng');

INSERT INTO mentor_users (mentor_id, user_id, department, expertise) VALUES
(1, 4, 'CNTT', 'Lập trình Web - ReactJS, Java Spring'),
(2, 5, 'Kinh doanh', 'Digital Marketing, SEO, Google Ads'),
(3, 6, 'Phân tích dữ liệu', 'Python, Power BI, SQL nâng cao');

INSERT INTO intern_users (intern_id, user_id, school, major, dob, address, cv_path, status) VALUES
(1, 7, 'CMC University', 'Công nghệ thông tin', '2003-01-15', 'Hà Đông, Hà Nội', 'cv_nam.pdf', 'APPROVED'),
(2, 8, 'CMC University', 'Thiết kế đồ họa', '2003-03-22', 'Hà Đông, Hà Nội', 'cv_mai.pdf', 'APPROVED'),
(3, 9, 'CMC University', 'Công nghệ thông tin', '2003-02-10', 'Hà Đông, Hà Nội', 'cv_quan.pdf', 'APPROVED'),
(4, 10, 'CMC University', 'Kinh tế số', '2002-12-05', 'Hà Đông, Hà Nội', 'cv_hang.pdf', 'PENDING'),
(5, 11, 'CMC University', 'Phân tích dữ liệu', '2003-05-19', 'Hà Đông, Hà Nội', 'cv_binh.pdf', 'APPROVED'),
(6, 12, 'CMC University', 'Công nghệ thông tin', '2003-04-08', 'Hà Đông, Hà Nội', 'cv_dat.pdf', 'ACTIVE'),
(7, 13, 'CMC University', 'Thiết kế đồ họa', '2003-07-14', 'Hà Đông, Hà Nội', 'cv_ngoc.pdf', 'ACTIVE'),
(8, 14, 'CMC University', 'Phân tích dữ liệu', '2003-09-23', 'Hà Đông, Hà Nội', 'cv_duc.pdf', 'ACTIVE'),
(9, 15, 'CMC University', 'Công nghệ thông tin', '2003-06-01', 'Hà Đông, Hà Nội', 'cv_anh.pdf', 'ACTIVE');

INSERT INTO programs (program_id, name, department, start_date, end_date, max_interns, created_by) VALUES
(1, 'Thực tập lập trình Web', 'CNTT', '2025-01-10', '2025-04-10', 10, 1),
(2, 'Thực tập Digital Marketing', 'Kinh doanh', '2025-02-01', '2025-05-01', 8, 2),
(3, 'Thực tập phân tích dữ liệu', 'Data Science', '2025-03-01', '2025-06-01', 6, 1);

INSERT INTO intern_program (program_id, intern_id, assigned_date) VALUES
(1, 1, NOW()), (1, 3, NOW()), (1, 6, NOW()), (1, 9, NOW()),
(2, 4, NOW()), (2, 7, NOW()),
(3, 5, NOW()), (3, 8, NOW());

INSERT INTO tasks (task_id, title, description, mentor_id, intern_id, priority, status, deadline) VALUES
(1, 'Xây dựng trang đăng nhập', 'Tạo giao diện đăng nhập ReactJS và API Spring Boot', 1, 1, 'HIGH', 'IN_PROGRESS', '2025-03-01'),
(2, 'Tạo bảng dashboard', 'Hiển thị thống kê bằng biểu đồ', 1, 3, 'MEDIUM', 'TODO', '2025-03-10'),
(3, 'Thiết kế landing page', 'Dựng giao diện quảng bá chương trình thực tập', 2, 7, 'HIGH', 'DONE', '2025-02-25'),
(4, 'Viết báo cáo SEO tháng 2', 'Tổng hợp từ khóa, lượng truy cập website', 2, 4, 'MEDIUM', 'DONE', '2025-02-28'),
(5, 'Phân tích dữ liệu điểm danh', 'Tạo biểu đồ trực quan trên Power BI', 3, 5, 'HIGH', 'IN_PROGRESS', '2025-03-15');

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

INSERT INTO support_requests (intern_id, type, description, status, created_at) VALUES
(1, 'TECHNICAL', 'Không thể truy cập hệ thống task', 'OPEN', NOW()),
(3, 'ADMIN', 'Sai tên trong hệ thống', 'IN_PROGRESS', NOW()),
(5, 'HR', 'Thắc mắc về phụ cấp tháng 2', 'RESOLVED', NOW()),
(7, 'OTHER', 'Xin nghỉ phép 1 ngày', 'OPEN', NOW());

INSERT INTO evaluations (intern_id, mentor_id, technical, communication, discipline, attitude, note) VALUES
(1, 1, 9, 8, 9, 9, 'Thực tập sinh chăm chỉ, hoàn thành tốt công việc'),
(3, 1, 8, 8, 9, 8, 'Tốt, cần cải thiện tốc độ xử lý'),
(5, 3, 9, 9, 8, 9, 'Hiểu dữ liệu tốt, cần luyện kỹ năng trình bày'),
(7, 2, 8, 8, 8, 9, 'Thực hiện đầy đủ yêu cầu của mentor');

INSERT INTO documents (document_id, intern_id, type, file_path, status, reviewed_by, reviewed_at, review_note) VALUES
(1, 1, 'CV', 'uploads/cv_nam.pdf', 'APPROVED', 1, NOW(), 'CV đạt yêu cầu'),
(2, 3, 'CV', 'uploads/cv_quan.pdf', 'APPROVED', 2, NOW(), 'Đủ điều kiện'),
(3, 5, 'CV', 'uploads/cv_binh.pdf', 'APPROVED', 1, NOW(), 'Tốt'),
(4, 4, 'CV', 'uploads/cv_hang.pdf', 'PENDING', NULL, NULL, NULL);

INSERT INTO audit_logs (user_id, action, ip_address) VALUES
(1, 'Tạo chương trình thực tập Web', '192.168.1.10'),
(2, 'Phê duyệt CV của Nguyễn Hữu Nam', '192.168.1.11'),
(4, 'Thêm task mới cho thực tập sinh', '192.168.1.12'),
(7, 'Đăng nhập hệ thống', '192.168.1.13'),
(9, 'Nộp báo cáo tháng 2', '192.168.1.14');
