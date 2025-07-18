# HỆ THỐNG GIAO THÔNG CÔNG CỘNG
---

## 📌 Chức năng chính

### 1. Quản lý Lịch trình và Tuyến đường (Admin)
- **Thêm/Sửa/Xóa lịch trình**: Quản lý lịch trình của xe buýt, tàu điện và các phương tiện giao thông công cộng.
- **Quản lý tuyến đường**: Thêm mới, cập nhật, xóa các tuyến đường và trạm dừng.
- **Cập nhật thời gian khởi hành và đến nơi**: Quản lý thời gian khởi hành và thời gian đến của phương tiện.

### 2. Tìm kiếm và Định tuyến (Client)
- **Tìm kiếm tuyến đường**: Nhập điểm đi và điểm đến.
- **Tính toán lộ trình**: Tìm lộ trình tối ưu dựa trên **thời gian**, **số lần chuyển tuyến** và **khoảng cách**.
- **Hiển thị bản đồ**: Lộ trình di chuyển kèm trạm dừng hiển thị trên bản đồ.

### 3. Tính năng Cá nhân hóa (Client)
- **Lưu lịch trình yêu thích**: Lưu & quản lý các tuyến đường yêu thích.
- **Thông báo tùy chỉnh**: Thông báo thay đổi lịch trình/tuyến đường.

### 4. Cung cấp Thông tin Thời gian thực (*)
- **Thông báo thời gian thực**: Vị trí xe, tình trạng kẹt xe, sự cố.
- **Báo cáo kẹt xe**: Cho phép người dùng gửi báo cáo kèm **hình ảnh**.

---

## 🛠️ Công nghệ sử dụng
### Backend
- **SpringMVC Framework** (có thể kết hợp Spring Boot)
- **Spring Security** – chứng thực và phân quyền (vai trò **Admin/User**)
- **Hibernate ORM thuần** (không dùng Spring Data JPA)
- CSDL: **MySQL**

### Frontend
- **ReactJS** (React Router, Axios)

---

## 👤 Vai trò người dùng
| Vai trò | Quyền hạn |
| :-- | :-- |
| **Quản trị viên (Admin)** | Quản lý tuyến đường, trạm dừng, lịch trình; Thống kê, báo cáo |
| **Người dân (User)** | Đăng ký, đăng nhập, tìm đường, lưu lịch trình yêu thích |

---


