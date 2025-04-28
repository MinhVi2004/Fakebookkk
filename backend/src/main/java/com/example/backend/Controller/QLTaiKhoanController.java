package com.example.backend.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.DTO.TaiKhoanDTO;
import com.example.backend.Service.TaiKhoanService;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@AllArgsConstructor
@RestController
@RequestMapping("/api/fakebook/admin")
@CrossOrigin(origins = "http://localhost:3000") // Thêm dòng này react được phép truy cập
public class QLTaiKhoanController {

     private TaiKhoanService taiKhoanService;

     @GetMapping("/users")
     public ResponseEntity<?> getAllUser() {
          List<TaiKhoanDTO> list = taiKhoanService.getAllTaiKhoan();
          return ResponseEntity.ok(list);
     }

     @GetMapping("/users/{id}")
     public ResponseEntity<?> getUserById(@PathVariable("id") int id) {
          TaiKhoanDTO taiKhoanDTO = taiKhoanService.getTaiKhoanById(id);
    
          if (taiKhoanDTO != null) {
               return ResponseEntity.ok(taiKhoanDTO);
          } else {
               Map<String, Object> response = new HashMap<>();
               response.put("status", "error");
               response.put("message", "Không tìm thấy người dùng với ID: " + id);
               return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
          }
     }
     @PutMapping("users/disable/{id}")
     public ResponseEntity<?> disableUser(@PathVariable("id") int id) {
          TaiKhoanDTO deleted = taiKhoanService.disableTaiKhoan(id);

          if (deleted != null) {
               return ResponseEntity.ok(deleted);
          } else {
               Map<String, Object> response = new HashMap<>();
               response.put("status", "error");
               response.put("message", "Không tìm thấy người dùng để vô hiệu hóa");
               return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
          }
     }
     @PutMapping("users/enable/{id}")
     public ResponseEntity<?> enable(@PathVariable("id") int id) {
          TaiKhoanDTO deleted = taiKhoanService.enableTaiKhoan(id);

          if (deleted != null) {
               return ResponseEntity.ok(deleted);
          } else {
               Map<String, Object> response = new HashMap<>();
               response.put("status", "error");
               response.put("message", "Không tìm thấy người dùng để mở khóa tài khoản");
               return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
          }
     }
}
