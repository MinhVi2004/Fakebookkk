package com.example.backend.Controller;

import com.example.backend.DTO.BaiVietDTO;
import com.example.backend.DTO.BaiVietDinhKemDTO;
import com.example.backend.Service.BaiVietService;
import com.example.backend.Service.BaiVietDinhKemService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/fakebook/admin")
@CrossOrigin(origins = "http://localhost:3000") // Thêm dòng này react được phép truy cập
public class QLBaiVietController {

    private BaiVietService baiVietService;
    private BaiVietDinhKemService baiVietDinhKemService;

    // Lấy danh sách tất cả bài viết
    @GetMapping("/posts")
    public ResponseEntity<?> getAllPost() {
        List<BaiVietDTO> list = baiVietService.getAllBaiViet();
        return ResponseEntity.ok(list);
    }

    // Lấy thông tin bài viết theo ID
    @GetMapping("/posts/{maBV}")
    public ResponseEntity<?> getPostById(@PathVariable int maBV) {
        BaiVietDTO baiVietDTO = baiVietService.getBaiVietById(maBV);
        if (baiVietDTO != null) {
            return ResponseEntity.ok(baiVietDTO);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Bài viết không tồn tại.");
    }

    // Tạo mới bài viết (kèm theo các đính kèm nếu có)
    @PostMapping("/posts")
    public ResponseEntity<?> createPost(@RequestBody BaiVietDTO baiVietDTO) {
        // Tạo bài viết mới
        BaiVietDTO createdBaiViet = baiVietService.createBaiViet(baiVietDTO);
        
        // Kiểm tra xem có các đính kèm hay không
        if (baiVietDTO.getDinhKems() != null && !baiVietDTO.getDinhKems().isEmpty()) {
            for (BaiVietDinhKemDTO dinhKemDTO : baiVietDTO.getDinhKems()) {
                dinhKemDTO.setMaBV(createdBaiViet.getMaBV()); // Gán mã bài viết cho đính kèm
                baiVietDinhKemService.createBaiVietDinhKem(dinhKemDTO); // Thêm đính kèm vào cơ sở dữ liệu
            }
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(createdBaiViet);
    }

    // Cập nhật bài viết
    @PutMapping("/posts/{maBV}")
    public ResponseEntity<?> updatePost(@PathVariable int maBV, @RequestBody BaiVietDTO baiVietDTO) {
        // Set maBV cho DTO để biết bài viết cần cập nhật
        baiVietDTO.setMaBV(maBV);
        BaiVietDTO updatedBaiViet = baiVietService.updateBaiViet(baiVietDTO);
        if (updatedBaiViet != null) {
            return ResponseEntity.ok(updatedBaiViet);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Bài viết không tồn tại để cập nhật.");
    }

    // Thay đổi trạng thái bài viết
    @PutMapping("/posts/{maBV}/status")
    public ResponseEntity<?> changeStatusPost(@PathVariable int maBV, @RequestParam String trangThai) {
        boolean isUpdated = baiVietService.changeStatusBaiViet(maBV, trangThai);
        if (isUpdated) {
            return ResponseEntity.ok("Trạng thái bài viết đã được thay đổi.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Bài viết không tồn tại để thay đổi trạng thái.");
    }

    // Lấy bài viết theo MaTK
    @GetMapping("/posts/byMaTK/{maTK}")
    public ResponseEntity<?> getPostByMaTK(@PathVariable int maTK) {
        List<BaiVietDTO> list = baiVietService.getBaiVietByMaTK(maTK);
        return ResponseEntity.ok(list);
    }
    
    // Lấy bài viết theo MaTK và LoaiChiaSe
    @GetMapping("/posts/byMaTKAndLoaiChiaSe")
    public ResponseEntity<?> getPostByMaTKAndLoaiChiaSe(
            @RequestParam int maTK,
            @RequestParam String loaiChiaSe) {
        List<BaiVietDTO> list = baiVietService.getBaiVietByMaTKAndLoaiChiaSe(maTK, loaiChiaSe);
        return ResponseEntity.ok(list);
    }

    // Lấy bài viết theo MaTK và TrangThai
    @GetMapping("/posts/byMaTKAndTrangThai")
    public ResponseEntity<?> getPostByMaTKAndTrangThai(
            @RequestParam int maTK,
            @RequestParam String trangThai) {
        List<BaiVietDTO> list = baiVietService.getBaiVietByMaTKAndTrangThai(maTK, trangThai);
        return ResponseEntity.ok(list);
    }

    // Lấy bài viết theo TrangThai
    @GetMapping("/posts/byTrangThai")
    public ResponseEntity<?> getPostByTrangThai(@RequestParam String trangThai) {
        List<BaiVietDTO> list = baiVietService.getBaiVietByTrangThai(trangThai);
        return ResponseEntity.ok(list);
    }
}
