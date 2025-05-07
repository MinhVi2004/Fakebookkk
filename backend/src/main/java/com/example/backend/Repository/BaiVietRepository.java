package com.example.backend.Repository;

import com.example.backend.DTO.TaiKhoanDTO;
import com.example.backend.Entity.BaiVietEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BaiVietRepository extends JpaRepository<BaiVietEntity, Integer> {
    List<BaiVietEntity> findAllByOrderByThoiGianDesc();
    List<BaiVietEntity> findByMaTK(int maTK);  // Tìm bài viết theo mã tài khoản
    List<BaiVietEntity> findByMaTKAndLoaiChiaSe(int maTK, String loaiChiaSe);
    List<BaiVietEntity> findByMaTKAndTrangThai(int maTK, String trangThai);
    List<BaiVietEntity> findByTrangThai(String trangThai);
    TaiKhoanDTO findUserByMaBV(int maBV);
    void deleteByMaBV(int maBV);
}
