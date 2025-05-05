package com.example.backend.Entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "luotthich")
@IdClass(LuotThichEntity.LuotThichKey.class)
public class LuotThichEntity {

    @Id
    @Column(name = "MaTK")
    private int maTK;

    @Id
    @Column(name = "MaBV")
    private int maBV;

    @Column(name = "ThoiGian")
    private LocalDateTime thoiGian;

    // --- Static inner class làm IdClass ---
    public static class LuotThichKey implements Serializable {
        private int maTK;
        private int maBV;

        public LuotThichKey() {}

        public LuotThichKey(int maTK, int maBV) {
            this.maTK = maTK;
            this.maBV = maBV;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof LuotThichKey)) return false;
            LuotThichKey that = (LuotThichKey) o;
            return maTK == that.maTK && maBV == that.maBV;
        }

        @Override
        public int hashCode() {
            return Objects.hash(maTK, maBV);
        }
    }

    // Getters/setters nếu cần
}

