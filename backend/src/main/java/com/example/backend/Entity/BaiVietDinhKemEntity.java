package com.example.backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "BaiViet_DinhKem")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BaiVietDinhKemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaBV_DK")
    private int maBV_DK;  // Khóa chính

    @Column(name = "MaBV")
    private int maBV;  // Khóa ngoại ánh xạ đến maBV trong BaiVietEntity

    @Column(name = "LinkDK")
    private String linkDK;

    @Column(name = "LoaiDK")
    private String loaiDK;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maBV", insertable = false, updatable = false)
    private BaiVietEntity baiVietEntity;
    // getters and setters
}
