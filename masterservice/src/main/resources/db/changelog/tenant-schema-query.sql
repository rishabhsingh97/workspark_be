CREATE TABLE `users` (
                         `id` bigint NOT NULL AUTO_INCREMENT,
                         `created_at` datetime(6) DEFAULT NULL,
                         `created_by` varchar(255) DEFAULT NULL,
                         `last_updated_at` datetime(6) DEFAULT NULL,
                         `last_updated_by` varchar(255) DEFAULT NULL,
                         `version` bigint DEFAULT NULL,
                         `email` varchar(255) DEFAULT NULL,
                         `first_name` varchar(255) DEFAULT NULL,
                         `last_name` varchar(255) DEFAULT NULL,
                         `password_hash` varchar(255) DEFAULT NULL,
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `user_role_mapping` (
                                     `id` bigint NOT NULL AUTO_INCREMENT,
                                     `created_at` datetime(6) DEFAULT NULL,
                                     `created_by` varchar(255) DEFAULT NULL,
                                     `last_updated_at` datetime(6) DEFAULT NULL,
                                     `last_updated_by` varchar(255) DEFAULT NULL,
                                     `version` bigint DEFAULT NULL,
                                     `role_id` bigint NOT NULL,
                                     `user_id` bigint NOT NULL,
                                     PRIMARY KEY (`id`),
                                     KEY `FK3y767mrjaru6vl6ctdaaw7os9` (`user_id`),
                                     CONSTRAINT `FK3y767mrjaru6vl6ctdaaw7os9` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `roles` (
                         `id` bigint NOT NULL AUTO_INCREMENT,
                         `status` int NOT NULL,
                         `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         `created_by` varchar(255) DEFAULT NULL,
                         `last_updated_at` datetime(6) DEFAULT NULL,
                         `last_updated_by` varchar(255) DEFAULT NULL,
                         `version` bigint DEFAULT NULL,
                         `name` enum('ADMIN','SUPER_ADMIN','SYSTEM','TENANT_ADMIN','USER') DEFAULT NULL,
                         `description` varchar(255) DEFAULT NULL,
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;