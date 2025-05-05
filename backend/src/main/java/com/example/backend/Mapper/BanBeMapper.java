package com.example.backend.Mapper;

import com.example.backend.DTO.BanBeDTO;
import com.example.backend.Entity.BanBeEntity;

public class BanBeMapper {
  public static BanBeDTO mapToBanBeDTO(BanBeEntity entity) {
    BanBeDTO dto = new BanBeDTO();
    dto.setMaBB(entity.getMaBB());
    dto.setMaTK1(entity.getMaTK1());
    dto.setMaTK2(entity.getMaTK2());
    dto.setTrangThaiBB(entity.getTrangThaiBB());
    dto.setNgayTao(entity.getNgayTao());
    return dto;
  }

  public static BanBeEntity mapToBanBeEntity(BanBeDTO dto) {
    BanBeEntity entity = new BanBeEntity();
    entity.setMaBB(dto.getMaBB());
    entity.setMaTK1(dto.getMaTK1());
    entity.setMaTK2(dto.getMaTK2());
    entity.setTrangThaiBB(dto.getTrangThaiBB());
    entity.setNgayTao(dto.getNgayTao());
    return entity;
  }
}