package com.example.backend.Service;

import com.example.backend.DTO.BaiVietDinhKemDTO;

import java.util.List;

public interface BaiVietDinhKemService {
    BaiVietDinhKemDTO getBaiVietDinhKemById(int maBV_DK);
    List<BaiVietDinhKemDTO> getAllBaiVietDinhKemByMaBV(int maBV);
      BaiVietDinhKemDTO createBaiVietDinhKem(BaiVietDinhKemDTO baiVietDinhKemDTO);
      BaiVietDinhKemDTO updateBaiVietDinhKem(BaiVietDinhKemDTO baiVietDinhKemDTO);
      boolean deleteBaiVietDinhKem(int maBV_DK);
      
}
