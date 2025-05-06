// package com.example.backend.Repository;

// import com.example.backend.Entity.BanBeEntity;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.stereotype.Repository;

// import java.util.List;
// import java.util.Optional;

// @Repository
// public interface BanBeRepository extends JpaRepository<BanBeEntity, Integer> {
//   boolean existsByMaTK1AndMaTK2(Integer maTK1, Integer maTK2);

//   @Query("SELECT b FROM BanBeEntity b WHERE (b.maTK1 = :userId OR b.maTK2 = :userId) AND b.trangThaiBB = 'Đã Đồng Ý'")
//   List<BanBeEntity> findFriendsByUserId(Integer userId);

//   Optional<BanBeEntity> findByMaTK1AndMaTK2(Integer maTK1, Integer maTK2);

// }
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

  // Kiểm tra xem quan hệ bạn bè có tồn tại không
  boolean existsByMaTK1AndMaTK2(Integer maTK1, Integer maTK2);

  // Lấy danh sách bạn bè cơ bản (chỉ từ bảng BanBeEntity)
  // @Query("SELECT b FROM BanBeEntity b WHERE (b.maTK1 = :userId OR b.maTK2 =
  // :userId) AND b.trangThaiBB = 'Đã Đồng Ý'")
  // List<BanBeEntity> findFriendsByUserId(@Param("userId") Integer userId);

  @Query(value = "SELECT b.MaBB, b.MaTK_1, b.MaTK_2, b.TrangThaiBB, b.NgayTao, t.HoTen, t.ProfilePic " +
      "FROM banbe b " +
      "JOIN taikhoan t ON b.MaTK_2 = t.MaTK " +
      "WHERE b.MaTK_1 = :userId AND b.TrangThaiBB = 'Đã Đồng Ý'", nativeQuery = true)
  List<Object[]> findFriendsWithDetails(@Param("userId") Integer userId);

  // lấy danh sách người dùng chưa kết bạn

  @Query("SELECT t.MaTK, t.hoTen, t.profilePic " +
      "FROM TaiKhoanEntity t " +
      "WHERE t.MaTK NOT IN ( " +
      "    SELECT CASE WHEN b.maTK1 = :userId THEN b.maTK2 ELSE b.maTK1 END " +
      "    FROM BanBeEntity b " +
      "    WHERE b.maTK1 = :userId OR b.maTK2 = :userId " +
      ") AND t.MaTK != :userId")
  List<Object[]> findSuggestedFriends(@Param("userId") Integer userId);

  @Query(value = "SELECT b.MaBB, b.MaTK_1, b.MaTK_2, b.TrangThaiBB, b.NgayTao, t.HoTen, t.ProfilePic " +
      "FROM banbe b " +
      "JOIN taikhoan t ON b.MaTK_1 = t.MaTK " +
      "WHERE b.MaTK_2 = :userId AND b.TrangThaiBB = 'Chờ Chấp Nhận'", nativeQuery = true)
  List<Object[]> findFriendRequestsWithDetails(@Param("userId") Integer userId);

  @Query(value = "SELECT b.MaBB, " +
      "       CASE WHEN b.MaTK_1 = :userId THEN b.MaTK_2 ELSE b.MaTK_1 END AS friendId, " +
      "       t.HoTen, " +
      "       t.ProfilePic " +
      "FROM banbe b " +
      "JOIN taikhoan t ON t.MaTK = (CASE WHEN b.MaTK_1 = :userId THEN b.MaTK_2 ELSE b.MaTK_1 END) " +
      "WHERE (b.MaTK_1 = :userId OR b.MaTK_2 = :userId) " +
      "  AND b.TrangThaiBB = 'Đã Đồng Ý'", nativeQuery = true)
  List<Object[]> findAllFriends(@Param("userId") Integer userId);

  // Tìm quan hệ bạn bè giữa hai người dùng
  Optional<BanBeEntity> findByMaTK1AndMaTK2(Integer maTK1, Integer maTK2);
}