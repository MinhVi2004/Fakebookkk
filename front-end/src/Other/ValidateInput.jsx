export const validateFormsignup = (
     fullname,
     birthday,
     gender,
     phone,
     email,
     tenDangNhap,
     password,
     repassword
) => {
     // Kiểm tra họ tên
     if (!fullname) {
          return "Họ tên không được để trống";
     }
     if (!/^[a-zA-ZÀ-ỹ\s]+$/.test(fullname)) {
          return "Họ tên chỉ được chứa chữ cái và khoảng trắng";
     }
     // Kiểm tra ngày sinh
     if (!birthday) {
          return "Vui lòng chọn ngày sinh";
     } else {
          // Tính toán tuổi từ đối tượng Date
          const currentDate = new Date();
          let age = currentDate.getFullYear() - birthday.getFullYear();

          const monthDiff = currentDate.getMonth() - birthday.getMonth();

          if (
               monthDiff < 0 ||
               (monthDiff === 0 && currentDate.getDate() < birthday.getDate())
          ) {
               age--; // Giảm tuổi nếu chưa đến sinh nhật năm nay
          }

          if (age < 16) {
               return "Bạn phải từ 16 tuổi trở lên để đăng ký";
          }
     }
     // Kiểm tra giới tính
     if (!gender) {
          return "Vui lòng chọn giới tính";
     }
     // Kiểm tra số điện thoại
     const phoneRegex = /^0[0-9]{9}$/;
     if (!phone || !phoneRegex.test(phone)) {
          return "Số điện thoại không hợp lệ (10 chữ số)";
     }
      const emailRegex = /^[a-zA-Z0-9._%+-]+@gmail\.com$/;
      if (!email || !emailRegex.test(email)) {
            return "Email phải có định dạng @gmail.com";
      }
     // Kiểm tra tên đăng nhập
     if (!tenDangNhap || tenDangNhap.length < 6) {
          return "Tên đăng nhập phải có ít nhất 6 ký tự";
     }

     // Kiểm tra mật khẩu
     if (!password || password.length < 6) {
          return "Mật khẩu phải có ít nhất 6 ký tự";
     }

     // Kiểm tra xác nhận mật khẩu
     if (password !== repassword) {
          return "Mật khẩu và xác nhận mật khẩu không khớp";
     }

     // Nếu không có lỗi, trả về null
     return null;
};
