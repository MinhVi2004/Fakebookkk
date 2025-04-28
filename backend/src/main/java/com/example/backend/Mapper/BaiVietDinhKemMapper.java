package com.example.backend.Mapper;

import com.example.backend.DTO.BaiVietDinhKemDTO;
import com.example.backend.Entity.BaiVietDinhKemEntity;

public class BaiVietDinhKemMapper {

      // Chuyển từ BaiVietDinhKemEntity sang BaiVietDinhKemDTO
      public static BaiVietDinhKemDTO toDTO(BaiVietDinhKemEntity baiVietDinhKemEntity) {
            BaiVietDinhKemDTO baiVietDinhKemDTO = new BaiVietDinhKemDTO();
            baiVietDinhKemDTO.setMaBV_DK(baiVietDinhKemEntity.getMaBV_DK());
            baiVietDinhKemDTO.setMaBV(baiVietDinhKemEntity.getMaBV());
            baiVietDinhKemDTO.setLinkDK(baiVietDinhKemEntity.getLinkDK());
            baiVietDinhKemDTO.setLoaiDK(baiVietDinhKemEntity.getLoaiDK());
            return baiVietDinhKemDTO;
      }

      // Chuyển từ BaiVietDinhKemDTO sang BaiVietDinhKemEntity
      public static BaiVietDinhKemEntity toEntity(BaiVietDinhKemDTO baiVietDinhKemDTO) {
            BaiVietDinhKemEntity baiVietDinhKemEntity = new BaiVietDinhKemEntity();
            baiVietDinhKemEntity.setMaBV_DK(baiVietDinhKemDTO.getMaBV_DK());
            baiVietDinhKemEntity.setMaBV(baiVietDinhKemDTO.getMaBV());
            baiVietDinhKemEntity.setLinkDK(baiVietDinhKemDTO.getLinkDK());
            baiVietDinhKemEntity.setLoaiDK(baiVietDinhKemDTO.getLoaiDK());
            return baiVietDinhKemEntity;
      }
}
