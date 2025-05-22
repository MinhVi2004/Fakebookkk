package com.example.backend.Service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.util.Base64;
import java.util.UUID;

@Service
public class FileService {

    // Đảm bảo sử dụng đường dẫn chính xác cho thư mục uploads trong thư mục backend
    private final String uploadDir = Paths.get("uploads").toAbsolutePath().toString();

    private String getExtensionFromMimeType(String mimeType) {
        if (mimeType == null || mimeType.isEmpty()) {
            return null; // Từ chối nếu không có mimeType
        }

        switch (mimeType.toLowerCase()) {
            // Hình ảnh
            case "image/jpeg": return ".jpg";
            case "image/jpg": return ".jpg";
            case "image/png": return ".png";
            case "image/gif": return ".gif";
            case "image/webp": return ".webp";
            case "image/bmp": return ".bmp";
            case "image/svg+xml": return ".svg";
            case "image/tiff": return ".tiff";

            // Video
            case "video/mp4": return ".mp4";
            case "video/x-msvideo": return ".avi";
            case "video/x-matroska": return ".mkv";
            case "video/webm": return ".webm";
            case "video/quicktime": return ".mov";

            default:
                return null; // Từ chối nếu không phải ảnh hoặc video
        }
    }

    public String saveFile(String type, String base64File) throws IOException {
        try {
            // Tách phần dữ liệu base64 nếu có header (ví dụ: data:image/png;base64,...)
            String[] parts = base64File.split(",", 2);
            String pureBase64 = (parts.length == 2) ? parts[1] : base64File;
    
            System.out.println("Base64 sau khi tách: " + pureBase64.substring(0, 10));
    
            // Giải mã base64 thành byte[]
            byte[] fileBytes = Base64.getDecoder().decode(pureBase64);
    
            // Lấy kiểu MIME từ phần header của base64 (nếu có)
            String mimeType = parts[0].split(":")[1].split(";")[0];
    
            // Lấy phần mở rộng dựa trên MIME type
            String extension = getExtensionFromMimeType(mimeType);
            if (extension == null) {
                throw new IllegalArgumentException("Chỉ hỗ trợ file ảnh và video.");
            }
    
            // Tạo tên file duy nhất (có thể thêm timestamp hoặc UUID)
            String fileName = System.currentTimeMillis() + "_" + UUID.randomUUID() + extension;
    
            // Tạo thư mục nếu chưa tồn tại
            Path uploadPath = Paths.get(uploadDir, type).toAbsolutePath();
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
    
            // Lưu dữ liệu vào file
            Path filePath = uploadPath.resolve(fileName);
            Files.write(filePath, fileBytes);  // Lưu mảng byte vào file thực tế
    
            System.out.println("Đã lưu file tại: " + filePath);
    
            return fileName; // Trả về tên file để lưu vào DB
        } catch (IOException | IllegalArgumentException e) {
            // Xử lý lỗi
            e.printStackTrace(); // In ra thông báo lỗi chi tiết
            // Dừng ngay lập tức chương trình (hoặc có thể ném lỗi lên phía trên nếu cần)
            throw new RuntimeException("Lỗi khi lưu file: " + e.getMessage(), e);
        }
    }
    

    public String getFile(String type, String fileName) throws IOException {
        // Đường dẫn tới thư mục (ảnh hoặc video)
        Path filePath = Paths.get(uploadDir, type, fileName).toAbsolutePath();
    
        // Kiểm tra xem file có tồn tại không
        if (!Files.exists(filePath)) {
            return null; // File không tồn tại
        }
    
        // Đọc file thành byte array
        byte[] fileBytes = Files.readAllBytes(filePath);
    
        // Chuyển đổi byte array thành Base64 string
        String base64Encoded = Base64.getEncoder().encodeToString(fileBytes);
    
        // Xác định MIME type của file (vd: image/jpeg, video/mp4)
        String mimeType = Files.probeContentType(filePath);
        if (mimeType == null) {
            mimeType = "application/octet-stream"; // fallback nếu không xác định được MIME type
        }
    
        // Trả về dữ liệu Base64 với MIME type (thường dùng với hình ảnh hoặc video)
        // Chắc chắn rằng chỉ có 1 lần tiền tố "data:image/jpeg;base64," cho chuỗi base64
        String result = "data:" + mimeType + ";base64," + base64Encoded;
    
        // Nếu base64 đã có tiền tố thì chỉ giữ lại phần dữ liệu base64, không thêm lại tiền tố
        if (base64Encoded.startsWith("data:" + mimeType + ";base64,")) {
            result = base64Encoded;
        }
    
        return result;
    }
    public void deleteFile(String type, String fileName) throws IOException {
      // Đường dẫn tới file cần xóa
      Path filePath = Paths.get(uploadDir, type, fileName).toAbsolutePath();
  
      // Kiểm tra xem file có tồn tại không
      if (Files.exists(filePath)) {
          try {
              // Xóa file
              Files.delete(filePath);
              System.out.println("Đã xóa file tại: " + filePath);
          } catch (IOException e) {
              // Xử lý lỗi khi không thể xóa file
              e.printStackTrace();
              throw new RuntimeException("Lỗi khi xóa file: " + e.getMessage(), e);
          }
      } else {
          System.out.println("File không tồn tại tại: " + filePath);
      }
  }
  
}
