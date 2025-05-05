package com.example.backend.Service;

import com.example.backend.DTO.BanBeDTO;
import com.example.backend.Entity.BanBeEntity;
import com.example.backend.Mapper.BanBeMapper;
import com.example.backend.Repository.BanBeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BanBeServiceImple implements BanBeService {

  private final BanBeRepository banBeRepository;

  // Gửi yêu cầu kết bạn
  @Override
  public void sendFriendRequest(Integer senderId, Integer receiverId) {
    // Kiểm tra xem đã tồn tại mối quan hệ giữa hai người dùng hay chưa
    Optional<BanBeEntity> existingRequest = banBeRepository.findByMaTK1AndMaTK2(senderId, receiverId);
    Optional<BanBeEntity> reverseRequest = banBeRepository.findByMaTK1AndMaTK2(receiverId, senderId);

    // Nếu người dùng 1 đã gửi yêu cầu kết bạn cho người dùng 2
    if (existingRequest.isPresent()) {
      BanBeEntity friendRequest = existingRequest.get();

      // Nếu trạng thái là "Chờ Chấp Nhận", thông báo rằng yêu cầu đang chờ xử lý
      if (friendRequest.getTrangThaiBB().equals("Chờ Chấp Nhận")) {
        throw new RuntimeException("Người dùng " + senderId + " đang gửi yêu cầu kết bạn đến bạn.");
      }

      // Nếu trạng thái là "Đã Đồng Ý", thông báo rằng hai người đã là bạn bè
      if (friendRequest.getTrangThaiBB().equals("Đã Đồng Ý")) {
        throw new RuntimeException("Hai người đã là bạn bè.");
      }

      // Nếu trạng thái là "Không Đồng Ý" hoặc "Đã Xóa", cập nhật lại trạng thái
      if (friendRequest.getTrangThaiBB().equals("Không Đồng Ý") || friendRequest.getTrangThaiBB().equals("Đã Xóa")) {
        friendRequest.setTrangThaiBB("Chờ Chấp Nhận");
        friendRequest.setNgayTao(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        banBeRepository.save(friendRequest);
        return;
      }
    }

    // Nếu người dùng 2 đã gửi yêu cầu kết bạn cho người dùng 1
    if (reverseRequest.isPresent()) {
      BanBeEntity reverseFriendRequest = reverseRequest.get();

      // Nếu trạng thái là "Chờ Chấp Nhận", thông báo rằng người dùng 1 đang gửi yêu
      // cầu kết bạn
      if (reverseFriendRequest.getTrangThaiBB().equals("Chờ Chấp Nhận")) {
        throw new RuntimeException("Người dùng " + receiverId + " đang gửi yêu cầu kết bạn đến bạn.");
      }

      // Nếu trạng thái là "Đã Đồng Ý", thông báo rằng hai người đã là bạn bè
      if (reverseFriendRequest.getTrangThaiBB().equals("Đã Đồng Ý")) {
        throw new RuntimeException("Hai người đã là bạn bè.");
      }

      // Nếu trạng thái là "Không Đồng Ý" hoặc "Đã Xóa", cập nhật lại trạng thái
      if (reverseFriendRequest.getTrangThaiBB().equals("Không Đồng Ý")
          || reverseFriendRequest.getTrangThaiBB().equals("Đã Xóa")) {
        reverseFriendRequest.setTrangThaiBB("Chờ Chấp Nhận");
        reverseFriendRequest.setNgayTao(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        banBeRepository.save(reverseFriendRequest);
        return;
      }
    }

    // Tạo yêu cầu kết bạn mới nếu không tồn tại bản ghi
    BanBeEntity friendRequest = new BanBeEntity();
    friendRequest.setMaTK1(senderId);
    friendRequest.setMaTK2(receiverId);
    friendRequest.setTrangThaiBB("Chờ Chấp Nhận");

    // Lấy thời gian hiện tại
    String ngayTao = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    friendRequest.setNgayTao(ngayTao);

    banBeRepository.save(friendRequest);
  }

  // /Xử lý phản hồi yêu cầu kết bạn
  @Override
  public void respondToFriendRequest(Integer senderId, Integer receiverId, String response) {
    BanBeEntity friendRequest = banBeRepository.findByMaTK1AndMaTK2(senderId, receiverId)
        .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu kết bạn."));

    // Kiểm tra trạng thái hiện tại
    if (friendRequest.getTrangThaiBB().equals("Đã Đồng Ý")) {
      throw new RuntimeException("Hai người đã là bạn bè. Không thể thay đổi trạng thái về 'Không Đồng Ý'.");
    }

    // Cập nhật trạng thái dựa trên phản hồi
    if (response.equalsIgnoreCase("Đã Đồng Ý")) {
      friendRequest.setTrangThaiBB("Đã Đồng Ý");
    } else if (response.equalsIgnoreCase("Không Đồng Ý")) {
      friendRequest.setTrangThaiBB("Không Đồng Ý");
    } else {
      throw new RuntimeException("Phản hồi không hợp lệ. Chỉ chấp nhận 'Đã Đồng Ý' hoặc 'Không Đồng Ý'.");
    }

    banBeRepository.save(friendRequest);
  }

  @Override
  public List<BanBeDTO> getFriendList(Integer userId) {
    List<BanBeEntity> friends = banBeRepository.findFriendsByUserId(userId);
    return friends.stream()
        .map(BanBeMapper::mapToBanBeDTO)
        .collect(Collectors.toList());
  }

  // Hủy yêu cầu kết bạn
  @Override
  public void cancelFriendRequest(Integer senderId, Integer receiverId) {
    BanBeEntity friendRequest = banBeRepository.findByMaTK1AndMaTK2(senderId, receiverId)
        .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu kết bạn."));

    // Kiểm tra trạng thái hiện tại
    if (!friendRequest.getTrangThaiBB().equals("Chờ Chấp Nhận")) {
      throw new RuntimeException("Chỉ có thể hủy yêu cầu kết bạn khi trạng thái là 'Chờ Chấp Nhận'.");
    }

    banBeRepository.delete(friendRequest);
  }

  @Override
  public void removeFriend(Integer userId1, Integer userId2) {
    BanBeEntity relationship = banBeRepository.findByMaTK1AndMaTK2(userId1, userId2)
        .or(() -> banBeRepository.findByMaTK1AndMaTK2(userId2, userId1))
        .orElseThrow(() -> new RuntimeException("Không tìm thấy mối quan hệ bạn bè."));

    // Kiểm tra trạng thái hiện tại
    if (!relationship.getTrangThaiBB().equals("Đã Đồng Ý")) {
      throw new RuntimeException("Chỉ có thể xóa bạn bè khi hai người đã là bạn bè.");
    }

    banBeRepository.delete(relationship);
  }
}