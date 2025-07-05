package com.example.backend.Service;

import com.example.backend.DTO.BaiVietDTO;
import com.example.backend.DTO.BaiVietDinhKemDTO;
import com.example.backend.Entity.BaiVietEntity;
import com.example.backend.Mapper.BaiVietMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BaiVietServiceImpl implements BaiVietService {

    private final BaiVietRepository baiVietRepository;
    private final BaiVietDinhKemService baiVietDinhKemService;
    private final FileService fileService;

    @Override
    public BaiVietDTO getBaiVietById(int maBV) {
        Optional<BaiVietEntity> entity = baiVietRepository.findById(maBV);
        return entity.map(BaiVietMapper::toDTO).orElse(null);
    }

    @Override
    public List<BaiVietDTO> getAllBaiViet() {
        return baiVietRepository.findAll()
                .stream()
                .map(BaiVietMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BaiVietDTO createBaiViet(BaiVietDTO baiVietDTO) {
        baiVietDTO.setThoiGian(LocalDateTime.now().toString());
        baiVietDTO.setTrangThai("Bình Thường");
        BaiVietEntity entity = BaiVietMapper.toEntity(baiVietDTO);
        return BaiVietMapper.toDTO(baiVietRepository.save(entity));
    }

    @Override
    public BaiVietDTO createBaiVietWithDinhKems(BaiVietDTO baiVietDTO, List<String> dinhKems, List<String> loaiDKs) {
        baiVietDTO.setThoiGian(LocalDateTime.now().toString());
        baiVietDTO.setTrangThai("Bình Thường");

        BaiVietEntity savedEntity = baiVietRepository.save(BaiVietMapper.toEntity(baiVietDTO));

        if (dinhKems != null && !dinhKems.isEmpty()) {
            for (int i = 0; i < dinhKems.size(); i++) {
                try {
                    String base64 = dinhKems.get(i);
                    String type = (loaiDKs != null && loaiDKs.size() > i) ? loaiDKs.get(i) : "unknown";

                    String fileName = fileService.saveFile("DinhKem", base64);

                    BaiVietDinhKemDTO dinhKemDTO = new BaiVietDinhKemDTO();
                    dinhKemDTO.setMaBV(savedEntity.getMaBV());
                    dinhKemDTO.setLinkDK(fileName);
                    dinhKemDTO.setLoaiDK(type);

                    baiVietDinhKemService.createBaiVietDinhKem(dinhKemDTO);

                } catch (IOException | IllegalArgumentException e) {
                    throw new RuntimeException("Không thể xử lý file đính kèm", e);
                }
            }
        }
        return BaiVietMapper.toDTO(savedEntity);
    }

    @Override
    public BaiVietDTO updateBaiViet(BaiVietDTO baiVietDTO) {
        return baiVietRepository.findById(baiVietDTO.getMaBV())
                .map(entity -> {
                    entity.setLoaiChiaSe(baiVietDTO.getLoaiChiaSe());
                    entity.setThoiGian(baiVietDTO.getThoiGian());
                    entity.setNoiDung(baiVietDTO.getNoiDung());
                    entity.setTrangThai(baiVietDTO.getTrangThai());
                    // Cập nhật thêm trường khác nếu cần
                    return BaiVietMapper.toDTO(baiVietRepository.save(entity));
                })
                .orElse(null);
    }

    @Override
    public boolean changeStatusBaiViet(int maBV, String trangThai) {
        return baiVietRepository.findById(maBV).map(entity -> {
            entity.setTrangThai(trangThai);
            baiVietRepository.save(entity);
            return true;
        }).orElse(false);
    }

    @Override
    public boolean changLoaiChiaSeBaiViet(int maBV, String loaiChiaSe) {
        return baiVietRepository.findById(maBV).map(entity -> {
            entity.setLoaiChiaSe(loaiChiaSe);
            baiVietRepository.save(entity);
            return true;
        }).orElse(false);
    }

    @Override
    public List<BaiVietDTO> getBaiVietByMaTK(int maTK) {
        return baiVietRepository.findByMaTK(maTK).stream()
                .map(BaiVietMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BaiVietDTO> getBaiVietByMaTKAndLoaiChiaSe(int maTK, String loaiChiaSe) {
        return baiVietRepository.findByMaTKAndLoaiChiaSe(maTK, loaiChiaSe).stream()
                .map(BaiVietMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BaiVietDTO> getBaiVietByMaTKAndTrangThai(int maTK, String trangThai) {
        return baiVietRepository.findByMaTKAndTrangThai(maTK, trangThai).stream()
                .map(BaiVietMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BaiVietDTO> getBaiVietByTrangThai(String trangThai) {
        return baiVietRepository.findByTrangThai(trangThai).stream()
                .map(BaiVietMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BaiVietDTO> findAllByOrderByThoiGianDesc() {
        return baiVietRepository.findAllByOrderByThoiGianDesc().stream()
                .map(BaiVietMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean deleteBaiViet(int maBV) {
        return baiVietRepository.findById(maBV).map(entity -> {
            entity.setTrangThai("Đã Xóa");
            baiVietRepository.save(entity);

            List<BaiVietDinhKemDTO> dinhKems = baiVietDinhKemService.getAllBaiVietDinhKemByMaBV(maBV);
            for (BaiVietDinhKemDTO dinhKemDTO : dinhKems) {
                File file = new File(dinhKemDTO.getLinkDK());
                if (file.exists()) file.delete();
            }
            return true;
        }).orElse(false);
    }

    @Override
    public List<BaiVietDTO> findAllVisiblePosts(int currentUserId) {
        return baiVietRepository.findAllVisiblePosts(currentUserId).stream()
                .map(BaiVietMapper::toDTO)
                .collect(Collectors.toList());
    }
}
