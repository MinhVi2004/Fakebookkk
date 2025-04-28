package com.example.backend.Entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "binhluan")
@Getter
@Setter
public class BinhLuanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaBL")
    private int maBL;

    @Column(name = "MaTK")
    private int maTK;

    @Column(name = "MaBV")
    private int maBV;

    @Column(name = "NoiDung")
    private String noiDung;

    @Column(name = "ThoiGian")
    private String thoiGian;
}
