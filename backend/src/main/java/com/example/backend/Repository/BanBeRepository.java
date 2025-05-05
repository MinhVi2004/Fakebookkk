package com.example.backend.Repository;

import com.example.backend.Entity.BanBeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BanBeRepository extends JpaRepository<BanBeEntity, Integer> {
  boolean existsByMaTK1AndMaTK2(Integer maTK1, Integer maTK2);

  @Query("SELECT b FROM BanBeEntity b WHERE (b.maTK1 = :userId OR b.maTK2 = :userId) AND b.trangThaiBB = 'Đã Đồng Ý'")
  List<BanBeEntity> findFriendsByUserId(Integer userId);

  Optional<BanBeEntity> findByMaTK1AndMaTK2(Integer maTK1, Integer maTK2);

}