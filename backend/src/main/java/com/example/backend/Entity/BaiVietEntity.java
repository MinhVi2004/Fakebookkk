package com.example.backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Table(name = "BaiViet")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class BaiVietEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaBV")
    private int maBV;  // Khóa chính

    @Column(name = "MaTK")
    private int maTK;  // Khóa ngoại (nếu có)

    @Column(name = "LoaiChiaSe")
    private String loaiChiaSe;

    @Column(name = "NoiDung")
    private String noiDung;

    @Column(name = "ThoiGian")
    private String thoiGian;

    @Column(name = "TrangThai")
    private String trangThai;

    @OneToMany(mappedBy = "baiVietEntity", fetch = FetchType.LAZY)
    private List<BaiVietDinhKemEntity> dinhKems;
    // getters and setters
}
