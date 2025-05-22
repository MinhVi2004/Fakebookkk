package com.example.backend.Service;

import java.util.List;

import com.example.backend.DTO.BinhLuanDTO;

public interface BinhLuanService {
    List<BinhLuanDTO> findByMaBVAndMaTK(int maBV, int maTK);
    List<BinhLuanDTO> findByMaBV(int maBV);
    public BinhLuanDTO createBinhLuan(int maBV, int maTK, String noiDung);
    public void deleteByMaBL(int maBL);
}