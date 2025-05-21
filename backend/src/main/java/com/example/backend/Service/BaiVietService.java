package com.example.backend.Service;

import com.example.backend.DTO.BaiVietDTO;

import java.io.File;
import java.util.List;

public interface BaiVietService {
    BaiVietDTO getBaiVietById(int maBV);

    List<BaiVietDTO> getAllBaiViet();

    List<BaiVietDTO> findAllByOrderByThoiGianDesc();

    BaiVietDTO createBaiViet(BaiVietDTO baiVietDTO);

    BaiVietDTO createBaiVietWithDinhKems(BaiVietDTO baiVietDTO, List<String> dinhKems, List<String> loaiDKs);

    boolean changeStatusBaiViet(int maBV, String trangThai);

    boolean changLoaiChiaSeBaiViet(int maBV, String loaiChiaSe);

    BaiVietDTO updateBaiViet(BaiVietDTO baiVietDTO);

    List<BaiVietDTO> getBaiVietByMaTK(int maTK);

    List<BaiVietDTO> getBaiVietByMaTKAndLoaiChiaSe(int maTK, String loaiChiaSe);

    List<BaiVietDTO> getBaiVietByMaTKAndTrangThai(int maTK, String trangThai);

    List<BaiVietDTO> getBaiVietByTrangThai(String trangThai);

    boolean deleteBaiViet(int maBV);

    List<BaiVietDTO> findAllVisiblePosts(int currentUserId);
}
