package com.example.backend.Service;

import com.example.backend.DTO.BaiVietDTO;
import com.example.backend.DTO.BaiVietDinhKemDTO;
import com.example.backend.Entity.BaiVietDinhKemEntity;
import com.example.backend.Entity.BaiVietEntity;
import com.example.backend.Mapper.BaiVietDinhKemMapper;
import com.example.backend.Mapper.BaiVietMapper;
import com.example.backend.Repository.BaiVietDinhKemRepository;
import com.example.backend.Repository.BaiVietRepository;

import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BaiVietServiceImpl implements BaiVietService {

    private BaiVietRepository baiVietRepository;
    private BaiVietDinhKemService baiVietDinhKemService;
    private FileService fileService;

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
public BaiVietDTO createBaiVietWithDinhKems(BaiVietDTO baiVietDTO, List<String> dinhKems, List<String> loaiDKs) {
    // Gán giá trị mặc định
    baiVietDTO.setThoiGian(LocalDateTime.now().toString());
    baiVietDTO.setTrangThai("Bình Thường");

    // Lưu bài viết
    BaiVietEntity baiVietEntity = BaiVietMapper.toEntity(baiVietDTO);
    BaiVietEntity savedBaiVietEntity = baiVietRepository.save(baiVietEntity);

    // Lưu đính kèm (nếu có)
    if (dinhKems != null && !dinhKems.isEmpty()) {
        for (int i = 0; i < dinhKems.size(); i++) {
            try {
                String base64 = dinhKems.get(i); // Đã là base64 thuần
                String type = (loaiDKs != null && loaiDKs.size() > i) ? loaiDKs.get(i) : "unknown";

                String fileName = fileService.saveFile("DinhKem", base64);

                BaiVietDinhKemDTO dinhKem = new BaiVietDinhKemDTO();
                dinhKem.setMaBV(savedBaiVietEntity.getMaBV());
                dinhKem.setLinkDK(fileName);
                dinhKem.setLoaiDK(type);
                baiVietDinhKemService.createBaiVietDinhKem(dinhKem);

            } catch (IOException | IllegalArgumentException e) {
                throw new RuntimeException("Không thể xử lý file đính kèm: ", e);
            }
        }
    }

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
    public boolean changLoaiChiaSeBaiViet(int maBV, String loaiChiaSe) {
        Optional<BaiVietEntity> baiVietEntityOptional = baiVietRepository.findById(maBV);
        if (baiVietEntityOptional.isPresent()) {
            BaiVietEntity baiVietEntity = baiVietEntityOptional.get();
            baiVietEntity.setLoaiChiaSe(loaiChiaSe);
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

    @Override
public List<BaiVietDTO> findAllByOrderByThoiGianDesc() {
    List<BaiVietEntity> baiVietEntities = baiVietRepository.findAllByOrderByThoiGianDesc();
    return baiVietEntities.stream()
            .map(BaiVietMapper::toDTO)
            .collect(Collectors.toList());
}

@Override
public List<BaiVietDTO> findAllVisiblePosts(int currentUserId) {
    List<BaiVietEntity> baiVietEntities = baiVietRepository.findAllVisiblePosts(currentUserId);
    return baiVietEntities.stream()
            .map(BaiVietMapper::toDTO)
            .collect(Collectors.toList());
}

    @Override
    @Transactional
    public boolean deleteBaiViet(int maBV) {
        Optional<BaiVietEntity> baiVietEntityOptional = baiVietRepository.findById(maBV);
        if (baiVietEntityOptional.isPresent()) {
            BaiVietEntity baiVietEntity = baiVietEntityOptional.get();
    
            // Đổi trạng thái thành "DaXoa"
            baiVietEntity.setTrangThai("Đã Xóa");
            baiVietRepository.save(baiVietEntity);
    
            // Nếu muốn xóa file vật lý đính kèm
            List<BaiVietDinhKemDTO> dinhKemsDTO = baiVietDinhKemService.getAllBaiVietDinhKemByMaBV(maBV);
            for (BaiVietDinhKemDTO dto : dinhKemsDTO) {
                String filePath = dto.getLinkDK();
                File file = new File(filePath);
                if (file.exists()) {
                    file.delete();
                }
            }
    
            return true;
        }
        return false;
    }
    
    // @Override
    // public List<BaiVietDTO> findAllVisiblePosts(int currentUserId) {
    //     List<BaiVietEntity> baiVietEntities = baiVietRepository.findAllVisiblePosts(currentUserId);
    //     return baiVietEntities.stream()
    //             .map(BaiVietMapper::toDTO)
    //             .collect(Collectors.toList());
    // }

}
