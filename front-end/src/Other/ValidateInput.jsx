export const validateFormsignup = (
     fullname,
     birthday,
     gender,
     phone,
     tenDangNhap,
     password,
     repassword
) => {
     // Kiểm tra họ tên
     if (!fullname) {
          return "Họ tên không được để trống";
     }

     // Kiểm tra tên đăng nhập
     if (!tenDangNhap || tenDangNhap.length < 6) {
          return "Tên đăng nhập phải có ít nhất 6 ký tự";
     }

     // Kiểm tra số điện thoại
     const phoneRegex = /^[0-9]{10}$/;
     if (!phone || !phoneRegex.test(phone)) {
          return "Số điện thoại không hợp lệ (10 chữ số)";
     }

     // Kiểm tra mật khẩu
     if (!password || password.length < 6) {
          return "Mật khẩu phải có ít nhất 6 ký tự";
     }

     // Kiểm tra xác nhận mật khẩu
     if (password !== repassword) {
          return "Mật khẩu và xác nhận mật khẩu không khớp";
     }

     // Kiểm tra giới tính
     if (!gender) {
          return "Vui lòng chọn giới tính";
     }

     // Kiểm tra ngày sinh
     if (!birthday) {
          return "Vui lòng chọn ngày sinh";
      } else {
          // Tính toán tuổi từ đối tượng Date
          const currentDate = new Date();
          let age = currentDate.getFullYear() - birthday.getFullYear();
          
          const monthDiff = currentDate.getMonth() - birthday.getMonth();
          
          if (monthDiff < 0 || (monthDiff === 0 && currentDate.getDate() < birthday.getDate())) {
              age--; // Giảm tuổi nếu chưa đến sinh nhật năm nay
          }
  
          if (age < 16) {
              return "Bạn phải từ 16 tuổi trở lên để đăng ký";
          }
      }

     // Nếu không có lỗi, trả về null
     return null;
};
