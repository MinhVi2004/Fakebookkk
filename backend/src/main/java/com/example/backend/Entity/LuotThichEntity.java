package com.example.backend.Entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "luotthich")
@Getter
@Setter
public class LuotThichEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaLT")
    private int maLT;

    @Column(name = "MaTK")
    private int maTK;

    @Column(name = "MaBV")
    private int maBV;

    @Column(name = "ThoiGian")
    private String thoiGian;
}
