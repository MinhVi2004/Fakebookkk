package com.example.backend.Service;

import com.example.backend.DTO.TaiKhoanDTO;
import com.example.backend.Entity.TaiKhoanEntity;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface TaiKhoanService {
    TaiKhoanDTO createTaiKhoan(TaiKhoanDTO taiKhoanDTO);

      TaiKhoanDTO getTaiKhoanById(int maTK);

    TaiKhoanDTO updateTaiKhoan(TaiKhoanDTO taiKhoanDTO);

    TaiKhoanDTO disableTaiKhoan(int maTK);

    TaiKhoanDTO enableTaiKhoan(int maTK);

    List<TaiKhoanDTO> getAllTaiKhoan();

    List<TaiKhoanDTO> getAllTaiKhoanByHoTen(String hoTen);

    List<TaiKhoanDTO> getAllTaiKhoanByTrangThai(String trangThai);

    TaiKhoanDTO checkSignin(String tenDangNhap, String matKhau);

    String checkSignup(String tenDangNhap, String soDienThoai);

    TaiKhoanEntity updateProfile(Integer maTK, String userName, MultipartFile profilePic, MultipartFile coverPic);

    boolean updateMatKhau(Integer maTK, String newPassword);
}
