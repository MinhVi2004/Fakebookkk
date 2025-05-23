# Facebook Clone - Mạng xã hội đơn giản

Dự án này là một bản sao đơn giản của mạng xã hội Facebook, xây dựng bằng **React** cho frontend và **Spring Boot** cho backend. Mục tiêu là giúp người dùng có trải nghiệm đăng bài, tương tác, quản lý bạn bè và hồ sơ cá nhân tương tự Facebook.

##  Các chức năng chính

### 1. Đăng ký / Đăng nhập người dùng
- Người dùng có thể tạo tài khoản và đăng nhập bằng email & mật khẩu.
- Bảo mật thông tin người dùng bằng JWT (JSON Web Token).

### 2. Đăng bài viết (văn bản, hình ảnh, video)
- Cho phép người dùng tạo bài viết với:
  - Văn bản
  - Hình ảnh (upload lên Cloudinary)
  - Video (upload lên Cloudinary)

### 3. Tương tác với bài viết
- Người dùng có thể:
  - Thả  "Thích"
  - Viết bình luận
  - Chia sẻ bài viết lên trang cá nhân

### 4. Quản lý bạn bè
- Gửi lời mời kết bạn / Hủy kết bạn
- Duyệt danh sách bạn bè
- Quản lý trạng thái kết nối

### 5. Quản lý hồ sơ cá nhân
- Cập nhật thông tin người dùng
- Thay đổi ảnh đại diện và ảnh bìa
- Xem trang cá nhân của mình hoặc người khác

### 6. Trang Admin (đã hoàn thành)
- Quản lý người dùng (xóa, khóa tài khoản)
- Quản lý bài viết (ẩn, xóa nội dung vi phạm)

---

## ⚙️ Công nghệ sử dụng

- **Frontend**: ReactJS, TailwindCSS, Axios
- **Backend**: Spring Boot, MySQL, Spring Security, JWT
- **Media**: Cloudinary API
- **Database**: MySQL 8.x

---

##  Hướng dẫn cài đặt

###  Yêu cầu môi trường

- Node.js >= 16.x
- Java JDK >= 17
- MySQL Server
- IDE: IntelliJ IDEA / VS Code

### 1. Cài đặt Backend (Spring Boot)

# Di chuyển vào thư mục backend
cd backend

# Cấu hình CSDL trong file: src/main/resources/application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/fakebook
spring.datasource.username=root
spring.datasource.password=your_password

# Build và chạy ứng dụng
./mvnw spring-boot:run

### 2. Cài đặt FrontEnd (ReactJs)
#Cài đặt môi trường NodeJs
# Di chuyển vào thư mục frontend
cd frontend

# Cài đặt các thư viện cần thiết
npm install

# Chạy ứng dụng React ở chế độ phát triển
npm run dev

# Lưu ý: chạy trên localhost:3000 

