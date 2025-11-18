-- TRIGGER TỰ ĐỘNG CẬP NHẬT STATUS CHO INTERN

DELIMITER $$

CREATE TRIGGER update_intern_status
BEFORE INSERT ON intern_users
FOR EACH ROW
BEGIN
    IF (NEW.cv_path IS NULL OR NEW.cv_path = '' OR NEW.internship_application_path IS NULL OR NEW.internship_application_path = '') THEN
        SET NEW.status = 'NO_FILE';
    ELSE
        SET NEW.status = 'PENDING';
    END IF;
END$$

DELIMITER ;

-- SAU KHI UPDATE
DELIMITER $$

CREATE TRIGGER update_intern_status_on_update
BEFORE UPDATE ON intern_users
FOR EACH ROW
BEGIN
    IF (NEW.cv_path IS NULL OR NEW.cv_path = '' OR NEW.internship_application_path IS NULL OR NEW.internship_application_path = '') THEN
        SET NEW.status = 'NO_FILE';
    ELSE
        SET NEW.status = 'PENDING';
    END IF;
END$$

DELIMITER ;

-- TRIGGER TỰ ĐỘNG TẠO PROFILE INTERN KHI CÓ ĐĂNG KÍ USER MỚI

DELIMITER $$

CREATE TRIGGER after_user_insert_create_intern
AFTER INSERT ON users
FOR EACH ROW
BEGIN
    -- Check if the new user has role_id = 4
    IF NEW.role_id = 4 THEN
        -- Insert a corresponding record in intern_users
        INSERT INTO intern_users (user_id, gpa)
        VALUES (NEW.user_id, 0.1);
    END IF;
END$$

DELIMITER ;
