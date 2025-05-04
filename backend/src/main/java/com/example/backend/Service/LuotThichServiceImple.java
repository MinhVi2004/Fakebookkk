package com.example.backend.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.backend.DTO.LuotThichDTO;
import com.example.backend.Entity.LuotThichEntity;
import com.example.backend.Mapper.LuotThichMapper;
import com.example.backend.Repository.LuotThichRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LuotThichServiceImple implements LuotThichService {
    private LuotThichRepository luotThichRepository;

    @Override
    public List<LuotThichDTO> findByMaBV(int maBV) {
        List<LuotThichEntity> luotThichEntities = luotThichRepository.findByMaBV(maBV);
        return luotThichEntities.stream()
                .map(LuotThichMapper::toDTO)
                .collect(Collectors.toList());
    }

    // @Override
    // public LuotThichDTO findByMaBVAndMaTK(int maBV, int MaTK) {
    //     LuotThichEntity luotThichEntity = luotThichRepository.findByMaBVAndMaTK(maBV, MaTK);
    //     return LuotThichMapper.toDTO(luotThichEntity);
    // }

    @Override
    public boolean toggleLuotThich(LuotThichDTO luotThichDTO) {
        LuotThichEntity existing = luotThichRepository.findByMaBVAndMaTK(
            luotThichDTO.getMaBV(), luotThichDTO.getMaTK()
        );

        if (existing != null) {
            luotThichRepository.delete(existing); // Xóa like cũ
            return false; // Đã unlike
        } else {
            LuotThichEntity newLike = new LuotThichEntity();
            newLike.setMaBV(luotThichDTO.getMaBV());
            newLike.setMaTK(luotThichDTO.getMaTK());
            newLike.setThoiGian(LocalDateTime.now());

            luotThichRepository.save(newLike);
            return true; // Đã like
        }
    }
}