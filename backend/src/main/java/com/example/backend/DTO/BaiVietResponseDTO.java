package com.example.backend.DTO;

import java.util.List;

import org.springframework.beans.BeanUtils;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BaiVietResponseDTO extends BaiVietDTO{
    private TaiKhoanBVAndBLDTO taiKhoanBVAndBL;
    private List<BinhLuanResponseDTO> binhLuanList;
    private List<BaiVietDinhKemResponseDTO> baiVietDinhKemResponseList;
    private List<LuotThichDTO> luotThichList;

    // Hàm tiện lợi để set từ BaiVietDTO
    public void setFromBaiVietDTO(BaiVietDTO dto) {
        BeanUtils.copyProperties(dto, this);
    }
}
