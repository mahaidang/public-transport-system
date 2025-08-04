USE `transportdb`;

-- 1. Dữ liệu cho bảng stop
INSERT INTO `stop` (`code`, `name`, `latitude`, `longitude`, `description`) VALUES
('STP001', 'Đại học tôn đức thắng', 10.732091380000043, 106.69945521900007, 'Bến xe quốc tế lớn nhất Hà Nội'),
('STP002', 'Ga Hà Nội',        21.028333, 105.836944, 'Nhà ga trung tâm thành phố'),
('STP003', 'Cổng Bách Khoa',    21.006833, 105.847130, 'Gần trường ĐH Bách khoa'),
('STP004', 'Rmit',          10.728961, 106.6950895, 'Khu vực quận Cầu Giấy'),
('STP005', 'Đại học mở',           10.816924396000047, 106.67711557400008, 'Trung tâm phố cổ Hà Nội');

-- 2. Dữ liệu cho bảng route
INSERT INTO `route` (`code`, `name`, `description`, `is_active`) VALUES
('RT001', 'Tuyến 01', 'Mỹ Đình ↔ Ga Hà Nội',               TRUE),
('RT002', 'Tuyến 02', 'Cầu Giấy ↔ Hồ Gươm',                TRUE),
('RT003', 'Tuyến 03', 'Mỹ Đình ↔ Cầu Giấy',               TRUE),
('RT004', 'Tuyến 04', 'Ga Hà Nội ↔ Cổng Bách Khoa',        TRUE),
('RT005', 'Tuyến 05', 'Cổng Bách Khoa ↔ Hồ Gươm',         FALSE);

-- 3. Dữ liệu cho bảng route_variant
INSERT INTO `route_variant` (`distance_km`, `time_minute`, `seq`, `from_stop_id`, `to_stop_id`, `route_id`) VALUES
(5.2, 20, 1, 1, 2, 1),
(4.5, 18, 1, 4, 5, 2),
(6.0, 25, 1, 1, 4, 3),
(7.8, 30, 1, 2, 3, 4),
(3.6, 15, 1, 3, 5, 5);

-- 4. Dữ liệu cho bảng schedule
INSERT INTO `schedule` (`day_group`, `effective_from`, `effective_to`, `note`, `variant_id`) VALUES
('Weekday',   '2025-08-01 00:00:00', '2025-12-31 23:59:59', 'Chạy trong ngày thường', 1),
('Weekend',   '2025-08-02 00:00:00', '2025-12-31 23:59:59', 'Chạy cuối tuần',       2),
('Holiday',   '2025-09-02 00:00:00', '2025-09-05 23:59:59', 'Lễ Quốc khánh',       3),
('Weekday',   '2025-08-01 00:00:00', '2025-12-31 23:59:59', 'Chạy trong ngày thường', 4),
('Weekend',   '2025-08-02 00:00:00', '2025-12-31 23:59:59', 'Chạy cuối tuần',       5);

-- 5. Dữ liệu cho bảng schedule_trip
INSERT INTO `schedule_trip` (`depart_time`,           `arrive_time`,           `schedule_id`) VALUES
('2025-08-01 06:00:00', '2025-08-01 06:20:00', 1),
('2025-08-01 07:00:00', '2025-08-01 07:20:00', 2),
('2025-09-02 09:00:00', '2025-09-02 09:25:00', 3),
('2025-08-01 08:00:00', '2025-08-01 08:30:00', 4),
('2025-08-02 10:00:00', '2025-08-02 10:15:00', 5);
