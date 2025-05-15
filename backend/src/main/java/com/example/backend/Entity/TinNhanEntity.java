package com.example.backend.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "TinNhan")
public class TinNhanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maTinNhan;

    @ManyToOne
    @JoinColumn(name = "nguoiGui", referencedColumnName = "MaTK", nullable = false)
    private TaiKhoanEntity nguoiGui;

    @ManyToOne
    @JoinColumn(name = "nguoiNhan", referencedColumnName = "MaTK", nullable = false)
    private TaiKhoanEntity nguoiNhan;

    @Column(columnDefinition = "TEXT")
    private String noiDung;

    @Column(name = "thoiGianGui")
    private LocalDateTime thoiGianGui = LocalDateTime.now();
    
      @Column(name = "daDoc")
    private boolean daDoc = false;

    // Getters and Setters
}

