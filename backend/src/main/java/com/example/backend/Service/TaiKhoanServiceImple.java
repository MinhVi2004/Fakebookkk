package com.example.backend.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

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

}
