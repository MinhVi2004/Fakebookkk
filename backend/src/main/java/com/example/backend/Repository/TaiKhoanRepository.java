package com.example.backend.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.Entity.TaiKhoanEntity;

@Repository
public interface TaiKhoanRepository extends JpaRepository<TaiKhoanEntity, Integer> {
     Optional<TaiKhoanEntity> findByTenDangNhap(String tenDangNhap);
     
     List<TaiKhoanEntity> findByTrangThai(String trangThai);

     boolean existsByTenDangNhap(String tenDangNhap);

     boolean existsBySoDienThoai(String soDienThoai);

}
