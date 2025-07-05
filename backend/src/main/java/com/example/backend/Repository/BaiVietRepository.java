package com.example.backend.Repository;

import com.example.backend.DTO.TaiKhoanDTO;
import com.example.backend.Entity.BaiVietEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BaiVietRepository extends JpaRepository<BaiVietEntity, Integer> {
    // List<BaiVietEntity> findAllByOrderByThoiGianDesc();
    List<BaiVietEntity> findByMaTK(int maTK);  // Tìm bài viết theo mã tài khoản
    List<BaiVietEntity> findByMaTKAndLoaiChiaSe(int maTK, String loaiChiaSe);
    List<BaiVietEntity> findByMaTKAndTrangThai(int maTK, String trangThai);
    List<BaiVietEntity> findByTrangThai(String trangThai);
    TaiKhoanDTO findUserByMaBV(int maBV);
    void deleteByMaBV(int maBV);
// KHÔNG dùng Pageable
List<BaiVietEntity> findAllByOrderByThoiGianDesc();

@Query(""" 
    SELECT bv
    FROM BaiVietEntity bv
    WHERE (
        bv.loaiChiaSe = 'Tất Cả'
        OR (
            bv.loaiChiaSe = 'Bạn Bè'
            AND (
                bv.maTK = :currentUserId
                OR EXISTS (
                    SELECT 1
                    FROM BanBeEntity bb
                    WHERE (
                        (bb.maTK1 = :currentUserId AND bb.maTK2 = bv.maTK)
                        OR (bb.maTK2 = :currentUserId AND bb.maTK1 = bv.maTK)
                    )
                    AND bb.trangThaiBB = 'Đã Đồng Ý'
                )
            )
        )
        OR (bv.loaiChiaSe = 'Chỉ Mình Tôi' AND bv.maTK = :currentUserId)
    )
    ORDER BY bv.thoiGian DESC
""")
List<BaiVietEntity> findAllVisiblePosts(@Param("currentUserId") Integer currentUserId);

}
