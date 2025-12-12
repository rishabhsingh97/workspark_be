CREATE TABLE IF NOT EXISTS `templates`
(
    `id`            bigint          NOT NULL AUTO_INCREMENT,
    `name`          varchar(255)    NOT NULL,
    `template_code` varchar(255)    NOT NULL,
    `file_data`     longblob        NOT NULL,
    `preview_file`  longblob        NOT NULL,
    `created_at`    datetime(6)     NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at`    datetime(6)     NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `certificate`
(
    `id`            bigint          NOT NULL AUTO_INCREMENT,
    `src_file`      longblob        NOT NULL,
    `type`          varchar(255)    DEFAULT NULL,
    `created_at`    datetime(6)     NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at`    datetime(6)     NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE IF NOT EXISTS `template_dynamic_fields`
(
    `id`            bigint          NOT NULL AUTO_INCREMENT,
    `template_id`       bigint          NOT NULL,
    `dynamic_field`    varchar(255)    DEFAULT NULL,
    `created_at`        datetime(6)     NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at`        datetime(6)     NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (`id`),
    KEY              `FKn4735dfh1vxlqnorv4l7rplvl` (`template_id`),
    CONSTRAINT `FKn4735dfh1vxlqnorv4l7rplvl` FOREIGN KEY (`template_id`) REFERENCES `templates` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE IF NOT EXISTS `template_assets`
(
    `id`            bigint          NOT NULL AUTO_INCREMENT,
    `template_id`   bigint          NOT NULL,
    `name`          varchar(255)    NOT NULL,
    `asset_data`    longblob        NOT NULL,
    `created_at`    datetime(6)     NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `updated_at`    datetime(6)     NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (`id`),
    KEY              `FKn4735dfh1vxlqrplvl` (`template_id`),
    CONSTRAINT `FKn4735dfh1vxlqrplvl` FOREIGN KEY (`template_id`) REFERENCES `templates` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
