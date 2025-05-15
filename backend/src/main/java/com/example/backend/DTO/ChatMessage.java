package com.example.backend.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter
public class ChatMessage {
    private Integer maTinNhan;    // sẽ được server gán khi lưu DB
    private Integer nguoiGuiId;
    private Integer nguoiNhanId;
    private String noiDung;
    private String timestamp; 
}

