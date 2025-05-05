package com.example.backend.DTO;

import java.util.List;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class BaiVietDTO {
    private int maBV;
    private int maTK;
    private String loaiChiaSe;
    private String noiDung;
    private String thoiGian;
      private String trangThai;
      private List<BaiVietDinhKemDTO> dinhKems;
}
