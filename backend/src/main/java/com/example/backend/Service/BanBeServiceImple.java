package com.example.backend.Service;

import com.example.backend.DTO.BanBeDTO;
import com.example.backend.DTO.Notification;
import com.example.backend.Entity.BanBeEntity;
import com.example.backend.Entity.TaiKhoanEntity;
import com.example.backend.Mapper.BanBeMapper;
import com.example.backend.Repository.BanBeRepository;
import com.example.backend.Repository.TaiKhoanRepository;

import lombok.AllArgsConstructor;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BanBeServiceImple implements BanBeService {

  private final BanBeRepository banBeRepository;
  private final TaiKhoanRepository taiKhoanRepository;
    private final SimpMessagingTemplate messagingTemplate;

  // Gửi yêu cầu kết bạn
  @Override
  public void sendFriendRequest(Integer senderId, Integer receiverId) {
    Optional<BanBeEntity> existingRequest = banBeRepository.findByMaTK1AndMaTK2(senderId, receiverId);
    Optional<BanBeEntity> reverseRequest = banBeRepository.findByMaTK1AndMaTK2(receiverId, senderId);

    if (existingRequest.isPresent()) {
      BanBeEntity friendRequest = existingRequest.get();
      if (friendRequest.getTrangThaiBB().equals("Chờ Chấp Nhận")) {
        throw new RuntimeException("Nguoi dung " + senderId + " đang gui yeu cau ket ban den ban.");
      }
      if (friendRequest.getTrangThaiBB().equals("Đã Đồng Ý")) {
        throw new RuntimeException("Hai nguoi da la ban be.");
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
        throw new RuntimeException("Nguoi dung " + receiverId + " dang gui yeu cau ket ban den ban.");
      }
      if (reverseFriendRequest.getTrangThaiBB().equals("Đã Đồng Ý")) {
        throw new RuntimeException("Hai nguoi da la ban be.");
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

  // Chấp nhận yêu cầu kết bạn
  @Override
  public void acceptFriendRequest(Integer requestId) {
    BanBeEntity request = banBeRepository.findById(requestId)
        .orElseThrow(() -> new RuntimeException("Loi moi ket ban khong ton tai."));

    request.setTrangThaiBB("Đã Đồng Ý");
    banBeRepository.save(request);
    // / Gửi thông báo qua WebSocket cho người gửi lời mời (sender)
      Integer senderId = request.getMaTK1();
      TaiKhoanEntity sender = taiKhoanRepository.findById(senderId)
          .orElseThrow(() -> new RuntimeException("Khong tim thay nguoi gui yeu cau ket ban."));
      Integer receiverId = request.getMaTK2();
      String message = sender.getHoTen() + " đã chấp nhận lời mời kết bạn !";
      Notification notification = new Notification(message, receiverId);
      messagingTemplate.convertAndSend("/topic/friend-accepted/" + senderId, notification);

  }

  // Từ chối yêu cầu kết bạn
  @Override
  public void rejectFriendRequest(Integer requestId) {
    banBeRepository.deleteById(requestId);
  }

  // hủy yêu cầu kết bạn đã gửi từ người gửi
  @Override
  public void cancelFriendRequest(Integer senderId, Integer receiverId) {
    BanBeEntity friendRequest = banBeRepository.findByMaTK1AndMaTK2(senderId, receiverId)
        .orElseThrow(() -> new RuntimeException("Khong tim thay yeu cau ket ban."));

    if (!friendRequest.getTrangThaiBB().equals("Chờ Chấp Nhận")) {
      throw new RuntimeException("Chi co the huy yeu cau ket ban khi trang thai la 'Cho Chap Nhan'.");
    }

    banBeRepository.delete(friendRequest);
  }

  // xóa bạn bè
  @Override
  public void removeFriend(Integer userId1, Integer userId2) {
    BanBeEntity relationship = banBeRepository.findByMaTK1AndMaTK2(userId1, userId2)
        .or(() -> banBeRepository.findByMaTK1AndMaTK2(userId2, userId1))
        .orElseThrow(() -> new RuntimeException("Khong tim thay moi quan he ban be."));

    if (!relationship.getTrangThaiBB().equals("Đã Đồng Ý")) {
      throw new RuntimeException("Chi co the xoa bạn be khi hau nguoi da la ban be.");
    }

    banBeRepository.delete(relationship);
  }

  // Lấy danh sách bạn bè của người dùng
  @Override
  public List<Map<String, Object>> getFriendList(Integer userId) {
    // Lấy tất cả bạn bè mà userId là người gửi hoặc người nhận
    List<BanBeEntity> friends1 = banBeRepository.findByTrangThaiBBAndMaTK1("Đã Đồng Ý", userId);
    List<BanBeEntity> friends2 = banBeRepository.findByTrangThaiBBAndMaTK2("Đã Đồng Ý", userId);

    List<BanBeEntity> friends = new ArrayList<>();
    friends.addAll(friends1);
    friends.addAll(friends2);
    // chuyển đổi danh sách bạn bè thành danh sách các Map
    return friends.stream().map(banBe -> {
      Map<String, Object> friendDetails = new HashMap<>();
      friendDetails.put("maBB", banBe.getMaBB());

      // Xác định bạn là MaTK_1 hay MaTK_2
      boolean isUserMaTK1 = banBe.getMaTK1() == userId;
      TaiKhoanEntity friendEntity = isUserMaTK1 ? banBe.getNguoiNhan() : banBe.getNguoiGui();

      friendDetails.put("friendId", isUserMaTK1 ? banBe.getMaTK2() : banBe.getMaTK1());
      friendDetails.put("hoTen", friendEntity != null ? friendEntity.getHoTen() : null);
      friendDetails.put("profilePic", friendEntity != null ? friendEntity.getProfilePic() : null);
      return friendDetails;
    }).collect(Collectors.toList());
  }

  // Lấy danh sách lời mời kết bạn với thông tin chi tiết
  @Override
  public List<Map<String, Object>> getFriendRequests(Integer userId) {
    List<BanBeEntity> requests = banBeRepository.findByTrangThaiBBAndMaTK2("Chờ Chấp Nhận", userId);

    return requests.stream().map(banBe -> {
      Map<String, Object> request = new HashMap<>();
      request.put("maBB", banBe.getMaBB());
      request.put("maTK1", banBe.getMaTK1());
      request.put("maTK2", banBe.getMaTK2());
      request.put("trangThaiBB", banBe.getTrangThaiBB());
      request.put("ngayTao", banBe.getNgayTao());
      // Lấy thông tin người gửi
      TaiKhoanEntity sender = banBe.getNguoiGui();
      request.put("hoTen", sender != null ? sender.getHoTen() : null);
      request.put("profilePic", sender != null ? sender.getProfilePic() : null);
      return request;
    }).collect(Collectors.toList());
  }

  // Lấy danh sách gợi ý chưa kết bạn (chỉ trả về các BanBeEntity chưa có
  // trạng thái Đã Đồng Ý)
  @Override
  public List<Map<String, Object>> getSuggestedFriends(Integer userId) {
    List<String> trangThaiList = Arrays.asList("Đã Đồng Ý", "Chờ Chấp Nhận");
    List<BanBeEntity> allRelations = banBeRepository
        .findByTrangThaiBBInAndMaTK1OrTrangThaiBBInAndMaTK2(trangThaiList, userId, trangThaiList, userId);

    Set<Integer> relatedIds = new HashSet<>();
    for (BanBeEntity relation : allRelations) {
      relatedIds.add(relation.getMaTK1());
      relatedIds.add(relation.getMaTK2());
    }
    relatedIds.add(userId); // Không gợi ý chính mình

    List<TaiKhoanEntity> allUsers = taiKhoanRepository.findAll();

    return allUsers.stream()
        .filter(user -> !relatedIds.contains(user.getMaTK()))
        .map(user -> {
          Map<String, Object> map = new HashMap<>();
          map.put("id", user.getMaTK());
          map.put("name", user.getHoTen());
          map.put("profilePic", user.getProfilePic());
          return map;
        }).collect(Collectors.toList());
  }
  // Lấy danh sách yêu cầu kết bạn đang chờ từ người gửi
  @Override
  public List<BanBeEntity> getPendingRequestsBySender(int maTK1) {
  return banBeRepository.findByTrangThaiBBAndMaTK1("Chờ Chấp Nhận", maTK1);
  }

  // Lấy danh sách yêu cầu kết bạn đang chờ từ người nhận
  @Override
  public List<BanBeEntity> getPendingRequestsByReceiver(int maTK2) {
  return banBeRepository.findByTrangThaiBBAndMaTK2("Chờ Chấp Nhận", maTK2);
  }
  // Hai hàm trên có thể sử dụng trong tương lai nếu cần thiết

}