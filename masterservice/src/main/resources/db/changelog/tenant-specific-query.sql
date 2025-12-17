CREATE TABLE IF NOT EXISTS `recognition_category`
(
    `category_id`                    bigint       NOT NULL AUTO_INCREMENT,
    `anonymous_nomination`           bit(1)       DEFAULT NULL,
    `category_name`                  varchar(255) NOT NULL,
    `certificate_expiry_date`        date         DEFAULT NULL,
    `certificate_required`           bit(1)       DEFAULT NULL,
    `certificate_template_id`        varchar(255) DEFAULT NULL,
    `created_date_time`              datetime(6) NOT NULL,
    `cron_expression`                varchar(60)  DEFAULT NULL,
    `end_date`                       date         DEFAULT NULL,
    `is_active`                      bit(1)       DEFAULT NULL,
    `max_nomination`                 int          DEFAULT NULL,
    `max_winner`                     int          DEFAULT NULL,
    `no_of_days_to_nomination`       int          DEFAULT NULL,
    `no_of_days_to_selection`        int          DEFAULT NULL,
    `selection_type`                 enum('AUTO','PANEL','EMPTY') DEFAULT NULL,
    `start_date`                     date         DEFAULT NULL,
    `type`                           enum('NOMINATION','SPOT','VOTING') NOT NULL,
    `updated_date_time`              datetime(6) DEFAULT NULL,
    PRIMARY KEY (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `category_nominators_mapping`
(
    `nominated_by` bigint NOT NULL,
    `category_id`  bigint NOT NULL,
    PRIMARY KEY (`category_id`, `nominated_by`),
    CONSTRAINT `FKf2l009vburx9xxh14yfb5kmph` FOREIGN KEY (`category_id`) REFERENCES `recognition_category` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `category_panelist_mapping`
(
    `panelist_id` bigint NOT NULL,
    `category_id` bigint NOT NULL,
    PRIMARY KEY (`category_id`, `panelist_id`),
    CONSTRAINT `FK5sfvj6mwklhnbttd1v7h0fim3` FOREIGN KEY (`category_id`) REFERENCES `recognition_category` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `nomination`
(
    `id`                   bigint NOT NULL AUTO_INCREMENT,
    `category_id`          bigint       DEFAULT NULL,
    `created_at`           datetime(6) DEFAULT NULL,
    `nominated_by_user_id` int          DEFAULT NULL,
    `nominee_email`        varchar(255) DEFAULT NULL,
    `nominee_employee_id`  varchar(255) DEFAULT NULL,
    `nominee_name`         varchar(255) DEFAULT NULL,
    `updated_at`           datetime(6) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `nomination_questions_answer_mapping`
(
    `id`            bigint NOT NULL AUTO_INCREMENT,
    `answer`        varchar(255) DEFAULT NULL,
    `question_id`   int          DEFAULT NULL,
    `nomination_id` bigint NOT NULL,
    PRIMARY KEY (`id`),
    KEY             `FK48gmkpy25mmldcgp61u82qrhd` (`nomination_id`),
    CONSTRAINT `FK48gmkpy25mmldcgp61u82qrhd` FOREIGN KEY (`nomination_id`) REFERENCES `nomination` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `question` (
    `question_id` BIGINT NOT NULL AUTO_INCREMENT,
    `question` VARCHAR(255) NOT NULL,
    `type` enum('NOMINATION','SPOT','VOTING') NOT NULL,
    `data_type` ENUM('BOOLEAN','TEXT','DROPDOWN','CHECK') NOT NULL,
    PRIMARY KEY (`question_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `question_category_mapping` (
    `question_id` BIGINT NOT NULL AUTO_INCREMENT,
    `category_id` BIGINT NOT NULL,
    `data_type` ENUM('BOOLEAN','TEXT','DROPDOWN','CHECK') NOT NULL,
    `question` VARCHAR(255) DEFAULT NULL,
    `created_at` DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at` DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (`question_id`),
    CONSTRAINT `fk_category_id` FOREIGN KEY (`category_id`) REFERENCES `recognition_category`(`category_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `recognition_tier`
(
    `tier_id`           bigint       NOT NULL AUTO_INCREMENT,
    `category_id`       bigint       NOT NULL,
    `created_date_time` datetime(6) NOT NULL,
    `description`       varchar(255) DEFAULT NULL,
    `end_date`          datetime(6) NOT NULL,
    `is_active`         bit(1)       NOT NULL,
    `name`              varchar(100) NOT NULL,
    `start_date`        datetime(6) NOT NULL,
    `updated_date_time` datetime(6) NOT NULL,
    PRIMARY KEY (`tier_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;