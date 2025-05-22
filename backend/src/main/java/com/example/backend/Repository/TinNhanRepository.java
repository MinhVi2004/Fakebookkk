package com.example.backend.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.backend.Entity.TaiKhoanEntity;
import com.example.backend.Entity.TinNhanEntity;
@Repository
public interface TinNhanRepository extends JpaRepository<TinNhanEntity, Integer> {

      @Query("""
            SELECT t 
            FROM TinNhanEntity t 
            WHERE (t.nguoiGui.MaTK = :id1 AND t.nguoiNhan.MaTK = :id2)
            OR (t.nguoiGui.MaTK = :id2 AND t.nguoiNhan.MaTK = :id1)
            ORDER BY t.thoiGianGui ASC
            """)
      List<TinNhanEntity> findTinNhanGiuaHaiNguoi(
            @Param("id1") Integer id1,
            @Param("id2") Integer id2
      );

}


