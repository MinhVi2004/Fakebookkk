package com.example.backend.Mapper;

import com.example.backend.DTO.BinhLuanDTO;
import com.example.backend.Entity.BinhLuanEntity;

public class BinhLuanMapper {
    public static BinhLuanDTO toDTO(BinhLuanEntity binhLuanEntity) {
        BinhLuanDTO binhLuanDTO = new BinhLuanDTO();
        binhLuanDTO.setMaBL(binhLuanEntity.getMaBL());
        binhLuanDTO.setMaBV(binhLuanEntity.getMaBV());
        binhLuanDTO.setMaTK(binhLuanEntity.getMaTK());
        binhLuanDTO.setNoiDung(binhLuanEntity.getNoiDung());
        binhLuanDTO.setThoiGian(binhLuanEntity.getThoiGian());

        return binhLuanDTO;
    }

    public static BinhLuanEntity toEntity(BinhLuanDTO binhLuanDTO) {
        BinhLuanEntity binhLuanEntity = new BinhLuanEntity();
        binhLuanEntity.setMaBL(binhLuanDTO.getMaBL());
        binhLuanEntity.setMaBV(binhLuanDTO.getMaBV());
        binhLuanEntity.setMaTK(binhLuanDTO.getMaTK());
        binhLuanEntity.setNoiDung(binhLuanDTO.getNoiDung());
        binhLuanEntity.setThoiGian(binhLuanDTO.getThoiGian());

        return binhLuanEntity;
    }
}
