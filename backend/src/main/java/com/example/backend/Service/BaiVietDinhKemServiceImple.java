package com.example.backend.Service;

import com.example.backend.DTO.BaiVietDinhKemDTO;
import com.example.backend.Entity.BaiVietDinhKemEntity;
import com.example.backend.Mapper.BaiVietDinhKemMapper;
import com.example.backend.Repository.BaiVietDinhKemRepository;
import com.example.backend.Service.BaiVietDinhKemService;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BaiVietDinhKemServiceImple implements BaiVietDinhKemService {

    private BaiVietDinhKemRepository baiVietDinhKemRepository;

    @Override
    public BaiVietDinhKemDTO getBaiVietDinhKemById(int maBV_DK) {
        Optional<BaiVietDinhKemEntity> baiVietDinhKemEntityOptional = baiVietDinhKemRepository.findById(maBV_DK);
        return baiVietDinhKemEntityOptional.map(BaiVietDinhKemMapper::toDTO).orElse(null);
    }

    @Override
    public List<BaiVietDinhKemDTO> getAllBaiVietDinhKemByMaBV(int maBV) {
        List<BaiVietDinhKemEntity> baiVietDinhKemEntities = baiVietDinhKemRepository.findByMaBV(maBV);
        return baiVietDinhKemEntities.stream()
                .map(BaiVietDinhKemMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BaiVietDinhKemDTO createBaiVietDinhKem(BaiVietDinhKemDTO baiVietDinhKemDTO) {
        BaiVietDinhKemEntity baiVietDinhKemEntity = BaiVietDinhKemMapper.toEntity(baiVietDinhKemDTO);
        BaiVietDinhKemEntity savedBaiVietDinhKemEntity = baiVietDinhKemRepository.save(baiVietDinhKemEntity);
        return BaiVietDinhKemMapper.toDTO(savedBaiVietDinhKemEntity);
    }

    @Override
    public BaiVietDinhKemDTO updateBaiVietDinhKem(BaiVietDinhKemDTO baiVietDinhKemDTO) {
        Optional<BaiVietDinhKemEntity> baiVietDinhKemEntityOptional = baiVietDinhKemRepository.findById(baiVietDinhKemDTO.getMaBV_DK());
        if (baiVietDinhKemEntityOptional.isPresent()) {
            BaiVietDinhKemEntity baiVietDinhKemEntity = baiVietDinhKemEntityOptional.get();
            baiVietDinhKemEntity.setLinkDK(baiVietDinhKemDTO.getLinkDK());
            baiVietDinhKemEntity.setLoaiDK(baiVietDinhKemDTO.getLoaiDK());
            // Cập nhật các trường khác nếu cần thiết

            BaiVietDinhKemEntity updatedBaiVietDinhKemEntity = baiVietDinhKemRepository.save(baiVietDinhKemEntity);
            return BaiVietDinhKemMapper.toDTO(updatedBaiVietDinhKemEntity);
        }
        return null;
    }

    @Override
    public boolean deleteBaiVietDinhKem(int maBV_DK) {
        Optional<BaiVietDinhKemEntity> baiVietDinhKemEntityOptional = baiVietDinhKemRepository.findById(maBV_DK);
        if (baiVietDinhKemEntityOptional.isPresent()) {
            baiVietDinhKemRepository.delete(baiVietDinhKemEntityOptional.get());
            return true;
        }
        return false;
    }
}
