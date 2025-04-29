package com.example.backend.Service;

import com.example.backend.DTO.BaiVietDTO;
import com.example.backend.Entity.BaiVietEntity;
import com.example.backend.Mapper.BaiVietMapper;
import com.example.backend.Repository.BaiVietRepository;
import com.example.backend.Service.BaiVietService;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BaiVietServiceImpl implements BaiVietService {

    private BaiVietRepository baiVietRepository;      


    @Override
    public BaiVietDTO getBaiVietById(int maBV) {
        Optional<BaiVietEntity> baiVietEntityOptional = baiVietRepository.findById(maBV);
        return baiVietEntityOptional.map(BaiVietMapper::toDTO).orElse(null);
    }

    @Override
    public List<BaiVietDTO> getAllBaiViet() {
        List<BaiVietEntity> baiVietEntities = baiVietRepository.findAll();
        return baiVietEntities.stream()
                .map(BaiVietMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BaiVietDTO createBaiViet(BaiVietDTO baiVietDTO) {
        // Gán các giá trị mặc định
        baiVietDTO.setThoiGian(java.time.LocalDateTime.now().toString());
        baiVietDTO.setTrangThai("Bình Thường");

        BaiVietEntity baiVietEntity = BaiVietMapper.toEntity(baiVietDTO);
        BaiVietEntity savedBaiVietEntity = baiVietRepository.save(baiVietEntity);
        return BaiVietMapper.toDTO(savedBaiVietEntity);
    }

    @Override
    public BaiVietDTO updateBaiViet(BaiVietDTO baiVietDTO) {
        Optional<BaiVietEntity> baiVietEntityOptional = baiVietRepository.findById(baiVietDTO.getMaBV());
        if (baiVietEntityOptional.isPresent()) {
            BaiVietEntity baiVietEntity = baiVietEntityOptional.get();
            baiVietEntity.setLoaiChiaSe(baiVietDTO.getLoaiChiaSe());
            baiVietEntity.setThoiGian(baiVietDTO.getThoiGian());
            baiVietEntity.setNoiDung(baiVietDTO.getNoiDung());
            baiVietEntity.setTrangThai(baiVietDTO.getTrangThai());
            // Cập nhật các trường khác nếu cần thiết

            BaiVietEntity updatedBaiVietEntity = baiVietRepository.save(baiVietEntity);
            return BaiVietMapper.toDTO(updatedBaiVietEntity);
        }
        return null;
    }

    @Override
    public boolean changeStatusBaiViet(int maBV, String trangThai) {
        Optional<BaiVietEntity> baiVietEntityOptional = baiVietRepository.findById(maBV);
        if (baiVietEntityOptional.isPresent()) {
            BaiVietEntity baiVietEntity = baiVietEntityOptional.get();
            baiVietEntity.setTrangThai(trangThai);
            baiVietRepository.save(baiVietEntity);
            return true;
        }
        return false;
    }

    @Override
    public List<BaiVietDTO> getBaiVietByMaTK(int maTK) {
        List<BaiVietEntity> baiVietEntities = baiVietRepository.findByMaTK(maTK);
        return baiVietEntities.stream()
                .map(BaiVietMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BaiVietDTO> getBaiVietByMaTKAndLoaiChiaSe(int maTK, String loaiChiaSe) {
        List<BaiVietEntity> baiVietEntities = baiVietRepository.findByMaTKAndLoaiChiaSe(maTK, loaiChiaSe);
        return baiVietEntities.stream()
                .map(BaiVietMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BaiVietDTO> getBaiVietByMaTKAndTrangThai(int maTK, String trangThai) {
        List<BaiVietEntity> baiVietEntities = baiVietRepository.findByMaTKAndTrangThai(maTK, trangThai);
        return baiVietEntities.stream()
                .map(BaiVietMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BaiVietDTO> getBaiVietByTrangThai(String trangThai) {
        List<BaiVietEntity> baiVietEntities = baiVietRepository.findByTrangThai(trangThai);
        return baiVietEntities.stream()
                .map(BaiVietMapper::toDTO)
                .collect(Collectors.toList());
    }
}
