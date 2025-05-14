package com.example.backend.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.DTO.TaiKhoanDTO;
import com.example.backend.Entity.TaiKhoanEntity;
import com.example.backend.Mapper.TaiKhoanMapper;
import com.example.backend.Repository.TaiKhoanRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TaiKhoanServiceImple implements TaiKhoanService {

     private TaiKhoanRepository taiKhoanRepository;

     @Override
     public TaiKhoanDTO createTaiKhoan(TaiKhoanDTO taiKhoanDTO) {
          // Kiểm tra trùng tên đăng nhập
          if (taiKhoanRepository.existsByTenDangNhap(taiKhoanDTO.getTenDangNhap())) {
               throw new RuntimeException("Tên đăng nhập đã tồn tại");
          }

          // Kiểm tra trùng số điện thoại
          if (taiKhoanRepository.existsBySoDienThoai(taiKhoanDTO.getSoDienThoai())) {
               throw new RuntimeException("Số điện thoại đã được sử dụng");
          }
          // Kiểm tra trùng email
          if (taiKhoanRepository.existsByEmail(taiKhoanDTO.getEmail())) {
               throw new RuntimeException("Email đã được sử dụng");
          }

          // Gán các giá trị mặc định
          taiKhoanDTO.setProfilePic("default.png");
          taiKhoanDTO.setCoverPic("default.png");
          taiKhoanDTO.setNgayTao(LocalDateTime.now().toString());
          taiKhoanDTO.setTrangThai("Bình Thường");
          taiKhoanDTO.setPhanQuyen("Người Dùng");

          // Chuyển DTO → Entity và lưu
          TaiKhoanEntity taiKhoanEntity = TaiKhoanMapper.mapToTaiKhoanEntity(taiKhoanDTO);
          TaiKhoanEntity savedTaiKhoanEntity = taiKhoanRepository.save(taiKhoanEntity);

          // Trả lại DTO
          return TaiKhoanMapper.mapToTaiKhoanDTO(savedTaiKhoanEntity);
     }

     @Override
     public TaiKhoanDTO updateTaiKhoan(TaiKhoanDTO taiKhoanDTO) {
          Optional<TaiKhoanEntity> optionalTaiKhoan = taiKhoanRepository.findById(taiKhoanDTO.getMaTK());
          if (optionalTaiKhoan.isPresent()) {
               TaiKhoanEntity existingTaiKhoan = optionalTaiKhoan.get();
               existingTaiKhoan.setTenDangNhap(taiKhoanDTO.getTenDangNhap());
               existingTaiKhoan.setMatKhau(taiKhoanDTO.getMatKhau());
               existingTaiKhoan.setProfilePic(taiKhoanDTO.getProfilePic());
               existingTaiKhoan.setCoverPic(taiKhoanDTO.getCoverPic());
               existingTaiKhoan.setHoTen(taiKhoanDTO.getHoTen());
               existingTaiKhoan.setGioiTinh(taiKhoanDTO.getGioiTinh());
               existingTaiKhoan.setSoDienThoai(taiKhoanDTO.getSoDienThoai());
               existingTaiKhoan.setNgaySinh(taiKhoanDTO.getNgaySinh());
               existingTaiKhoan.setTrangThai(taiKhoanDTO.getTrangThai());
               existingTaiKhoan.setPhanQuyen(taiKhoanDTO.getPhanQuyen());

               TaiKhoanEntity updatedTaiKhoan = taiKhoanRepository.save(existingTaiKhoan);
               return TaiKhoanMapper.mapToTaiKhoanDTO(updatedTaiKhoan);
          }
          return null;
     }

     @Override
     public TaiKhoanDTO disableTaiKhoan(int maTK) {
          Optional<TaiKhoanEntity> optionalTaiKhoan = taiKhoanRepository.findById(maTK);
          if (optionalTaiKhoan.isPresent()) {
               TaiKhoanEntity taiKhoan = optionalTaiKhoan.get();
               taiKhoan.setTrangThai("Vô Hiệu Hóa"); // Đánh dấu là đã xóa
               taiKhoanRepository.save(taiKhoan); // Lưu lại thay đổi
               return TaiKhoanMapper.mapToTaiKhoanDTO(taiKhoan);
          }
          return null;
     }

     @Override
     public TaiKhoanDTO enableTaiKhoan(int maTK) {
          Optional<TaiKhoanEntity> optionalTaiKhoan = taiKhoanRepository.findById(maTK);
          if (optionalTaiKhoan.isPresent()) {
               TaiKhoanEntity taiKhoan = optionalTaiKhoan.get();
               taiKhoan.setTrangThai("Bình Thường"); // Đánh dấu là đã xóa
               taiKhoanRepository.save(taiKhoan); // Lưu lại thay đổi
               return TaiKhoanMapper.mapToTaiKhoanDTO(taiKhoan);
          }
          return null;
     }

     @Override
     public List<TaiKhoanDTO> getAllTaiKhoan() {
          List<TaiKhoanEntity> entities = taiKhoanRepository.findAll();
          return entities.stream()
                    .map(TaiKhoanMapper::mapToTaiKhoanDTO)
                    .collect(Collectors.toList());
     }

     @Override
     public List<TaiKhoanDTO> getAllTaiKhoanByHoTen(String hoTen) {
          List<TaiKhoanEntity> entities = taiKhoanRepository.findByHoTenContaining(hoTen);
          return entities.stream()
                    .map(TaiKhoanMapper::mapToTaiKhoanDTO)
                    .collect(Collectors.toList());
     }

     @Override
     public List<TaiKhoanDTO> getAllTaiKhoanByTrangThai(String trangThai) {
          List<TaiKhoanEntity> entities = taiKhoanRepository.findByTrangThai(trangThai);
          return entities.stream()
                    .map(TaiKhoanMapper::mapToTaiKhoanDTO)
                    .collect(Collectors.toList());
     }

     @Override
     public TaiKhoanDTO getTaiKhoanById(int maTK) {
          Optional<TaiKhoanEntity> optionalTaiKhoan = taiKhoanRepository.findById(maTK);
          return optionalTaiKhoan.map(TaiKhoanMapper::mapToTaiKhoanDTO).orElse(null);
     }

     @Override
     public TaiKhoanDTO checkSignin(String tenDangNhap, String matKhau) {
          Optional<TaiKhoanEntity> optionalTaiKhoan = taiKhoanRepository.findByTenDangNhap(tenDangNhap);

          if (optionalTaiKhoan.isPresent()) {
               TaiKhoanEntity taiKhoanEntity = optionalTaiKhoan.get();

               // Kiểm tra mật khẩu
               if (taiKhoanEntity.getMatKhau().equals(matKhau)) {
                    return TaiKhoanMapper.mapToTaiKhoanDTO(taiKhoanEntity);
               } else {
                    return null;
               }
          }

          return null;
     }

     @Override
     public String checkSignup(String tenDangNhap, String soDienThoai) {
          // Kiểm tra xem tên đăng nhập đã tồn tại chưa
          boolean isUsernameExists = taiKhoanRepository.existsByTenDangNhap(tenDangNhap);
          if (isUsernameExists) {
               return "Tên đăng nhập đã tồn tại";
          }

          // Kiểm tra xem số điện thoại đã tồn tại chưa
          boolean isPhoneExists = taiKhoanRepository.existsBySoDienThoai(soDienThoai);
          if (isPhoneExists) {
               return "Số điện thoại đã được sử dụng";
          }

          // Không trùng, có thể tiếp tục xử lý đăng ký
          return "OK";
     }

     // update profile////////////////////////////
     @Override
     public TaiKhoanEntity updateProfile(Integer maTK, String userName, MultipartFile profilePic,
               MultipartFile coverPic) {
          System.out.println("Bat dau cap nhap thong tin cho maTK: " + maTK);
          Optional<TaiKhoanEntity> optionalTaiKhoan = taiKhoanRepository.findById(maTK);
          if (optionalTaiKhoan.isPresent()) {
               TaiKhoanEntity taiKhoan = optionalTaiKhoan.get();
               System.out.println("Nguoi dung tim thay: " + taiKhoan.getHoTen());

               // Cập nhật tên người dùng nếu có
               if (userName != null && !userName.isEmpty()) {
                    taiKhoan.setHoTen(userName);
                    System.out.println("Ten nguoi dung moi: " + userName);
               }

               // Cập nhật ảnh đại diện nếu có
               if (profilePic != null && !profilePic.isEmpty()) {
                    String profilePicPath = saveFile(profilePic, "Avatar");
                    taiKhoan.setProfilePic(profilePicPath);
                    System.out.println("Duong dan anh dai dien: " + profilePicPath);
               } else {
                    System.out.println("Khong co anh dai dien moi, giu nguyen anh cu");
               }

               // Cập nhật ảnh bìa nếu có
               if (coverPic != null && !coverPic.isEmpty()) {
                    System.out.println("Bat dau luu anh bia");
                    String coverPicPath = saveFile(coverPic, "Background");
                    taiKhoan.setCoverPic(coverPicPath);
                    System.out.println("Đuong dan anh bia: " + coverPicPath);
               } else {
                    System.out.println("Khong co anh bia moi, giu nguyen anh cu");
               }
               // Lưu thay đổi vào cơ sở dữ liệu
               taiKhoanRepository.save(taiKhoan);
               System.out.println("Cap nhat thanh cong cho maTK: " + maTK);
               return taiKhoan;
          } else {
               throw new RuntimeException("Khong tim thay nguoi dung voi ID: " + maTK);
          }
     }

     // Lưu file vào thư mục
     private String saveFile(MultipartFile file, String folder) {
          try {

               String currentDir = System.getProperty("user.dir");
               System.out.println("Thu muc lam viec hien tai: " + currentDir);

               // Điều hướng đến thư mục front-end/public/Resource
               Path baseFolder = Paths.get(currentDir).getParent() // Lấy thư mục cha của backend
                         .resolve("front-end/public/Resource");
               // Sử dụng tên file gốc
               String fileName = file.getOriginalFilename();
               System.out.println("Ten file goc: " + fileName);

               Path filePath = baseFolder.resolve(folder).resolve(fileName);
               System.out.println("Duong dan file: " + filePath);

               // Tạo thư mục nếu chưa tồn tại
               Files.createDirectories(filePath.getParent());
               System.out.println("Thu muc da duoc tao(neu chua ton tai)");

               // Ghi nội dung file vào đường dẫn
               Files.write(filePath, file.getBytes());
               System.out.println("File da duoc luu thanh cong");

               // Trả về tên file (không bao gồm thư mục)
               return fileName;
          } catch (IOException e) {
               System.err.println("Loi khi luu file: " + e.getMessage());
               throw new RuntimeException("Loi khi luu file: " + e.getMessage());
          }
     }

}
