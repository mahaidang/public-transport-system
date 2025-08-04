-- 1. Xoá database nếu đã tồn tại
DROP DATABASE IF EXISTS `transportdb`;

-- 2. Tạo lại database và chọn sử dụng
CREATE DATABASE `transportdb` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `transportdb`;

CREATE TABLE role (
    id            BIGINT PRIMARY KEY AUTO_INCREMENT,
    name          VARCHAR(50) UNIQUE NOT NULL,   -- ADMIN, USER
    description   VARCHAR(255)
);

CREATE TABLE user (
    id            BIGINT PRIMARY KEY AUTO_INCREMENT,
    username      VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255)      NOT NULL,
    full_name     VARCHAR(100),
    email         VARCHAR(100) UNIQUE,
    avatar_url    VARCHAR(255),
    role_id       BIGINT            NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES role(id)
);

-- 3. Bảng Stop
CREATE TABLE `stop` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `code` VARCHAR(50) NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `latitude` DOUBLE NOT NULL,
  `longitude` DOUBLE NOT NULL,
  `description` TEXT,
  INDEX (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. Bảng Route
CREATE TABLE `route` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `code` VARCHAR(50) NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `description` TEXT,
  `is_active` BOOLEAN NOT NULL DEFAULT TRUE,
  UNIQUE KEY `uq_route_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. Bảng RouteVariant
CREATE TABLE `route_variant` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `distance_km` DOUBLE NOT NULL,
  `time_minute` INT NOT NULL,
  `seq` INT NOT NULL,
  `from_stop_id` BIGINT NOT NULL,
  `to_stop_id` BIGINT NOT NULL,
  `route_id` BIGINT NOT NULL,
  INDEX (`from_stop_id`),
  INDEX (`to_stop_id`),
  INDEX (`route_id`),
  CONSTRAINT `fk_rv_start_stop`
    FOREIGN KEY (`from_stop_id`) REFERENCES `stop`(`id`)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT `fk_rv_end_stop`
    FOREIGN KEY (`to_stop_id`)   REFERENCES `stop`(`id`)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT `fk_rv_route`
    FOREIGN KEY (`route_id`)      REFERENCES `route`(`id`)
    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 6. Bảng Schedule
CREATE TABLE `schedule` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `day_group` VARCHAR(50) NOT NULL,
  `effective_from` DATETIME NOT NULL,
  `effective_to`   DATETIME NOT NULL,
  `note` TEXT,
  `variant_id` BIGINT NOT NULL,
  INDEX (`variant_id`),
  CONSTRAINT `fk_schedule_variant`
    FOREIGN KEY (`variant_id`) REFERENCES `route_variant`(`id`)
    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 7. Bảng ScheduleTrip
CREATE TABLE `schedule_trip` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `depart_time` DATETIME NOT NULL,
  `arrive_time` DATETIME NOT NULL,
  `schedule_id` BIGINT NOT NULL,
  -- Nếu bạn tạo bảng vehicle thì mở comment và thay bằng: FOREIGN KEY (`vehicle_id`) REFERENCES `vehicle`(`id`)
  -- `vehicle_id` BIGINT NOT NULL,
  INDEX (`schedule_id`),
  CONSTRAINT `fk_st_schedule`
    FOREIGN KEY (`schedule_id`) REFERENCES `schedule`(`id`)
    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
