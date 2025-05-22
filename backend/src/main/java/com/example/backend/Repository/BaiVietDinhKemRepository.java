package com.example.backend.Repository;

import com.example.backend.Entity.BaiVietDinhKemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BaiVietDinhKemRepository extends JpaRepository<BaiVietDinhKemEntity, Integer> {
    List<BaiVietDinhKemEntity> findByMaBV(int maBV); // Lấy danh sách đính kèm theo mã bài viết
}
