package com.example.backend.DTO;

import java.time.LocalDateTime;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LuotThichDTO {
    // private int maLT;
    private int maTK;
    private int maBV;
    private LocalDateTime thoiGian;
}
