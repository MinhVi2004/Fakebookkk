package com.example.backend.DTO;

import org.springframework.beans.BeanUtils;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BaiVietDinhKemResponseDTO extends BaiVietDinhKemDTO {
    private String fileData;

    // Hàm tiện ích để copy từ BinhLuanDTO
    public void setFromBaiVietDinhKemDTO(BaiVietDinhKemDTO dto) {
        BeanUtils.copyProperties(dto, this);
    }
}
