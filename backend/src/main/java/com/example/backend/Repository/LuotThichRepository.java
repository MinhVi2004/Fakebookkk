package com.example.backend.Repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.Entity.LuotThichEntity;

public interface LuotThichRepository extends JpaRepository<LuotThichEntity, LuotThichEntity.LuotThichKey>{
    List<LuotThichEntity> findByMaBV(int maBV);
    LuotThichEntity findByMaBVAndMaTK(int maBV, int maTK);
}
