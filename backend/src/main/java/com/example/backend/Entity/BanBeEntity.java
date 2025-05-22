package com.example.backend.Entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "banbe")
@Getter
@Setter
public class BanBeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaBB")
    private int maBB;

    @Column(name = "MaTK_1")
    private int maTK1;

    @Column(name = "MaTK_2")
    private int maTK2;

    @Column(name = "TrangThaiBB")
    private String trangThaiBB;

    @Column(name = "NgayTao")
    private String ngayTao;
    // ...existing code...
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaTK_1", insertable = false, updatable = false)
    private TaiKhoanEntity nguoiGui; // Người gửi lời mời

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaTK_2", insertable = false, updatable = false)
    private TaiKhoanEntity nguoiNhan; // Người nhận lời mời
    // ...existing code...

}