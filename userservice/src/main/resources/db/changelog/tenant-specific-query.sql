-- Create `tenant_users` table
CREATE TABLE IF NOT EXISTS `tenant_users` (
    id BIGINT NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    status ENUM('ACTIVE', 'INACTIVE', 'PENDING') NOT NULL DEFAULT 'PENDING',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    password_hash VARCHAR(100) NOT NULL,
    sso TINYINT NULL DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE KEY (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Create `tenant_user_roles` table
CREATE TABLE IF NOT EXISTS `tenant_user_roles` (
    id BIGINT NOT NULL AUTO_INCREMENT,
    role_name VARCHAR(50) NOT NULL,
    role_desc VARCHAR(255) DEFAULT NULL,
    status TINYINT NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY (role_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT IGNORE INTO `tenant_user_roles` (`role_name`, `role_desc`)
VALUES ('TENANT_ADMIN', 'Manages specific tenant and its users and roles assigned to users');

INSERT IGNORE INTO `tenant_user_roles` (`role_name`, `role_desc`)
VALUES ('USER', 'Basic user of the tenant ');

CREATE TABLE IF NOT EXISTS `tenant_user_roles_map` (
	`id` BIGINT NOT NULL AUTO_INCREMENT,
	`user_id` BIGINT NOT NULL DEFAULT (0),
	`role_id` BIGINT NULL DEFAULT NULL,
	`assigned_at` DATETIME NULL DEFAULT NULL,
	PRIMARY KEY (`id`),
	CONSTRAINT `FK__tenant_users` FOREIGN KEY (`user_id`) REFERENCES `tenant_users` (`id`) ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT `FK__tenant_user_roles` FOREIGN KEY (`role_id`) REFERENCES `tenant_user_roles` (`id`) ON UPDATE NO ACTION ON DELETE NO ACTION
)
COLLATE='utf8mb4_0900_ai_ci'
;


