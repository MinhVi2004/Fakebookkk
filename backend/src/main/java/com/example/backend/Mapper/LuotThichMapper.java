package com.example.backend.Mapper;

import com.example.backend.DTO.LuotThichDTO;
import com.example.backend.Entity.LuotThichEntity;

public class LuotThichMapper {
    public static LuotThichDTO toDTO(LuotThichEntity luotThichEntity) {
        LuotThichDTO luotThichDTO = new LuotThichDTO();
        // luotThichDTO.setMaLT(luotThichEntity.getMaLT());
        luotThichDTO.setMaBV(luotThichEntity.getMaBV());
        luotThichDTO.setMaTK(luotThichEntity.getMaTK());
        luotThichDTO.setThoiGian(luotThichEntity.getThoiGian());

        return luotThichDTO;
    }
    public static LuotThichEntity toEntity(LuotThichDTO luotThichDTO) {
        LuotThichEntity luotThichEntity = new LuotThichEntity();
        // luotThichEntity.setMaLT(luotThichDTO.getMaLT());
        luotThichEntity.setMaBV(luotThichDTO.getMaBV());
        luotThichEntity.setMaTK(luotThichDTO.getMaTK());
        luotThichEntity.setThoiGian(luotThichDTO.getThoiGian());

        return luotThichEntity;
    }
}
