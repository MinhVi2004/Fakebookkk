package com.example.backend.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.Entity.BinhLuanEntity;

public interface BinhLuanRepository extends JpaRepository<BinhLuanEntity, Integer> {
    public List<BinhLuanEntity> findByMaBVAndMaTK(int maBV, int maTK);
    public List<BinhLuanEntity> findByMaBV(int maBV);
    public void deleteById(int maBL);
}
