package com.example.backend.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.DTO.TaiKhoanDTO;
import com.example.backend.Entity.TaiKhoanEntity;
import com.example.backend.Repository.TaiKhoanRepository;
import com.example.backend.Service.TaiKhoanService;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/fakebook")
@CrossOrigin(origins = "http://localhost:3000") // Thêm dòng này react được phép truy cập
public class TaiKhoanController {

     @Autowired
     private TaiKhoanService taiKhoanService;

     @PostMapping("/signup")
     public ResponseEntity<?> signup(@RequestBody TaiKhoanDTO taiKhoanDTO) {
          try {
               TaiKhoanDTO created = taiKhoanService.createTaiKhoan(taiKhoanDTO);
               Map<String, Object> response = new HashMap<>();
               response.put("status", "success");
               response.put("message", "Đăng ký thành công");
               response.put("data", created);
               return ResponseEntity.ok(response);
          } catch (RuntimeException ex) {
               Map<String, Object> error = new HashMap<>();
               error.put("status", "error");
               error.put("message", "Đăng ký thất bại: " + ex.getMessage());
               return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
          }
     }

     @PostMapping("/signin")
     public ResponseEntity<?> signin(@RequestBody TaiKhoanDTO loginRequest) {
          Map<String, Object> response = new HashMap<>();
          if (loginRequest.getTenDangNhap() == "" && loginRequest.getMatKhau() == "") {
               response.put("status", "error");
               response.put("message", "Thiếu thông tin đăng nhập");
               return ResponseEntity.badRequest().body(response);
          }
          if (loginRequest.getTenDangNhap() == "") {
               response.put("status", "error");
               response.put("message", "Vui lòng nhập tên đăng nhập");
               return ResponseEntity.badRequest().body(response);
          }
          if (loginRequest.getMatKhau() == "") {
               response.put("status", "error");
               response.put("message", "Vui lòng nhập mật khẩu");
               return ResponseEntity.badRequest().body(response);
          }
          TaiKhoanDTO taiKhoanDTO = taiKhoanService.checkSignin(loginRequest.getTenDangNhap(),
                    loginRequest.getMatKhau());
          if (taiKhoanDTO != null) {
               if (taiKhoanDTO.getTrangThai().equals("Vô Hiệu Hóa")) {
                    response.put("status", "error");
                    response.put("message", "Tài khoản của bạn đã bị vô hiệu hóa");
                    return ResponseEntity.badRequest().body(response);
               } else {
                    response.put("status", "success");
                    response.put("message", "Đăng nhập thành công");
                    response.put("data", taiKhoanDTO);
                    return ResponseEntity.ok(response);
               }
          } else {
               response.put("status", "error");
               response.put("message", "Sai tài khoản hoặc mật khẩu");
               return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
          }
     }

     @GetMapping("/user/{maTK}")
     public ResponseEntity<?> getTaiKhoanById(@PathVariable Integer maTK) {
          try {
               TaiKhoanDTO taiKhoanDTO = taiKhoanService.getTaiKhoanById(maTK);
               if (taiKhoanDTO != null) {
                    Map<String, Object> response = new HashMap<>();
                    response.put("status", "success");
                    response.put("message", "Lấy thông tin thành công");
                    response.put("data", taiKhoanDTO);
                    return ResponseEntity.ok(response);
               } else {
                    Map<String, Object> response = new HashMap<>();
                    response.put("status", "error");
                    response.put("message", "Không tìm thấy người dùng với ID: " + maTK);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
               }
          } catch (RuntimeException ex) {
               Map<String, Object> error = new HashMap<>();
               error.put("status", "error");
               error.put("message", "Lỗi khi lấy thông tin người dùng: " + ex.getMessage());
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
          }
     }

     @PutMapping("/update")
     public ResponseEntity<?> updateTaiKhoan(@RequestBody TaiKhoanDTO taiKhoanDTO) {
          TaiKhoanDTO updated = taiKhoanService.updateTaiKhoan(taiKhoanDTO);
          if (updated != null) {
               return ResponseEntity.ok(updated);
          } else {
               Map<String, Object> response = new HashMap<>();
               response.put("status", "error");
               response.put("message", "Không tìm thấy người dùng để cập nhật");
               return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
          }
     }
      @PutMapping("/updateMatKhau/{maTK}")
     public ResponseEntity<?> updateMatKhau(@PathVariable Integer maTK) {
          boolean updated = taiKhoanService.updateMatKhau(maTK, "123456");
          if (updated) {
               return ResponseEntity.ok(Map.of(
                  "status", "success",
                  "message", "Cập nhật mật khẩu thành công"
                  ));
          } else {
               Map<String, Object> response = new HashMap<>();
               response.put("status", "error");
               response.put("message", "Không tìm thấy người dùng để cập nhật");
               return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
          }
     }
     @GetMapping("/searchUser")
     public ResponseEntity<?> searchUser(@RequestParam String keyWord) {
          List<TaiKhoanDTO> list = taiKhoanService.getAllTaiKhoanByHoTen(keyWord);
          return ResponseEntity.ok(list);
     }

     @PutMapping("/update-profile/{maTK}")
     public ResponseEntity<?> updateProfile(
               @PathVariable Integer maTK,
               @RequestParam("userName") String userName,
               @RequestParam(value = "profilePic", required = false) MultipartFile profilePic,
               @RequestParam(value = "coverPic", required = false) MultipartFile coverPic) {
          try {
               System.out.println("Nhan yeu cau cap nhap thong tin cho maTK: " + maTK);
               TaiKhoanEntity updatedUser = taiKhoanService.updateProfile(maTK, userName, profilePic, coverPic);
               Map<String, Object> response = new HashMap<>();
               response.put("status", "success");
               response.put("message", "Cap nhap thong tin thanh cong");
               response.put("data", updatedUser); // Trả về dữ liệu người dùng đã cập nhật

               return ResponseEntity.ok(response);
          } catch (RuntimeException ex) {
               System.err.println("Loi khi cap nhap thong tin: " + ex.getMessage());
               Map<String, Object> error = new HashMap<>();
               error.put("status", "error");
               error.put("message", "Cap nhap that bai: " + ex.getMessage());
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
          }
     }

}
