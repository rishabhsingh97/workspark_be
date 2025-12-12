CREATE TABLE IF NOT EXISTS `routes`
(
    `id` bigint NOT NULL AUTO_INCREMENT,
    `path`  varchar(255) DEFAULT NULL,
    `title` varchar(255) DEFAULT NULL,
    `created_at`       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT IGNORE INTO `routes` (`id`,`path`, `title`)
VALUES (1, '/dashboard', 'Dashboard'),
       (2, '/category/history', 'Category Info'),
       (3, '/category/manage', 'Manage Categories'),
       (4, '/nomination/list', 'Nomination List'),
       (5, '/nomination/history', 'Nomination History'),
       (6, '/reward/list', 'Reward List'),
       (7, '/reward/manage', 'Add/Edit Rewards'),
       (8, '/settings/system', 'System Settings'),
       (9, '/settings/access-control', 'Access Control');

CREATE TABLE IF NOT EXISTS `roles`
(
    `id`         bigint NOT NULL AUTO_INCREMENT,
    `role_desc`  varchar(255) DEFAULT NULL,
    `role_name`  varchar(255) NOT NULL,
    `status`     bit(1)       NOT NULL DEFAULT b'1',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT IGNORE INTO `roles` (`id`,`role_name`, `role_desc`)
VALUES (1, 'SUPER_ADMIN', 'Global Access'),
       (2, 'TENANT_ADMIN', 'System admin user of the tenant '),
       (3, 'ADMIN', 'Product Admin user of the tenant '),
       (4, 'USER', 'Basic user of the tenant ');

CREATE TABLE IF NOT EXISTS `route_roles`
(
    `id`       bigint NOT NULL AUTO_INCREMENT,
    `route_id` bigint NOT NULL,
    `role_id`  bigint NOT NULL,
    `created_at`       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY        `FKc83wt62q4qk4ofnt59eeo0vf0` (`role_id`),
    KEY        `FK767c7mds52ki4s1xc3jdiupti` (`route_id`),
    CONSTRAINT `FK4gbw3c5rfnlv8t5bowjrbhskj` FOREIGN KEY (`route_id`) REFERENCES `routes` (`id`),
    CONSTRAINT `FKc83wt62q4qk4ofnt59eeo0vf0` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT IGNORE INTO `route_roles` (`route_id`, `role_id`)
VALUES (1, 1),
       (2, 1),
       (3, 1),
       (4, 1),
       (5, 1),
       (6, 1),
       (7, 1),
       (8, 1),
       (9, 1);

-- Create `tenant_config` table
CREATE TABLE IF NOT EXISTS `tenant_config` (
    `tenant_id`        BIGINT NOT NULL AUTO_INCREMENT,
    `tenant_name`      VARCHAR(255) NOT NULL,
    `tenant_db_schema` VARCHAR(255) NOT NULL,
    `tenant_domain`    VARCHAR(255) NOT NULL,
    `status`           BIT(1) NOT NULL DEFAULT b'1',
    `created_at`       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`tenant_id`),
    UNIQUE KEY `tenant_name` (`tenant_name`)
    ) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Insert data into `tenant_config`
INSERT IGNORE INTO `tenant_config` (`tenant_name`, `tenant_db_schema`, `tenant_domain`)
VALUES
       ('mindfire', 'mindfire', 'mindfire'),
       ('localhost', 'localhost', 'localhost');
