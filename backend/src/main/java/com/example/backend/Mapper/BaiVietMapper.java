package com.example.backend.Mapper;

import com.example.backend.DTO.BaiVietDTO;
import com.example.backend.DTO.BaiVietDinhKemDTO;
import com.example.backend.Entity.BaiVietEntity;
import com.example.backend.Entity.BaiVietDinhKemEntity;

import java.util.List;
import java.util.stream.Collectors;

public class BaiVietMapper {

      // Chuyển từ BaiVietEntity sang BaiVietDTO
      public static BaiVietDTO toDTO(BaiVietEntity baiVietEntity) {
            BaiVietDTO baiVietDTO = new BaiVietDTO();
            baiVietDTO.setMaBV(baiVietEntity.getMaBV());
            baiVietDTO.setMaTK(baiVietEntity.getMaTK());
            baiVietDTO.setLoaiChiaSe(baiVietEntity.getLoaiChiaSe());
            baiVietDTO.setNoiDung(baiVietEntity.getNoiDung());
            baiVietDTO.setThoiGian(baiVietEntity.getThoiGian());
            baiVietDTO.setTrangThai(baiVietEntity.getTrangThai());

            // Ánh xạ dinhKems
            if (baiVietEntity.getDinhKems() != null) {
                  List<BaiVietDinhKemDTO> dinhKemDTOs = baiVietEntity.getDinhKems().stream()
                              .map(BaiVietDinhKemMapper::toDTO)
                              .collect(Collectors.toList());
                  baiVietDTO.setDinhKems(dinhKemDTOs);
            }

            return baiVietDTO;
      }

      // Chuyển từ BaiVietDTO sang BaiVietEntity
      public static BaiVietEntity toEntity(BaiVietDTO baiVietDTO) {
            BaiVietEntity baiVietEntity = new BaiVietEntity();
            baiVietEntity.setMaBV(baiVietDTO.getMaBV());
            baiVietEntity.setMaTK(baiVietDTO.getMaTK());
            baiVietEntity.setLoaiChiaSe(baiVietDTO.getLoaiChiaSe());
            baiVietEntity.setNoiDung(baiVietDTO.getNoiDung());
            baiVietEntity.setThoiGian(baiVietDTO.getThoiGian());
            baiVietEntity.setTrangThai(baiVietDTO.getTrangThai());

            // Ánh xạ dinhKems (nếu có)
            if (baiVietDTO.getDinhKems() != null) {
                  List<BaiVietDinhKemEntity> dinhKemEntities = baiVietDTO.getDinhKems().stream()
                              .map(BaiVietDinhKemMapper::toEntity)
                              .collect(Collectors.toList());
                  baiVietEntity.setDinhKems(dinhKemEntities);
            }

            return baiVietEntity;
      }
}
