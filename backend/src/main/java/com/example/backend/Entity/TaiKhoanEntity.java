package com.example.backend.Entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "taikhoan")
@Getter
@Setter
public class TaiKhoanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaTK")
    private int MaTK;

    @Column(name = "TenDangNhap")
    private String tenDangNhap;
    @Column(name = "ProfilePic")
    private String profilePic;
    @Column(name = "CoverPic")
    private String coverPic;
    @Column(name = "MatKhau")
    private String matKhau;
    @Column(name = "HoTen")
    private String hoTen;
    @Column(name = "GioiTinh")
    private String gioiTinh;
    @Column(name = "SoDienThoai")
    private String soDienThoai;
    @Column(name = "Email")
    private String email;
    @Column(name = "NgaySinh")
    private String ngaySinh;
    @Column(name = "NgayTao")
    private String ngayTao;
    @Column(name = "TrangThai")
    private String trangThai;
    @Column(name = "PhanQuyen")
    private String phanQuyen;
} 
