package com.example.backend.Service;

import com.example.backend.DTO.BaiVietDTO;

import java.util.List;

public interface BaiVietService {
    BaiVietDTO getBaiVietById(int maBV);
    List<BaiVietDTO> getAllBaiViet();
    BaiVietDTO createBaiViet(BaiVietDTO baiVietDTO);
    boolean changeStatusBaiViet(int maBV, String trangThai);
      BaiVietDTO updateBaiViet(BaiVietDTO baiVietDTO);
      List<BaiVietDTO> getBaiVietByMaTK(int maTK);
      List<BaiVietDTO> getBaiVietByMaTKAndLoaiChiaSe(int maTK, String loaiChiaSe);
      List<BaiVietDTO> getBaiVietByMaTKAndTrangThai(int maTK, String trangThai);
      List<BaiVietDTO> getBaiVietByTrangThai(String trangThai);
}
