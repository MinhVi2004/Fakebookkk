package com.example.backend.Controller;

import com.example.backend.DTO.*;
import com.example.backend.Service.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api/fakebook/posts")
@CrossOrigin(origins = "http://localhost:3000")
public class BaiVietController {

    @Autowired
    private BaiVietService baiVietService;

    @Autowired
    private BaiVietDinhKemService baiVietDinhKemService;

    @Autowired
    private TaiKhoanService taiKhoanService;

    @Autowired
    private LuotThichService luotThichService;

    @Autowired
    private BinhLuanService binhLuanService;

    @Autowired
    private FileService fileService;

    @GetMapping("")
    public ResponseEntity<?> getAllPostQL() {
        List<BaiVietDTO> baiVietList = baiVietService.getAllBaiViet();
        List<BaiVietResponseDTO> responseList = buildBaiVietResponseList(baiVietList, "Bình Thường");
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/home/{maTK}")
    public ResponseEntity<?> getAllPost(@PathVariable int maTK) {
        List<BaiVietDTO> baiVietList = baiVietService.findAllVisiblePosts(maTK);
        List<BaiVietResponseDTO> responseList = buildBaiVietResponseList(baiVietList, "Bình Thường");
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/profile/{maTK}")
    public ResponseEntity<?> getAllPostById(@PathVariable int maTK) {
        List<BaiVietDTO> baiVietList = baiVietService.findAllByOrderByThoiGianDesc();
        // Lọc theo maTK và trạng thái Bình Thường
        List<BaiVietDTO> filtered = baiVietList.stream()
                .filter(bv -> bv.getMaTK() == maTK && "Bình Thường".equals(bv.getTrangThai()))
                .toList();
        List<BaiVietResponseDTO> responseList = buildBaiVietResponseList(filtered, null);
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/hided")
    public ResponseEntity<?> getAllPostHided() {
        List<BaiVietDTO> baiVietList = baiVietService.findAllByOrderByThoiGianDesc();
        List<BaiVietResponseDTO> responseList = buildBaiVietResponseList(baiVietList, "Đã Ẩn");
        return ResponseEntity.ok(responseList);
    }

    // Hàm chung xây dựng danh sách response dựa trên danh sách DTO và trạng thái lọc
    private List<BaiVietResponseDTO> buildBaiVietResponseList(List<BaiVietDTO> baiVietList, String trangThaiFilter) {
        List<BaiVietResponseDTO> baiVietResponseList = new ArrayList<>();

        for (BaiVietDTO baiVietDTO : baiVietList) {
            if (trangThaiFilter != null && !trangThaiFilter.equals(baiVietDTO.getTrangThai())) {
                continue;
            }
            BaiVietResponseDTO baiVietResponseDTO = new BaiVietResponseDTO();

            TaiKhoanDTO taiKhoanDTO = taiKhoanService.getTaiKhoanById(baiVietDTO.getMaTK());

            // Lấy đính kèm
            List<BaiVietDinhKemDTO> dinhKemList = baiVietDinhKemService.getAllBaiVietDinhKemByMaBV(baiVietDTO.getMaBV());

            List<BaiVietDinhKemResponseDTO> dinhKemResponseList = new ArrayList<>();
            for (BaiVietDinhKemDTO dinhKemDTO : dinhKemList) {
                try {
                    String base64FileData = fileService.getFile("DinhKem", dinhKemDTO.getLinkDK());
                    BaiVietDinhKemResponseDTO dinhKemResponseDTO = new BaiVietDinhKemResponseDTO();
                    dinhKemResponseDTO.setFromBaiVietDinhKemDTO(dinhKemDTO);
                    dinhKemResponseDTO.setFileData(base64FileData);
                    dinhKemResponseList.add(dinhKemResponseDTO);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Lấy lượt thích và bình luận
            List<LuotThichDTO> luotThichList = luotThichService.findByMaBV(baiVietDTO.getMaBV());
            List<BinhLuanDTO> binhLuanList = binhLuanService.findByMaBV(baiVietDTO.getMaBV());

            baiVietResponseDTO.setFromBaiVietDTO(baiVietDTO);
            baiVietResponseDTO.setLuotThichList(luotThichList);
            baiVietResponseDTO.setTaiKhoanBVAndBL(new TaiKhoanBVAndBLDTO(taiKhoanDTO));
            baiVietResponseDTO.setBaiVietDinhKemResponseList(dinhKemResponseList);

            // Xử lý bình luận
            List<BinhLuanResponseDTO> binhLuanResponseList = new ArrayList<>();
            for (BinhLuanDTO blDTO : binhLuanList) {
                TaiKhoanDTO blTaiKhoanDTO = taiKhoanService.getTaiKhoanById(blDTO.getMaTK());
                BinhLuanResponseDTO blResponseDTO = new BinhLuanResponseDTO();
                blResponseDTO.setFromBinhLuanDTO(blDTO);
                blResponseDTO.setTaiKhoanBVAndBL(new TaiKhoanBVAndBLDTO(blTaiKhoanDTO));
                binhLuanResponseList.add(blResponseDTO);
            }
            baiVietResponseDTO.setBinhLuanList(binhLuanResponseList);

            baiVietResponseList.add(baiVietResponseDTO);
        }

        return baiVietResponseList;
    }

    @GetMapping("/{maBV}/like")
    public ResponseEntity<?> getSoLuongLuotThich(@PathVariable int maBV) {
        List<LuotThichDTO> luotThichList = luotThichService.findByMaBV(maBV);
        return ResponseEntity.ok(luotThichList.size());
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createPost(
            @RequestPart("baiViet") String baiVietJson,
            @RequestPart(value = "dinhKems", required = false) String dinhKemsJson,
            @RequestPart(value = "loaiDKs", required = false) String loaiDKsJson) {

        try {
            ObjectMapper mapper = new ObjectMapper();

            BaiVietDTO baiVietDTO = mapper.readValue(baiVietJson, BaiVietDTO.class);

            List<String> dinhKems = new ArrayList<>();
            if (dinhKemsJson != null && !dinhKemsJson.isEmpty()) {
                dinhKems = mapper.readValue(dinhKemsJson, new TypeReference<List<String>>() {});
            }

            List<String> loaiDKs = new ArrayList<>();
            if (loaiDKsJson != null && !loaiDKsJson.isEmpty()) {
                loaiDKs = mapper.readValue(loaiDKsJson, new TypeReference<List<String>>() {});
            }

            BaiVietDTO saved = baiVietService.createBaiVietWithDinhKems(baiVietDTO, dinhKems, loaiDKs);

            return ResponseEntity.ok(saved);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lỗi khi tạo bài viết");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updatePost(@RequestBody BaiVietDTO baiVietDTO) {
        BaiVietDTO updated = baiVietService.updateBaiViet(baiVietDTO);
        if (updated == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy bài viết");
        }
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/update-status/{maBV}")
    public ResponseEntity<?> changeStatusPost(@PathVariable int maBV, @RequestParam String trangThai) {
        boolean success = baiVietService.changeStatusBaiViet(maBV, trangThai);
        if (!success) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy bài viết");
        }
        return ResponseEntity.ok("Đổi trạng thái thành công");
    }

    @DeleteMapping("/delete/{maBV}")
    public ResponseEntity<?> deletePost(@PathVariable int maBV) {
        boolean success = baiVietService.deleteBaiViet(maBV);
        if (!success) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy bài viết");
        }
        return ResponseEntity.ok("Xóa bài viết thành công");
    }
}
