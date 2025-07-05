@Service
@AllArgsConstructor
public class BaiVietServiceImpl implements BaiVietService {

    private final BaiVietRepository baiVietRepository;
    private final BaiVietDinhKemService baiVietDinhKemService;
    private final FileService fileService;

    @Override
    public BaiVietDTO getBaiVietById(int maBV) {
        return baiVietRepository.findById(maBV)
                .map(BaiVietMapper::toDTO)
                .orElse(null);
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

        BaiVietEntity baiVietEntity = BaiVietMapper.toEntity(baiVietDTO);
        BaiVietEntity saved = baiVietRepository.save(baiVietEntity);
        return BaiVietMapper.toDTO(saved);
    }

    @Override
    public BaiVietDTO createBaiVietWithDinhKems(BaiVietDTO baiVietDTO, List<String> dinhKemsBase64, List<String> loaiDKs) {
        baiVietDTO.setThoiGian(LocalDateTime.now().toString());
        baiVietDTO.setTrangThai("Bình Thường");

        BaiVietEntity baiVietEntity = BaiVietMapper.toEntity(baiVietDTO);
        BaiVietEntity savedBaiViet = baiVietRepository.save(baiVietEntity);

        if (dinhKemsBase64 != null && !dinhKemsBase64.isEmpty()) {
            for (int i = 0; i < dinhKemsBase64.size(); i++) {
                try {
                    String rawBase64 = dinhKemsBase64.get(i);
                    String base64Content = rawBase64.contains(",") ? rawBase64.split(",")[1] : rawBase64;
                    String type = (loaiDKs != null && loaiDKs.size() > i) ? loaiDKs.get(i) : "unknown";

                    // Lưu file và nhận tên file
                    String fileName = fileService.saveFile("DinhKem", base64Content);

                    BaiVietDinhKemDTO dinhKemDTO = new BaiVietDinhKemDTO();
                    dinhKemDTO.setMaBV(savedBaiViet.getMaBV());
                    dinhKemDTO.setLinkDK(fileName);
                    dinhKemDTO.setLoaiDK(type);

                    baiVietDinhKemService.createBaiVietDinhKem(dinhKemDTO);

                } catch (IOException | IllegalArgumentException e) {
                    throw new RuntimeException("Không thể xử lý file đính kèm: ", e);
                }
            }
        }

        return BaiVietMapper.toDTO(savedBaiViet);
    }

    @Override
    public BaiVietDTO updateBaiViet(BaiVietDTO baiVietDTO) {
        return baiVietRepository.findById(baiVietDTO.getMaBV())
                .map(entity -> {
                    entity.setLoaiChiaSe(baiVietDTO.getLoaiChiaSe());
                    entity.setThoiGian(baiVietDTO.getThoiGian());
                    entity.setNoiDung(baiVietDTO.getNoiDung());
                    entity.setTrangThai(baiVietDTO.getTrangThai());
                    return BaiVietMapper.toDTO(baiVietRepository.save(entity));
                }).orElse(null);
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
        return baiVietRepository.findByMaTK(maTK)
                .stream()
                .map(BaiVietMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BaiVietDTO> getBaiVietByMaTKAndLoaiChiaSe(int maTK, String loaiChiaSe) {
        return baiVietRepository.findByMaTKAndLoaiChiaSe(maTK, loaiChiaSe)
                .stream()
                .map(BaiVietMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BaiVietDTO> getBaiVietByMaTKAndTrangThai(int maTK, String trangThai) {
        return baiVietRepository.findByMaTKAndTrangThai(maTK, trangThai)
                .stream()
                .map(BaiVietMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BaiVietDTO> getBaiVietByTrangThai(String trangThai) {
        return baiVietRepository.findByTrangThai(trangThai)
                .stream()
                .map(BaiVietMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BaiVietDTO> findAllByOrderByThoiGianDesc() {
        return baiVietRepository.findAllByOrderByThoiGianDesc()
                .stream()
                .map(BaiVietMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean deleteBaiViet(int maBV) {
        Optional<BaiVietEntity> opt = baiVietRepository.findById(maBV);
        if (opt.isPresent()) {
            BaiVietEntity baiVietEntity = opt.get();

            // Đổi trạng thái thành "Đã Xóa"
            baiVietEntity.setTrangThai("Đã Xóa");
            baiVietRepository.save(baiVietEntity);

            // Xóa file vật lý đính kèm nếu có
            List<BaiVietDinhKemDTO> dinhKems = baiVietDinhKemService.getAllBaiVietDinhKemByMaBV(maBV);
            for (BaiVietDinhKemDTO dto : dinhKems) {
                try {
                    fileService.deleteFile("DinhKem", dto.getLinkDK());
                } catch (Exception ignored) {
                    // Log lỗi nếu cần
                }
            }

            return true;
        }
        return false;
    }

    @Override
    public List<BaiVietDTO> findAllVisiblePosts(int currentUserId) {
        return baiVietRepository.findAllVisiblePosts(currentUserId)
                .stream()
                .map(BaiVietMapper::toDTO)
                .collect(Collectors.toList());
    }
}
