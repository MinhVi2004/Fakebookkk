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
    private int maTK_1;

    @Column(name = "MaTK_2")
    private int maTK_2;

    @Column(name = "TrangThaiBB")
    private String trangThaiBB;

    @Column(name = "NgayTao")
    private String ngayTao;
}
