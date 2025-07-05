@RestController
@RequestMapping("/api/fakebook/posts")
@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
public class BaiVietController {

    private final BaiVietService baiVietService;
    private final BaiVietDinhKemService baiVietDinhKemService;
    private final TaiKhoanService taiKhoanService;
    private final LuotThichService luotThichService;
    private final BinhLuanService binhLuanService;
    private final FileService fileService;

    @GetMapping("")
    public ResponseEntity<?> getAllPostQL() {
        List<BaiVietDTO> baiVietList = baiVietService.getAllBaiViet();
        List<BaiVietResponseDTO> result = baiVietList.stream()
                .filter(bv -> "Bình Thường".equals(bv.getTrangThai()))
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/home/{maTK}")
    public ResponseEntity<?> getAllPost(@PathVariable int maTK) {
        List<BaiVietDTO> baiVietList = baiVietService.findAllVisiblePosts(maTK);
        List<BaiVietResponseDTO> result = baiVietList.stream()
                .filter(bv -> "Bình Thường".equals(bv.getTrangThai()))
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/profile/{maTK}")
    public ResponseEntity<?> getAllPostById(@PathVariable int maTK) {
        List<BaiVietDTO> baiVietList = baiVietService.findAllByOrderByThoiGianDesc();
        List<BaiVietResponseDTO> result = baiVietList.stream()
                .filter(bv -> bv.getMaTK() == maTK && "Bình Thường".equals(bv.getTrangThai()))
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/hided")
    public ResponseEntity<?> getAllPostHided() {
        List<BaiVietDTO> baiVietList = baiVietService.findAllByOrderByThoiGianDesc();
        List<BaiVietResponseDTO> result = baiVietList.stream()
                .filter(bv -> "Đã Ẩn".equals(bv.getTrangThai()))
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    private BaiVietResponseDTO convertToResponseDTO(BaiVietDTO baiVietDTO) {
        BaiVietResponseDTO responseDTO = new BaiVietResponseDTO();
        TaiKhoanDTO taiKhoanDTO = taiKhoanService.getTaiKhoanById(baiVietDTO.getMaTK());

        responseDTO.setFromBaiVietDTO(baiVietDTO);
        responseDTO.setTaiKhoanBVAndBL(new TaiKhoanBVAndBLDTO(taiKhoanDTO));
        responseDTO.setLuotThichList(luotThichService.findByMaBV(baiVietDTO.getMaBV()));

        List<BaiVietDinhKemDTO> dinhKemDTOs = baiVietDinhKemService.getAllBaiVietDinhKemByMaBV(baiVietDTO.getMaBV());
        List<BaiVietDinhKemResponseDTO> dinhKemResponseList = new ArrayList<>();
        for (BaiVietDinhKemDTO dinhKemDTO : dinhKemDTOs) {
            try {
                String base64FileData = fileService.getFile("DinhKem", dinhKemDTO.getLinkDK());

                BaiVietDinhKemResponseDTO dinhKemResponseDTO = new BaiVietDinhKemResponseDTO();
                dinhKemResponseDTO.setFromBaiVietDinhKemDTO(dinhKemDTO);
                dinhKemResponseDTO.setFileData(base64FileData);

                dinhKemResponseList.add(dinhKemResponseDTO);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        responseDTO.setBaiVietDinhKemResponseList(dinhKemResponseList);

        // Bình luận
        List<BinhLuanDTO> binhLuanList = binhLuanService.findByMaBV(baiVietDTO.getMaBV());
        List<BinhLuanResponseDTO> binhLuanResponseList = new ArrayList<>();
        for (BinhLuanDTO binhLuanDTO : binhLuanList) {
            TaiKhoanDTO taiKhoanBL = taiKhoanService.getTaiKhoanById(binhLuanDTO.getMaTK());
            BinhLuanResponseDTO blResp = new BinhLuanResponseDTO();
            blResp.setFromBinhLuanDTO(binhLuanDTO);
            blResp.setTaiKhoanBVAndBL(new TaiKhoanBVAndBLDTO(taiKhoanBL));
            binhLuanResponseList.add(blResp);
        }
        responseDTO.setBinhLuanList(binhLuanResponseList);

        return responseDTO;
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createPost(
            @RequestPart("baiViet") String baiVietJson,
            @RequestPart(value = "dinhKems", required = false) String dinhKemsJson,
            @RequestPart(value = "loaiDKs", required = false) String loaiDKsJson) {

        try {
            ObjectMapper mapper = new ObjectMapper();

            BaiVietDTO baiVietDTO = mapper.readValue(baiVietJson, BaiVietDTO.class);

            List<String> dinhKemsBase64 = new ArrayList<>();
            if (dinhKemsJson != null && !dinhKemsJson.isEmpty()) {
                dinhKemsBase64 = mapper.readValue(dinhKemsJson, new TypeReference<List<String>>() {});
            }

            List<String> loaiDKs = new ArrayList<>();
            if (loaiDKsJson != null && !loaiDKsJson.isEmpty()) {
                loaiDKs = mapper.readValue(loaiDKsJson, new TypeReference<List<String>>() {});
            }

            BaiVietDTO saved
