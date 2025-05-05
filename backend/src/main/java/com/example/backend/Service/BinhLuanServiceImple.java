package com.example.backend.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.backend.DTO.BinhLuanDTO;
import com.example.backend.Entity.BaiVietEntity;
import com.example.backend.Entity.BinhLuanEntity;
import com.example.backend.Mapper.BinhLuanMapper;
import com.example.backend.Repository.BinhLuanRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BinhLuanServiceImple implements BinhLuanService {
    private BinhLuanRepository binhLuanRepository;

    @Override
    public List<BinhLuanDTO> findByMaBVAndMaTK(int maBV, int maTK) {
        List<BinhLuanEntity> binhLuanEntities = binhLuanRepository.findByMaBVAndMaTK(maBV, maTK);
        return binhLuanEntities.stream()
                .map(BinhLuanMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BinhLuanDTO> findByMaBV(int maBV) {
        List<BinhLuanEntity> binhLuanEntities = binhLuanRepository.findByMaBV(maBV);
        return binhLuanEntities.stream()
                .map(BinhLuanMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BinhLuanDTO createBinhLuan(int maBV, int maTK, String noiDung) {
        BinhLuanEntity binhLuanEntity = new BinhLuanEntity();
        binhLuanEntity.setMaBV(maBV);
        binhLuanEntity.setMaTK(maTK);
        binhLuanEntity.setNoiDung(noiDung);
        // binhLuanEntity.setThoiGian(LocalDateTime.now());
        BinhLuanEntity savedBinhLuanEntity = binhLuanRepository.save(binhLuanEntity);
        
        return BinhLuanMapper.toDTO(savedBinhLuanEntity);
    }

    @Override
    public void deleteByMaBL(int maBL) {
        binhLuanRepository.deleteById(maBL);
    }
}