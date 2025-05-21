package com.example.backend.Controller;

import com.example.backend.DTO.BanBeDTO;
import com.example.backend.Entity.BanBeEntity;
import com.example.backend.Service.BanBeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/friends")
@CrossOrigin(origins = "http://localhost:3000") // Thêm dòng này react được phép truy cập

@AllArgsConstructor
public class BanBeController {

  private final BanBeService banBeService;

  // 1. API: Gửi yêu cầu kết bạn
  @PostMapping("/send-request")
  public ResponseEntity<?> sendFriendRequest(@RequestParam Integer senderId, @RequestParam Integer receiverId) {
    try {
      banBeService.sendFriendRequest(senderId, receiverId);
      return ResponseEntity.ok("Yeu cau ket ban duoc gui.");
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  // 2. API: Chấp nhận lời mời kết bạn
  @PostMapping("/accept/{requestId}")
  public ResponseEntity<?> acceptFriendRequest(@PathVariable Integer requestId) {
    banBeService.acceptFriendRequest(requestId);
    return ResponseEntity.ok("Loi moi ket ban da duoc chap nhan.");
  }

  // 3. API: Từ chối lời mời kết bạn
  @PostMapping("/reject/{requestId}")
  public ResponseEntity<?> rejectFriendRequest(@PathVariable Integer requestId) {
    banBeService.rejectFriendRequest(requestId);
    return ResponseEntity.ok("Loi moi ket ban da bi tu choi.");
  }

  // 4. API: hủy yêu cầu kết bạn đã gửi
  @DeleteMapping("/cancel-request")
  public ResponseEntity<?> cancelFriendRequest(@RequestParam Integer senderId, @RequestParam Integer receiverId) {
    try {
      banBeService.cancelFriendRequest(senderId, receiverId);
      return ResponseEntity.ok("Yeu cau ket ban da duoc huy.");
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  // 5. API: xóa bạn bè
  @DeleteMapping("/remove-friend")
  public ResponseEntity<?> removeFriend(@RequestParam Integer userId1, @RequestParam Integer userId2) {
    try {
      banBeService.removeFriend(userId1, userId2);
      return ResponseEntity.ok("Da huy ket ban.");
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  // 6. API: Lấy danh sách bạn bè của người dùng
  @GetMapping("/list/details")
  public ResponseEntity<List<Map<String, Object>>> getFriendList(@RequestParam Integer userId) {
    List<Map<String, Object>> friends = banBeService.getFriendList(userId);
    return ResponseEntity.ok(friends);
  }

  // 7. API: danh sách lời mời kết bạn được nhận
  @GetMapping("/requests")
  public ResponseEntity<List<Map<String, Object>>> getFriendRequests(@RequestParam Integer userId) {
    List<Map<String, Object>> friendRequests = banBeService.getFriendRequests(userId);
    return ResponseEntity.ok(friendRequests);
  }

  // 8. API: Gợi ý bạn bè
  @GetMapping("/suggested-friends")
  public ResponseEntity<List<Map<String, Object>>> getSuggestedFriends(@RequestParam Integer userId) {
    // Gọi trực tiếp phương thức từ Service
    List<Map<String, Object>> suggestedFriends = banBeService.getSuggestedFriends(userId);
    return ResponseEntity.ok(suggestedFriends);

  }

  // . API: Lấy danh sách yêu cầu kết bạn đã gửi (MaTK_1 là người gửi)///Nhung
  // khong làm chức năng này
  // @GetMapping("/pending/sent")
  // public List<BanBeEntity> getPendingSent(@RequestParam int senderId) {
  // return banBeService.getPendingRequestsBySender(senderId);
  // }

  // . API: Lấy danh sách yêu cầu nhận được (MaTK_2 là người nhận)
  // @GetMapping("/pending/received")
  // public List<BanBeEntity> getPendingReceived(@RequestParam int receiverId) {
  // return banBeService.getPendingRequestsByReceiver(receiverId);
  // }

}