package com.example.backend.Service;

import com.example.backend.DTO.BanBeDTO;
import com.example.backend.Entity.BanBeEntity;
import com.example.backend.Mapper.BanBeMapper;
import com.example.backend.Repository.BanBeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Map;

@Service
@AllArgsConstructor
public class BanBeServiceImple implements BanBeService {

  private final BanBeRepository banBeRepository;

  @Override
  public List<BanBeEntity> getPendingRequestsBySender(int maTK1) {
    return banBeRepository.findByTrangThaiBBAndMaTK1("Chờ Đồng Ý", maTK1);
  }

  @Override
  public List<BanBeEntity> getPendingRequestsByReceiver(int maTK2) {
    return banBeRepository.findByTrangThaiBBAndMaTK2("Chờ Đồng Ý", maTK2);
  }

  // Gửi yêu cầu kết bạn
  @Override
  public void sendFriendRequest(Integer senderId, Integer receiverId) {
    Optional<BanBeEntity> existingRequest = banBeRepository.findByMaTK1AndMaTK2(senderId, receiverId);
    Optional<BanBeEntity> reverseRequest = banBeRepository.findByMaTK1AndMaTK2(receiverId, senderId);

    if (existingRequest.isPresent()) {
      BanBeEntity friendRequest = existingRequest.get();
      if (friendRequest.getTrangThaiBB().equals("Chờ Chấp Nhận")) {
        throw new RuntimeException("Người dùng " + senderId + " đang gửi yêu cầu kết bạn đến bạn.");
      }
      if (friendRequest.getTrangThaiBB().equals("Đã Đồng Ý")) {
        throw new RuntimeException("Hai người đã là bạn bè.");
      }
      if (friendRequest.getTrangThaiBB().equals("Không Đồng Ý") || friendRequest.getTrangThaiBB().equals("Đã Xóa")) {
        friendRequest.setTrangThaiBB("Chờ Chấp Nhận");
        friendRequest.setNgayTao(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        banBeRepository.save(friendRequest);
        return;
      }
    }

    if (reverseRequest.isPresent()) {
      BanBeEntity reverseFriendRequest = reverseRequest.get();
      if (reverseFriendRequest.getTrangThaiBB().equals("Chờ Chấp Nhận")) {
        throw new RuntimeException("Người dùng " + receiverId + " đang gửi yêu cầu kết bạn đến bạn.");
      }
      if (reverseFriendRequest.getTrangThaiBB().equals("Đã Đồng Ý")) {
        throw new RuntimeException("Hai người đã là bạn bè.");
      }
      if (reverseFriendRequest.getTrangThaiBB().equals("Không Đồng Ý")
          || reverseFriendRequest.getTrangThaiBB().equals("Đã Xóa")) {
        reverseFriendRequest.setTrangThaiBB("Chờ Chấp Nhận");
        reverseFriendRequest.setNgayTao(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        banBeRepository.save(reverseFriendRequest);
        return;
      }
    }

    BanBeEntity friendRequest = new BanBeEntity();
    friendRequest.setMaTK1(senderId);
    friendRequest.setMaTK2(receiverId);
    friendRequest.setTrangThaiBB("Chờ Chấp Nhận");
    friendRequest.setNgayTao(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    banBeRepository.save(friendRequest);
  }

  // Lấy danh sách bạn bè với thông tin chi tiết
  @Override
  public List<Map<String, Object>> getFriendList(Integer userId) {
    List<Object[]> results = banBeRepository.findAllFriends(userId);

    // Chuyển đổi kết quả thành danh sách Map
    return results.stream().map(row -> {
      Map<String, Object> friendDetails = new HashMap<>();
      friendDetails.put("maBB", row[0]); // ID của quan hệ bạn bè
      friendDetails.put("friendId", row[1]); // ID của bạn bè
      friendDetails.put("hoTen", row[2]); // Tên bạn bè
      friendDetails.put("profilePic", row[3]); // Ảnh đại diện bạn bè
      return friendDetails;
    }).collect(Collectors.toList());
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

  // Lấy danh sách người dùng chưa kết bạn
  @Override
  public List<Map<String, Object>> getSuggestedFriends(Integer userId) {
    List<Object[]> results = banBeRepository.findSuggestedFriends(userId);

    return results.stream().map(row -> {
      Map<String, Object> suggestedFriend = new HashMap<>();
      suggestedFriend.put("id", row[0]); // MaTK
      suggestedFriend.put("name", row[1]); // HoTen
      suggestedFriend.put("profilePic", row[2]); // ProfilePic
      return suggestedFriend;
    }).collect(Collectors.toList());
  }

  @Override
  public List<Map<String, Object>> getFriendRequests(Integer userId) {
    List<Object[]> results = banBeRepository.findFriendRequestsWithDetails(userId);

    // Chuyển đổi dữ liệu thô thành danh sách Map
    return results.stream().map(row -> {
      Map<String, Object> request = new HashMap<>();
      request.put("maBB", row[0]); // ID của lời mời
      request.put("maTK1", row[1]); // ID người gửi
      request.put("maTK2", row[2]); // ID người nhận
      request.put("trangThaiBB", row[3]); // Trạng thái
      request.put("ngayTao", row[4]); // Ngày tạo
      request.put("hoTen", row[5]); // Tên người gửi
      request.put("profilePic", row[6]); // Ảnh đại diện người gửi
      return request;
    }).collect(Collectors.toList());
  }

  // Chấp nhận lời mời kết bạn
  @Override
  public void acceptFriendRequest(Integer requestId) {
    // Tìm lời mời kết bạn theo ID
    BanBeEntity request = banBeRepository.findById(requestId)
        .orElseThrow(() -> new RuntimeException("Lời mời kết bạn không tồn tại."));

    // Cập nhật trạng thái lời mời kết bạn thành "Đã Đồng Ý"
    request.setTrangThaiBB("Đã Đồng Ý");
    banBeRepository.save(request);
  }

  @Override
  public void rejectFriendRequest(Integer requestId) {
    banBeRepository.deleteById(requestId);
  }

}