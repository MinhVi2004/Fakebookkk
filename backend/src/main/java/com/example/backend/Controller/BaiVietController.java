package com.example.backend.Controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.DTO.BaiVietDTO;
import com.example.backend.DTO.BaiVietDinhKemDTO;
import com.example.backend.DTO.BaiVietDinhKemResponseDTO;
import com.example.backend.DTO.BaiVietResponseDTO;
import com.example.backend.DTO.BinhLuanDTO;
import com.example.backend.DTO.BinhLuanResponseDTO;
import com.example.backend.DTO.LuotThichDTO;
import com.example.backend.DTO.TaiKhoanBVAndBLDTO;
import com.example.backend.DTO.TaiKhoanDTO;
import com.example.backend.Service.BaiVietDinhKemService;
import com.example.backend.Service.BaiVietService;
import com.example.backend.Service.BinhLuanService;
import com.example.backend.Service.FileService;
import com.example.backend.Service.LuotThichService;
import com.example.backend.Service.TaiKhoanService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

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
    private FileService fileService; // Inject ImageService

    @GetMapping("")
    public ResponseEntity<?> getAllPost() {
        List<BaiVietDTO> baiVietList = baiVietService.findAllByOrderByThoiGianDesc();
        List<BaiVietResponseDTO> baiVietResponseList = new ArrayList<BaiVietResponseDTO>();

        for (BaiVietDTO baiVietDTO : baiVietList) {
            TaiKhoanDTO taiKhoanDTO = taiKhoanService.getTaiKhoanById(baiVietDTO.getMaTK());

            if (baiVietDTO.getTrangThai().equals("Bình Thường")) {
                BaiVietResponseDTO baiVietResponseDTO = new BaiVietResponseDTO();

                // Lấy danh sách file đính kèm
                List<BaiVietDinhKemDTO> baiVietDinhKemList = baiVietDinhKemService
                        .getAllBaiVietDinhKemByMaBV(baiVietDTO.getMaBV());

                // Lấy lượt thích và bình luận
                List<LuotThichDTO> luotThichList = luotThichService.findByMaBV(baiVietDTO.getMaBV());
                List<BinhLuanDTO> binhLuanList = binhLuanService.findByMaBV(baiVietDTO.getMaBV());

                baiVietResponseDTO.setFromBaiVietDTO(baiVietDTO);
                baiVietResponseDTO.setLuotThichList(luotThichList);
                baiVietResponseDTO.setTaiKhoanBVAndBL(new TaiKhoanBVAndBLDTO(taiKhoanDTO));

                // Cập nhật lại đường dẫn của file đính kèm
                List<BaiVietDinhKemResponseDTO> baiVietDinhKemResponseList = new ArrayList<BaiVietDinhKemResponseDTO>();
                for (BaiVietDinhKemDTO dinhKemDTO : baiVietDinhKemList) {
                    try {
                        // Lấy file dưới dạng Base64 string
                        String base64FileData = fileService.getFile("DinhKem", dinhKemDTO.getLinkDK());

                        // Tạo đối tượng response
                        BaiVietDinhKemResponseDTO baiVietDinhKemResponseDTO = new BaiVietDinhKemResponseDTO();

                        // Gán dữ liệu từ dinhKemDTO vào baiVietDinhKemResponseDTO
                        baiVietDinhKemResponseDTO.setFromBaiVietDinhKemDTO(dinhKemDTO);

                        // Gán dữ liệu Base64 vào fileData
                        baiVietDinhKemResponseDTO.setFileData(base64FileData);

                        // Thêm đối tượng vào danh sách response
                        baiVietDinhKemResponseList.add(baiVietDinhKemResponseDTO);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                baiVietResponseDTO.setBaiVietDinhKemResponseList(baiVietDinhKemResponseList);

                // Xử lý bình luận
                List<BinhLuanResponseDTO> binhLuanResponseList = new ArrayList<BinhLuanResponseDTO>();
                for (BinhLuanDTO binhLuanDTO : binhLuanList) {
                    TaiKhoanDTO taiKhoanDTO_BinhLuan = taiKhoanService.getTaiKhoanById(binhLuanDTO.getMaTK());
                    BinhLuanResponseDTO binhLuanResponseDTO = new BinhLuanResponseDTO();
                    binhLuanResponseDTO.setFromBinhLuanDTO(binhLuanDTO);
                    binhLuanResponseDTO.setTaiKhoanBVAndBL(new TaiKhoanBVAndBLDTO(taiKhoanDTO_BinhLuan));
                    binhLuanResponseList.add(binhLuanResponseDTO);
                }
                baiVietResponseDTO.setBinhLuanList(binhLuanResponseList);

                baiVietResponseList.add(baiVietResponseDTO);
            }
        }

        return ResponseEntity.ok(baiVietResponseList);
    }
    @GetMapping("/{maTK}")
    public ResponseEntity<?> getAllPostById(@PathVariable int maTK) {
        List<BaiVietDTO> baiVietList = baiVietService.findAllByOrderByThoiGianDesc();
        List<BaiVietResponseDTO> baiVietResponseList = new ArrayList<BaiVietResponseDTO>();

        for (BaiVietDTO baiVietDTO : baiVietList) {
            TaiKhoanDTO taiKhoanDTO = taiKhoanService.getTaiKhoanById(baiVietDTO.getMaTK());
            if(baiVietDTO.getMaTK() != maTK) continue;
            if (baiVietDTO.getTrangThai().equals("Bình Thường")) {
                BaiVietResponseDTO baiVietResponseDTO = new BaiVietResponseDTO();

                // Lấy danh sách file đính kèm
                List<BaiVietDinhKemDTO> baiVietDinhKemList = baiVietDinhKemService
                        .getAllBaiVietDinhKemByMaBV(baiVietDTO.getMaBV());

                // Lấy lượt thích và bình luận
                List<LuotThichDTO> luotThichList = luotThichService.findByMaBV(baiVietDTO.getMaBV());
                List<BinhLuanDTO> binhLuanList = binhLuanService.findByMaBV(baiVietDTO.getMaBV());

                baiVietResponseDTO.setFromBaiVietDTO(baiVietDTO);
                baiVietResponseDTO.setLuotThichList(luotThichList);
                baiVietResponseDTO.setTaiKhoanBVAndBL(new TaiKhoanBVAndBLDTO(taiKhoanDTO));

                // Cập nhật lại đường dẫn của file đính kèm
                List<BaiVietDinhKemResponseDTO> baiVietDinhKemResponseList = new ArrayList<BaiVietDinhKemResponseDTO>();
                for (BaiVietDinhKemDTO dinhKemDTO : baiVietDinhKemList) {
                    try {
                        // Lấy file dưới dạng Base64 string
                        String base64FileData = fileService.getFile("DinhKem", dinhKemDTO.getLinkDK());

                        // Tạo đối tượng response
                        BaiVietDinhKemResponseDTO baiVietDinhKemResponseDTO = new BaiVietDinhKemResponseDTO();

                        // Gán dữ liệu từ dinhKemDTO vào baiVietDinhKemResponseDTO
                        baiVietDinhKemResponseDTO.setFromBaiVietDinhKemDTO(dinhKemDTO);

                        // Gán dữ liệu Base64 vào fileData
                        baiVietDinhKemResponseDTO.setFileData(base64FileData);

                        // Thêm đối tượng vào danh sách response
                        baiVietDinhKemResponseList.add(baiVietDinhKemResponseDTO);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                baiVietResponseDTO.setBaiVietDinhKemResponseList(baiVietDinhKemResponseList);

                // Xử lý bình luận
                List<BinhLuanResponseDTO> binhLuanResponseList = new ArrayList<BinhLuanResponseDTO>();
                for (BinhLuanDTO binhLuanDTO : binhLuanList) {
                    TaiKhoanDTO taiKhoanDTO_BinhLuan = taiKhoanService.getTaiKhoanById(binhLuanDTO.getMaTK());
                    BinhLuanResponseDTO binhLuanResponseDTO = new BinhLuanResponseDTO();
                    binhLuanResponseDTO.setFromBinhLuanDTO(binhLuanDTO);
                    binhLuanResponseDTO.setTaiKhoanBVAndBL(new TaiKhoanBVAndBLDTO(taiKhoanDTO_BinhLuan));
                    binhLuanResponseList.add(binhLuanResponseDTO);
                }
                baiVietResponseDTO.setBinhLuanList(binhLuanResponseList);

                baiVietResponseList.add(baiVietResponseDTO);
            }
        }

        return ResponseEntity.ok(baiVietResponseList);
    }
    @GetMapping("/hided")
    public ResponseEntity<?> getAllPostHided() {
        List<BaiVietDTO> baiVietList = baiVietService.findAllByOrderByThoiGianDesc();
        List<BaiVietResponseDTO> baiVietResponseList = new ArrayList<BaiVietResponseDTO>();

        for (BaiVietDTO baiVietDTO : baiVietList) {
            TaiKhoanDTO taiKhoanDTO = taiKhoanService.getTaiKhoanById(baiVietDTO.getMaTK());

            if (baiVietDTO.getTrangThai().equals("Đã Ẩn")) {
                BaiVietResponseDTO baiVietResponseDTO = new BaiVietResponseDTO();

                // Lấy danh sách file đính kèm
                List<BaiVietDinhKemDTO> baiVietDinhKemList = baiVietDinhKemService
                        .getAllBaiVietDinhKemByMaBV(baiVietDTO.getMaBV());

                // Lấy lượt thích và bình luận
                List<LuotThichDTO> luotThichList = luotThichService.findByMaBV(baiVietDTO.getMaBV());
                List<BinhLuanDTO> binhLuanList = binhLuanService.findByMaBV(baiVietDTO.getMaBV());

                baiVietResponseDTO.setFromBaiVietDTO(baiVietDTO);
                baiVietResponseDTO.setLuotThichList(luotThichList);
                baiVietResponseDTO.setTaiKhoanBVAndBL(new TaiKhoanBVAndBLDTO(taiKhoanDTO));

                // Cập nhật lại đường dẫn của file đính kèm
                List<BaiVietDinhKemResponseDTO> baiVietDinhKemResponseList = new ArrayList<BaiVietDinhKemResponseDTO>();
                for (BaiVietDinhKemDTO dinhKemDTO : baiVietDinhKemList) {
                    try {
                        // Lấy file dưới dạng Base64 string
                        String base64FileData = fileService.getFile("DinhKem", dinhKemDTO.getLinkDK());

                        // Tạo đối tượng response
                        BaiVietDinhKemResponseDTO baiVietDinhKemResponseDTO = new BaiVietDinhKemResponseDTO();

                        // Gán dữ liệu từ dinhKemDTO vào baiVietDinhKemResponseDTO
                        baiVietDinhKemResponseDTO.setFromBaiVietDinhKemDTO(dinhKemDTO);

                        // Gán dữ liệu Base64 vào fileData
                        baiVietDinhKemResponseDTO.setFileData(base64FileData);

                        // Thêm đối tượng vào danh sách response
                        baiVietDinhKemResponseList.add(baiVietDinhKemResponseDTO);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                baiVietResponseDTO.setBaiVietDinhKemResponseList(baiVietDinhKemResponseList);

                // Xử lý bình luận
                List<BinhLuanResponseDTO> binhLuanResponseList = new ArrayList<BinhLuanResponseDTO>();
                for (BinhLuanDTO binhLuanDTO : binhLuanList) {
                    TaiKhoanDTO taiKhoanDTO_BinhLuan = taiKhoanService.getTaiKhoanById(binhLuanDTO.getMaTK());
                    BinhLuanResponseDTO binhLuanResponseDTO = new BinhLuanResponseDTO();
                    binhLuanResponseDTO.setFromBinhLuanDTO(binhLuanDTO);
                    binhLuanResponseDTO.setTaiKhoanBVAndBL(new TaiKhoanBVAndBLDTO(taiKhoanDTO_BinhLuan));
                    binhLuanResponseList.add(binhLuanResponseDTO);
                }
                baiVietResponseDTO.setBinhLuanList(binhLuanResponseList);

                baiVietResponseList.add(baiVietResponseDTO);
            }
        }

        return ResponseEntity.ok(baiVietResponseList);
    }

//     @GetMapping("/{maTK}")
//     public ResponseEntity<?> getPostByMaTK(@PathVariable int maTK) {
//         List<BaiVietDTO> baiVietList = baiVietService.getBaiVietByMaTK(maTK);
//         return ResponseEntity.ok(baiVietList);
//     }

    @GetMapping("/{maBV}/like")
    public ResponseEntity<?> getSoLuongLuotThich(@PathVariable int maBV) {
        List<LuotThichDTO> luotThichList = luotThichService.findByMaBV(maBV);
        return ResponseEntity.ok(luotThichList.size());
    }
    
//     @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
// public ResponseEntity<?> createPost(
//         @RequestPart("baiViet") String baiVietJson,
//         @RequestPart(value = "dinhKems", required = false) String dinhKemsJson,
//         @RequestPart(value = "loaiDKs", required = false) String loaiDKsJson) {

//     try {
//         ObjectMapper mapper = new ObjectMapper();

//         // Parse bài viết
//         BaiVietDTO baiVietDTO = mapper.readValue(baiVietJson, BaiVietDTO.class);

//         // Parse danh sách base64 và loại đính kèm
//         List<String> dinhKemsBase64 = (dinhKemsJson != null && !dinhKemsJson.isEmpty())
//                 ? mapper.readValue(dinhKemsJson, new TypeReference<>() {})
//                 : new ArrayList<>();

//         List<String> loaiDKs = (loaiDKsJson != null && !loaiDKsJson.isEmpty())
//                 ? mapper.readValue(loaiDKsJson, new TypeReference<>() {})
//                 : new ArrayList<>();

//         // Lưu bài viết trước để có mã BV
//         BaiVietDTO savedBaiViet = baiVietService.createBaiViet(baiVietDTO);

//         // Xử lý đính kèm
//         for (int i = 0; i < dinhKemsBase64.size(); i++) {
//             String rawBase64 = dinhKemsBase64.get(i);
//             String cleanBase64;

//             if (rawBase64.contains(",")) {
//                 String[] parts = rawBase64.split(",", 2);
//                 if (parts.length != 2 || parts[1].isBlank()) {
//                     return ResponseEntity.badRequest().body("Chuỗi base64 không hợp lệ tại vị trí " + i);
//                 }
//                 cleanBase64 = parts[1];
//             } else {
//                 cleanBase64 = rawBase64;
//             }

//             String type = (loaiDKs.size() > i) ? loaiDKs.get(i) : "unknown";

//             // Lưu file
//             String fileName = fileService.saveFile(type, cleanBase64);

//             // Tạo đính kèm
//             BaiVietDinhKemDTO dinhKem = new BaiVietDinhKemDTO();
//             dinhKem.setMaBV(savedBaiViet.getMaBV());
//             dinhKem.setLinkDK(fileName);
//             dinhKem.setLoaiDK(type);

//             baiVietDinhKemService.createBaiVietDinhKem(dinhKem);
//         }

//         return ResponseEntity.ok(savedBaiViet);

//     } catch (IOException e) {
//         return ResponseEntity.badRequest().body("Lỗi dữ liệu đầu vào: " + e.getMessage());
//     } catch (Exception e) {
//         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi hệ thống: " + e.getMessage());
//     }
// }

@PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<?> createPost(
        @RequestPart("baiViet") String baiVietJson,
        @RequestPart(value = "dinhKems", required = false) String dinhKemsJson,
        @RequestPart(value = "loaiDKs", required = false) String loaiDKsJson) {
    try {
        ObjectMapper mapper = new ObjectMapper();
        BaiVietDTO baiVietDTO = mapper.readValue(baiVietJson, BaiVietDTO.class);

        List<String> dinhKemsBase64 = new ArrayList<>();
        if (dinhKemsJson != null && !dinhKemsJson.isEmpty()) {
            dinhKemsBase64 = mapper.readValue(dinhKemsJson, new TypeReference<List<String>>() {});
        }

        List<String> loaiDKs = new ArrayList<>();
        if (loaiDKsJson != null && !loaiDKsJson.isEmpty()) {
            loaiDKs = mapper.readValue(loaiDKsJson, new TypeReference<List<String>>() {});
        }

        // Chuyển toàn bộ xử lý sang Service
        return ResponseEntity.ok(baiVietService.createBaiVietWithDinhKems(baiVietDTO, dinhKemsBase64, loaiDKs));

    } catch (IOException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lỗi JSON đầu vào: " + e.getMessage());
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi hệ thống: " + e.getMessage());
    }
}

    

    @PostMapping("/like")
    public boolean updateLike(@RequestBody LuotThichDTO luotThichDTO) {
        return luotThichService.toggleLuotThich(luotThichDTO);
    }

    @PostMapping("/comment")
    public ResponseEntity<?> createComment(@RequestBody BinhLuanDTO binhLuanDTO) {
        return ResponseEntity.ok(
                binhLuanService.createBinhLuan(binhLuanDTO.getMaBV(), binhLuanDTO.getMaTK(), binhLuanDTO.getNoiDung()));
    }

    @PostMapping("/comment/delete/{maBL}")
    public void deleteComment(@PathVariable int maBL) {
        binhLuanService.deleteByMaBL(maBL);
    }
    @DeleteMapping("/{maBV}")
      public ResponseEntity<?> deletePost(@PathVariable int maBV) {
            try {
                  // Kiểm tra nếu bài viết tồn tại
                  BaiVietDTO baiVietDTO = baiVietService.getBaiVietById(maBV);
                  if (baiVietDTO == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                              .body("Bài viết không tồn tại");
                  }

                  // Xóa tất cả các file đính kèm liên quan đến bài viết
                  List<BaiVietDinhKemDTO> baiVietDinhKemList = baiVietDinhKemService.getAllBaiVietDinhKemByMaBV(maBV);
                  for (BaiVietDinhKemDTO dinhKemDTO : baiVietDinhKemList) {
                        // Xóa file từ hệ thống hoặc Cloud storage (tuỳ vào cách bạn lưu trữ)
                        fileService.deleteFile("DinhKem", dinhKemDTO.getLinkDK());
                  }

                  // Xóa bài viết
                  baiVietService.deleteBaiViet(maBV);

                  // Trả về phản hồi thành công
                  return ResponseEntity.ok("Bài viết đã được xóa thành công");
            } catch (Exception e) {
                  // In ra lỗi để kiểm tra
                  e.printStackTrace();

                  // Trả về thông báo lỗi với chi tiết thông qua ResponseEntity
                  return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Đã xảy ra lỗi trong quá trình xóa bài viết. Vui lòng thử lại sau.");
            }
      }



}
