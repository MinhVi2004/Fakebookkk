package com.example.backend.Mapper;

import com.example.backend.Entity.TinNhanEntity;
import com.example.backend.DTO.TinNhanDTO;

public class TinNhanMapper {

    public static TinNhanDTO toDTO(TinNhanEntity entity) {
        TinNhanDTO dto = new TinNhanDTO();
        dto.setMaTinNhan(entity.getMaTinNhan());
        dto.setNguoiGuiId(entity.getNguoiGui().getMaTK());
        dto.setNguoiNhanId(entity.getNguoiNhan().getMaTK());
        dto.setNoiDung(entity.getNoiDung());
        dto.setThoiGianGui(entity.getThoiGianGui());
        dto.setDaDoc(entity.isDaDoc());
        dto.setTenNguoiGui(entity.getNguoiGui().getTenDangNhap());
        dto.setTenNguoiNhan(entity.getNguoiNhan().getTenDangNhap());
        return dto;
    }
}
