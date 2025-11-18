-- ======================================
--Trigger cho tự động cập nhật status của file

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
-- Trước khi insert
-- ======================================
-- Sau khi update
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