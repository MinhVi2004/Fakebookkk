package com.example.backend.DTO;

import lombok.*;

@Getter
@Setter
public class TaiKhoanBVAndBLDTO {
    private int MaTK;
    private String ProfilePic;
    private String HoTen;
    private String TrangThai;

    public TaiKhoanBVAndBLDTO(TaiKhoanDTO dto) {
        this.setMaTK(dto.getMaTK());
        this.setHoTen(dto.getHoTen());
        this.setProfilePic(dto.getProfilePic());
        this.setTrangThai(dto.getTrangThai());
    }
}
