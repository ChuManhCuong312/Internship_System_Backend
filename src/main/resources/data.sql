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
(9, 'tts03@cmcuni.edu.vn', '$2a$12$uIY3CJXTaEkuiMqIhesO6ejgxazDdq8F58Qqo4tLSI0udaGfICCRW', 'Phạm Minh Quân', '0366999888', 'ACTIVE', 4),
(10, 'tts04@cmcuni.edu.vn', '$2a$12$uIY3CJXTaEkuiMqIhesO6ejgxazDdq8F58Qqo4tLSI0udaGfICCRW', 'Nguyễn Thị Hằng', '0345678912', 'ACTIVE', 4),
(11, 'tts05@cmcuni.edu.vn', '$2a$12$uIY3CJXTaEkuiMqIhesO6ejgxazDdq8F58Qqo4tLSI0udaGfICCRW', 'Lê Văn Bình', '0329988777', 'ACTIVE', 4),
(12, 'tts06@cmcuni.edu.vn', '$2a$12$uIY3CJXTaEkuiMqIhesO6ejgxazDdq8F58Qqo4tLSI0udaGfICCRW', 'Trần Văn Đạt', '0377111222', 'ACTIVE', 4),
(13, 'tts07@cmcuni.edu.vn', '$2a$12$uIY3CJXTaEkuiMqIhesO6ejgxazDdq8F58Qqo4tLSI0udaGfICCRW', 'Phan Bảo Ngọc', '0399222111', 'ACTIVE', 4),
(14, 'tts08@cmcuni.edu.vn', '$2a$12$uIY3CJXTaEkuiMqIhesO6ejgxazDdq8F58Qqo4tLSI0udaGfICCRW', 'Hoàng Minh Đức', '0334455667', 'ACTIVE', 4),
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

INSERT INTO intern_users (
    intern_id, user_id, school, major, gpa, dob, gender, address,
    intern_image_path, university_confirm, internship_application_path,
    cv_path, status, rejection_reason
) VALUES

-- 1. Nam
(1, 7, 'CMC University', 'Công nghệ thông tin', 3.2, '2003-01-15', 'MALE', 'Hà Đông, Hà Nội',
 'https://res.cloudinary.com/dbudb7i8v/image/upload/v1763990715/Screenshot_2025-11-24_202449_m8yxc7.png',
 'https://res.cloudinary.com/dbudb7i8v/raw/upload/v1763745941/Nam_uni_confirm_g72nf8.docx',
 'https://res.cloudinary.com/dbudb7i8v/raw/upload/v1763745146/Nam_app_zixvmm.docx',
 'https://res.cloudinary.com/dbudb7i8v/image/upload/v1763740806/Nguy%E1%BB%85n_H%E1%BB%AFu_Nam_CV_zvej8x.pdf',
 'APPROVED', NULL),

-- 2. Mai
(2, 8, 'CMC University', 'Thiết kế đồ họa', 3.0, '2003-03-22', 'FEMALE', 'Hà Đông, Hà Nội',
 'https://res.cloudinary.com/dbudb7i8v/image/upload/v1763991165/Screenshot_2025-11-24_202719_zqivt7.png',
 'https://res.cloudinary.com/dbudb7i8v/raw/upload/v1763745936/Mai_uni_confirm_arnooq.docx',
 'https://res.cloudinary.com/dbudb7i8v/raw/upload/v1763745145/Mai_app_gwrcxp.docx',
 'https://res.cloudinary.com/dbudb7i8v/image/upload/v1763740803/Tr%E1%BA%A7n_Th%E1%BB%8B_Mai_CV_vz0om9.pdf',
 'APPROVED', NULL),

-- 3. Quân
(3, 9, 'CMC University', 'Công nghệ thông tin', 2.8, '2003-02-10', 'MALE', 'Hà Đông, Hà Nội',
 'img_quan.jpg',
 NULL,
 'app_quan.pdf',
 'cv_quan.pdf',
 'APPROVED', NULL),

-- 4. Hằng
(4, 10, 'CMC University', 'Kinh tế số', 3.1, '2002-12-05', 'FEMALE', 'Hà Đông, Hà Nội',
 'https://res.cloudinary.com/dbudb7i8v/image/upload/v1763991165/Screenshot_2025-11-24_202821_sgsic8.png',
 'https://res.cloudinary.com/dbudb7i8v/raw/upload/v1763745936/Hang_uni_confirm_rgoecx.docx',
 'https://res.cloudinary.com/dbudb7i8v/raw/upload/v1763745145/Hang_app_kxxpdc.docx',
 'https://res.cloudinary.com/dbudb7i8v/image/upload/v1763740805/Nguy%E1%BB%85n_Th%E1%BB%8B_H%E1%BA%B1ng_CV_yiijtb.pdf',
 'APPROVED', NULL),

-- 5. Bình
(5, 11, 'CMC University', 'Phân tích dữ liệu', 3.5, '2003-05-19', 'MALE', 'Hà Đông, Hà Nội',
 'https://res.cloudinary.com/dbudb7i8v/image/upload/v1763991165/Screenshot_2025-11-24_202904_gzuvua.png',
 'https://res.cloudinary.com/dbudb7i8v/raw/upload/v1763745937/Binh_uni_confirm_emu4xq.docx',
 'https://res.cloudinary.com/dbudb7i8v/raw/upload/v1763745145/Binh_app_k4rpyg.docx',
 'https://res.cloudinary.com/dbudb7i8v/image/upload/v1763740805/L%C3%AA_V%C4%83n_B%C3%ACnh_CV_iqdzzv.pdf',
 'APPROVED', NULL),

-- 6. Đạt
(6, 12, 'CMC University', 'Công nghệ thông tin', 3.4, '2003-04-08', 'FEMALE', 'Hà Đông, Hà Nội',
 'https://res.cloudinary.com/dbudb7i8v/image/upload/v1763991165/Screenshot_2025-11-24_202942_qpzoma.png',
 'https://res.cloudinary.com/dbudb7i8v/raw/upload/v1763745937/Dat_uni_confirm_zdm6je.docx',
 'https://res.cloudinary.com/dbudb7i8v/raw/upload/v1763745145/Dat_app_vsopvd.docx',
 'https://res.cloudinary.com/dbudb7i8v/image/upload/v1763740804/Tr%E1%BA%A7n_V%C4%83n_%C4%90%E1%BA%A1t_CV_pct3ha.pdf',
 'APPROVED', NULL),

-- 7. Ngọc
(7, 13, 'CMC University', 'Thiết kế đồ họa', 2.9, '2003-07-14', 'MALE', 'Hà Đông, Hà Nội',
 'img_ngoc.jpg',
 NULL,
 'app_ngoc.pdf',
 'cv_ngoc.pdf',
 'REJECTED', 'Thiếu hồ sơ bổ sung'),

-- 8. Đức (NO_FILE)
(8, 14, 'CMC University', 'Phân tích dữ liệu', 3.3, '2003-09-23', 'MALE', 'Hà Đông, Hà Nội',
 'img_duc.jpg',
 NULL,
 NULL,
 NULL,
 'NO_FILE', NULL),

-- 9. Đức Anh
(9, 15, 'CMC University', 'Công nghệ thông tin', 3.6, '2003-06-01', 'MALE', 'Hà Đông, Hà Nội',
 'https://res.cloudinary.com/dbudb7i8v/image/upload/v1763991165/Screenshot_2025-11-24_203027_nzl39r.png',
 'https://res.cloudinary.com/dbudb7i8v/raw/upload/v1763745937/Anh_uni_confirm_ybbken.docx',
 'https://res.cloudinary.com/dbudb7i8v/raw/upload/v1763745145/DucAnh_app_qdvrwv.docx',
 'https://res.cloudinary.com/dbudb7i8v/image/upload/v1763740804/Nguy%E1%BB%85n_%C4%90%E1%BB%A9c_Anh_CV_q9h85g.pdf',
 'APPROVED', NULL),

-- 10. Hà
(10, 16, 'CMC University', 'Khoa học máy tính', 3.2, '2003-08-01', 'MALE', 'Hà Đông, Hà Nội',
 'https://res.cloudinary.com/dbudb7i8v/image/upload/v1763991165/Screenshot_2025-11-24_203053_e9fnnw.png',
 'https://res.cloudinary.com/dbudb7i8v/raw/upload/v1763745937/Ha_uni_confirm_n21ari.docx',
 'https://res.cloudinary.com/dbudb7i8v/raw/upload/v1763745146/Ha_app_jmz8rg.docx',
 'https://res.cloudinary.com/dbudb7i8v/image/upload/v1763740804/Nguy%E1%BB%85n_%C4%90%E1%BB%A9c_H%C3%A0_CV_kbu7di.pdf',
 'APPROVED', NULL),

-- 11. Đức (pending)
(11, 19, 'CMC University', 'Kỹ sư phần mềm', 3.7, '2003-02-21', 'MALE', 'Hà Đông, Hà Nội',
 'img_duc.jpg',
 'https://res.cloudinary.com/dbudb7i8v/raw/upload/v1763745938/Duc_uni_confirm_dgfc8z.docx',
 'https://res.cloudinary.com/dbudb7i8v/raw/upload/v1763745146/Duc_app_pvihuj.docx',
 'https://res.cloudinary.com/dbudb7i8v/image/upload/v1763740808/Tr%E1%BA%A7n_Minh_%C4%90%E1%BB%A9c_CV_oktbj6.pdf',
 'PENDING', NULL),

-- 12. Chương PENDING
(12, 21, 'CMC University', 'An toàn thông tin', 3.4, '2003-12-31', 'MALE', 'Hà Đông, Hà Nội',
 'https://res.cloudinary.com/dbudb7i8v/image/upload/v1763991166/Screenshot_2025-11-24_203214_tlboai.png',
 'https://res.cloudinary.com/dbudb7i8v/raw/upload/v1763745941/Chuong_uni_confirm_zh4pok.docx',
 'https://res.cloudinary.com/dbudb7i8v/raw/upload/v1763745146/Chuong_app_mjkbqi.docx',
 'https://res.cloudinary.com/dbudb7i8v/image/upload/v1763740803/Phan_Nguy%C3%AAn_Ch%C6%B0%C6%A1ng_CV_ppgelx.pdf',
 'PENDING', NULL);



INSERT INTO contract_documents
(document_id, intern_id, file_path, contract_status, intern_confirm_status, confirm_at, note)
VALUES
-- 1) tts01 - APPROVED
(1, 1, 'https://res.cloudinary.com/dbudb7i8v/image/upload/v1764096329/H%E1%BB%A3p_%C4%91%E1%BB%93ng_th%E1%BB%B1c_t%E1%BA%ADp_c%E1%BB%A7a_Nam_si6y5e.pdf',
 'UPLOAD', 'APPROVED', NOW(), 'Hợp đồng đã ký và xác nhận'),
-- 2) tts02 - APPROVED
(2, 2, 'https://res.cloudinary.com/dbudb7i8v/image/upload/v1764096329/H%E1%BB%A3p_%C4%91%E1%BB%93ng_th%E1%BB%B1c_t%E1%BA%ADp_c%E1%BB%A7a_Mai_tujgx1.pdf',
 'UPLOAD', 'APPROVED', NOW(), 'Hợp đồng đã ký và xác nhận'),
-- 3) tts03 - APPROVED
(3, 3, 'https://res.cloudinary.com/dbudb7i8v/image/upload/v1764096329/H%E1%BB%A3p_%C4%91%E1%BB%93ng_th%E1%BB%B1c_t%E1%BA%ADp_c%E1%BB%A7a_Qu%C3%A2n_omzuki.pdf',
 'UPLOAD', 'APPROVED', NOW(), 'Hợp đồng đã ký và xác nhận'),
-- 4) tts04 - APPROVED
(4, 4, 'https://res.cloudinary.com/dbudb7i8v/image/upload/v1764096329/H%E1%BB%A3p_%C4%91%E1%BB%93ng_th%E1%BB%B1c_t%E1%BA%ADp_c%E1%BB%A7a_H%E1%BA%B1ng_erlcsy.pdf',
 'UPLOAD', 'APPROVED', NOW(), 'Hợp đồng đã ký và xác nhận'),
-- 5) tts05 - APPROVED
(5, 5, 'https://res.cloudinary.com/dbudb7i8v/image/upload/v1764096328/H%E1%BB%A3p_%C4%91%E1%BB%93ng_th%E1%BB%B1c_t%E1%BA%ADp_c%E1%BB%A7a_B%C3%ACnh_yk5vxg.pdf',
 'UPLOAD', 'APPROVED', NOW(), 'Hợp đồng đã ký và xác nhận'),
-- 6) tts06 - APPROVED
(6, 6, 'https://res.cloudinary.com/dbudb7i8v/image/upload/v1764096328/H%E1%BB%A3p_%C4%91%E1%BB%93ng_th%E1%BB%B1c_t%E1%BA%ADp_c%E1%BB%A7a_%C4%90%E1%BA%A1t_ka5avg.pdf',
 'UPLOAD', 'APPROVED', NOW(), 'Hợp đồng đã ký và xác nhận'),
-- 7) tts07 - APPROVED
(7, 7, 'https://res.cloudinary.com/dbudb7i8v/image/upload/v1764096328/H%E1%BB%A3p_%C4%91%E1%BB%93ng_th%E1%BB%B1c_t%E1%BA%ADp_c%E1%BB%A7a_Ng%E1%BB%8Dc_ybmljg.pdf',
 'UPLOAD', 'APPROVED', NOW(), 'Hợp đồng đã ký và xác nhận'),
-- 8) tts08 - APPROVED
(8, 8, 'https://res.cloudinary.com/dbudb7i8v/image/upload/v1764096328/H%E1%BB%A3p_%C4%91%E1%BB%93ng_th%E1%BB%B1c_t%E1%BA%ADp_c%E1%BB%A7a_%C4%90%E1%BB%A9c_tscjgi.pdf',
 'UPLOAD', 'APPROVED', NOW(), 'Hợp đồng đã ký và xác nhận'),
-- 9) tts09 - APPROVED
(9, 9, 'https://res.cloudinary.com/dbudb7i8v/image/upload/v1764096328/H%E1%BB%A3p_%C4%91%E1%BB%93ng_th%E1%BB%B1c_t%E1%BA%ADp_c%E1%BB%A7a_%C4%90%E1%BB%A9c_Anh_kkha5g.pdf',
 'UPLOAD', 'PENDING', NOW(), 'Hợp đồng đã ký và xác nhận'),
-- 10) tts10 - PENDING
(10, 10, 'https://res.cloudinary.com/dbudb7i8v/image/upload/v1764096328/H%E1%BB%A3p_%C4%91%E1%BB%93ng_th%E1%BB%B1c_t%E1%BA%ADp_c%E1%BB%A7a_H%C3%A0_oqpxmo.pdf',
 'NOT_UPLOAD', 'APPROVED',NOW(), 'Hợp đồng đã ký và xác nhận');


INSERT INTO programs (program_id, name, department, start_date, end_date, program_status, max_interns) VALUES
(1, 'Thực tập lập trình Web', 'CNTT', '2025-09-25', '2025-12-25', 'ON_GOING', 10),
(2, 'Thực tập Digital Marketing', 'Kinh doanh', '2025-11-25', '2026-01-25', 'ON_GOING', 8),
(3, 'Thực tập phân tích dữ liệu', 'Data Science', '2025-12-25', '2026-01-25', 'UPCOMING', 6);

-- Gán mentor cho program
INSERT INTO mentor_program (mentor_program_id, program_id, mentor_id, assigned_date) VALUES
(1, 1, 1, NOW()),
(2, 2, 1, NOW()),
(3, 3, 3, NOW());

-- Tạo teams (thay thế intern_program)
INSERT INTO teams (team_id, program_id, mentor_id, assigned_date) VALUES
(1, 1, 1, NOW()), -- Team lập trình Web
(2, 2, 2, NOW()), -- Team Marketing
(3, 1, 1, NOW()), -- Team lập trình Web
(4, 2, 2, NOW()), -- Team Marketing
(5, 1, 1, NOW()), -- Team lập trình Web
(6, 3, 3, NOW()); -- Team Data

-- Gán intern vào teams
INSERT INTO team_intern (team_intern_id, team_id, intern_id, assigned_date) VALUES
(1, 1, 1, NOW()), -- Nam - Team Web
(2, 1, 3, NOW()), -- Quân - Team Web
(3, 1, 6, NOW()), -- Đạt - Team Web
(4, 1, 4, NOW()), -- Hằng - Team Web
(5, 2, 2, NOW()), -- Mai - Team Marketing
(6, 2, 7, NOW()), -- Ngọc - Team Marketing
(7, 3, 5, NOW()), -- Bình - Team Data
(8, 3, 8, NOW()); -- Đức - Team Data

INSERT INTO tasks (program_id, title, description, assigned_by, priority, status, deadline, due_soon)
VALUES
(1, 'Xây dựng trang đăng nhập', 'Dùng React + Spring Boot với validation và JWT authentication', 1, 'HIGH', 'IN_PROGRESS', '2025-12-25', FALSE),
(1, 'Dashboard', 'Biểu đồ thống kê tương tác, dữ liệu real-time từ API', 1, 'MEDIUM', 'TODO', '2025-12-28', FALSE),
(1, 'Landing Page', 'Thiết kế giao diện responsive cho desktop, tablet, mobile', 2, 'HIGH', 'DONE', '2025-12-20', FALSE),
(2, 'Phân tích thị trường Facebook Ads', 'Phân tích chi phí, hiệu suất, ROI của các chiến dịch', 2, 'HIGH', 'IN_PROGRESS', '2025-12-27', FALSE),
(2, 'SEO optimization cho website', 'Tối ưu từ khóa, meta tags, backlinks, sitemap', 2, 'MEDIUM', 'TODO', '2026-01-05', FALSE),
(3, 'Data cleaning và preprocessing', 'Xử lý missing values, outliers, normalize data từ CSV', 3, 'MEDIUM', 'IN_PROGRESS', '2025-12-26', FALSE),
(3, 'Power BI Dashboard', 'Tạo dashboard báo cáo bán hàng theo category và region', 3, 'HIGH', 'TODO', '2026-01-08', FALSE);

INSERT INTO task_team_assignments (task_id, team_id)
VALUES
(1, 1), -- Task 1 to Team Web
(2, 1), -- Task 2 to Team Web
(3, 2), -- Task 3 to Team Marketing
(4, 2), -- Task 4 to Team Marketing
(5, 2), -- Task 5 to Team Marketing
(6, 3), -- Task 6 to Team Data
(7, 3); -- Task 7 to Team Data

INSERT INTO task_progress (task_id, percent_complete, note, updated_at)
VALUES
-- Task 1 (Xây dựng trang đăng nhập) - IN_PROGRESS: 60%
(1, 20, 'Bắt đầu làm: phân tích yêu cầu và thiết kế database', '2025-12-10 09:00:00'),
(1, 40, 'Hoàn thành backend API authentication, còn frontend login form', '2025-12-12 14:30:00'),
(1, 60, 'Giao diện login hoàn thành, đang test validation và error handling', '2025-12-15 10:15:00'),

-- Task 2 (Dashboard) - TODO: 0%
(2, 0, 'Chưa bắt đầu, chờ hoàn thành task 1', '2025-12-10 08:00:00'),

-- Task 3 (Landing Page) - DONE: 100%
(3, 30, 'Hoàn thành header, hero section và features section', '2025-11-20 11:00:00'),
(3, 70, 'Thêm footer, blog section, testimonials, responsive CSS', '2025-11-22 15:30:00'),
(3, 100, 'Landing page hoàn thành, test responsive trên desktop/tablet/mobile, deploy lên staging', '2025-12-20 16:45:00'),

-- Task 4 (Phân tích Facebook Ads) - IN_PROGRESS: 50%
(4, 25, 'Tình lên dữ liệu từ 10 chiến dịch, phân tích cost per click', '2025-12-11 09:30:00'),
(4, 50, 'Tính toán ROI, conversion rate, so sánh các chiến dịch, chuẩn bị báo cáo', '2025-12-14 13:00:00'),

-- Task 5 (SEO optimization) - TODO: 0%
(5, 0, 'Chưa bắt đầu, lên lịch cho tuần sau', '2025-12-10 08:00:00'),

-- Task 6 (Data cleaning) - IN_PROGRESS: 35%
(6, 15, 'Import CSV vào Python, kiểm tra structure và data types', '2025-12-12 08:30:00'),
(6, 35, 'Xử lý missing values (drop/fill), detect và loại bỏ outliers', '2025-12-14 11:00:00'),

-- Task 7 (Power BI Dashboard) - TODO: 0%
(7, 0, 'Chờ task 6 hoàn thành để lấy dữ liệu clean', '2025-12-10 08:00:00');

INSERT INTO attendance (intern_id, date, check_in, check_out) VALUES
(1, '2025-02-10', '08:05:00', '17:00:00'),
(2, '2025-02-10', '08:05:00', '17:00:00'),
(3, '2025-02-10', '08:15:00', '17:05:00'),
(4, '2025-02-10', '08:05:00', '17:00:00'),
(5, '2025-02-10', '08:00:00', '17:10:00'),
(6, '2025-02-10', '08:10:00', '17:00:00'),
(7, '2025-02-10', '08:00:00', '16:55:00'),
(8, '2025-02-10', '08:20:00', '17:00:00');

INSERT INTO leave_requests (intern_id, start_date, end_date, reason, status, rejection_reason, processed_by)VALUES
(1, '2025-12-01', '2025-12-03','Nghỉ phép về quê thăm gia đình', 'APPROVED', NULL,  1),
(1, '2025-12-05', '2025-12-07','Bị ốm, cần nghỉ ngơi', 'REJECTED', 'Không đủ giấy tờ chứng minh',  1),
(1, '2025-12-10', '2025-12-10','Có việc cá nhân gấp', 'PENDING', NULL,  2);

INSERT INTO allowances (intern_id, type, amount, date_applied,note) VALUES
(1, 'Phụ cấp bữa ăn', 50000, '2025-02-10','Phụ cấp bữa ăn'),
(3, 'Phụ cấp đi lại', 30000, '2025-02-10','Phụ cấp đi lại'),
(5, 'Thưởng', 100000, '2025-02-15','Thưởng thêm'),
(6, 'Phụ cấp bữa ăn', 50000, '2025-02-11','Phụ cấp bữa ăn'),
(7, 'Phụ cấp đi lại', 30000, '2025-02-11','Phụ cấp đi lại');

INSERT INTO support_requests (intern_id, description, file_path, status, created_at) VALUES
(1, 'Không truy cập được hệ thống', NULL, 'OPEN', NOW()),
(2, 'Yêu cầu sửa thông tin', NULL, 'IN_PROGRESS', NOW()),
(5, 'Thắc mắc về phụ cấp tháng 2',NULL, 'RESOLVED', NOW()),
(7, 'Xin nghỉ phép 1 ngày',NULL, 'OPEN', NOW());

INSERT INTO evaluations
(intern_id, mentor_evaluate_id, title, technical, communication, discipline, attitude, weight, note)
VALUES
(1, 1, 'Đánh giá cuối kì', 9, 8, 9, 9, 50, 'Thực tập sinh chăm chỉ, hoàn thành tốt công việc'),
(3, 1, 'Đánh giá cuối kì', 8, 8, 9, 8, 50, 'Tốt, cần cải thiện tốc độ xử lý'),
(5, 3, 'Đánh giá cuối kì', 9, 9, 8, 9, 50, 'Hiểu dữ liệu tốt, cần luyện kỹ năng trình bày'),
(7, 2, 'Đánh giá cuối kì', 8, 8, 8, 9, 50, 'Thực hiện đầy đủ yêu cầu của mentor'),
(1, 1, 'Đánh giá giữa kì', 6, 8, 3, 7, 25, 'Thực tập sinh chăm chỉ, hoàn thành tốt '),
(3, 1, 'Đánh giá giữa kì', 8, 6, 9, 7, 25, 'Thực tập sinh chăm chỉ, hoàn thành tốt'),
(5, 3, 'Đánh giá giữa kì', 9, 7, 8, 8, 25, 'Thực tập sinh chăm chỉ, hoàn thành tốt'),
(7, 2, 'Đánh giá giữa kì', 5, 8, 7, 6, 25, 'Thực tập sinh chăm chỉ, hoàn thành tốt');

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
