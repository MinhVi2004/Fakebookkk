package com.example.backend.DTO;
import org.springframework.beans.BeanUtils;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BinhLuanResponseDTO extends BinhLuanDTO{
    private TaiKhoanBVAndBLDTO taiKhoanBVAndBL;

    // Hàm tiện ích để copy từ BinhLuanDTO
    public void setFromBinhLuanDTO(BinhLuanDTO dto) {
        BeanUtils.copyProperties(dto, this);
    }
}
