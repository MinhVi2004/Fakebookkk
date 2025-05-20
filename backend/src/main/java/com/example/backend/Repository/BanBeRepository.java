
package com.example.backend.Repository;

import com.example.backend.DTO.BanBeDTO;
import com.example.backend.Entity.BanBeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface BanBeRepository extends JpaRepository<BanBeEntity, Integer> {
        // Tìm danh sách bạn bè của người dùng dựa vào trạng thái
        List<BanBeEntity> findByTrangThaiBBAndMaTK1(String trangThai, int maTK1);

        List<BanBeEntity> findByTrangThaiBBAndMaTK2(String trangThai, int maTK2);

        // Kiểm tra xem quan hệ bạn bè có tồn tại không
        // boolean existsByMaTK1AndMaTK2(Integer maTK1, Integer maTK2);

        // Tìm quan hệ bạn bè giữa hai người dùng
        Optional<BanBeEntity> findByMaTK1AndMaTK2(Integer maTK1, Integer maTK2);

        // Tìm danh sách yêu cầu kết bạn đang chờ từ người gửi
        List<BanBeEntity> findByTrangThaiBBInAndMaTK1OrTrangThaiBBInAndMaTK2(
                        List<String> trangThai1, int maTK1, List<String> trangThai2, int maTK2);
}