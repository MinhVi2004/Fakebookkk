package com.example.backend.DTO;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TinNhanDTO {

    private Integer maTinNhan;
    private Integer nguoiGuiId;
    private Integer nguoiNhanId;
    private String noiDung;
    private LocalDateTime thoiGianGui;
    private boolean daDoc;

    // hoặc thêm tên người gửi/người nhận nếu cần:
    private String tenNguoiGui;
    private String tenNguoiNhan;
}
