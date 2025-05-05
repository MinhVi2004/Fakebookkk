package com.example.backend.Controller;

import com.example.backend.DTO.BanBeDTO;
import com.example.backend.Entity.BanBeEntity;
import com.example.backend.Service.BanBeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
@AllArgsConstructor
public class BanBeController {

  private final BanBeService banBeService;

  @PostMapping("/send-request")
  public ResponseEntity<?> sendFriendRequest(@RequestParam Integer senderId, @RequestParam Integer receiverId) {
    try {
      banBeService.sendFriendRequest(senderId, receiverId);
      return ResponseEntity.ok("Yêu cầu kết bạn đã được gửi.");
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @DeleteMapping("/cancel-request")
  public ResponseEntity<?> cancelFriendRequest(@RequestParam Integer senderId, @RequestParam Integer receiverId) {
    try {
      banBeService.cancelFriendRequest(senderId, receiverId);
      return ResponseEntity.ok("Yêu cầu kết bạn đã được hủy.");
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @DeleteMapping("/remove-friend")
  public ResponseEntity<?> removeFriend(@RequestParam Integer userId1, @RequestParam Integer userId2) {
    try {
      banBeService.removeFriend(userId1, userId2);
      return ResponseEntity.ok("Đã hủy kết bạn.");
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("/list")
  public ResponseEntity<List<BanBeDTO>> getFriendList(@RequestParam Integer userId) {
    return ResponseEntity.ok(banBeService.getFriendList(userId));
  }

  @PutMapping("/respond-request")
  public ResponseEntity<?> respondToFriendRequest(
      @RequestParam Integer senderId,
      @RequestParam Integer receiverId,
      @RequestParam String response) {
    try {
      banBeService.respondToFriendRequest(senderId, receiverId, response);
      return ResponseEntity.ok("Phản hồi yêu cầu kết bạn thành công.");
    } catch (RuntimeException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}