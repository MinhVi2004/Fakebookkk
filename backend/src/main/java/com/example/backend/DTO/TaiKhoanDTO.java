package com.example.backend.DTO;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TaiKhoanDTO {
    private int MaTK;
    private String TenDangNhap;
    private String MatKhau;
    private String ProfilePic;
    private String CoverPic;
    private String HoTen;
    private String GioiTinh;
    private String SoDienThoai;
    private String Email;
    private String NgaySinh;
    private String NgayTao;
    private String TrangThai;
    private String PhanQuyen;
    public TaiKhoanDTO orElseThrow() {
     // TODO Auto-generated method stub
     throw new UnsupportedOperationException("Unimplemented method 'orElseThrow'");
    }
}
