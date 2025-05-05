
package com.example.backend.DTO;

import java.time.LocalDateTime;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BinhLuanDTO {
    private int maBL;
    private int maTK;
    private int maBV;
    private String noiDung;
    private LocalDateTime thoiGian;
}

