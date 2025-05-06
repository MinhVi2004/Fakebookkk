package com.example.backend.Service;

import com.example.backend.DTO.BanBeDTO;
import com.example.backend.Entity.BanBeEntity;

import java.util.List;
import java.util.Map;

public interface BanBeService {
  void sendFriendRequest(Integer senderId, Integer receiverId);

  // void respondToFriendRequest(Integer senderId, Integer receiverId, String
  // response);

  // List<BanBeDTO> getFriendList(Integer userId);
  List<Map<String, Object>> getFriendList(Integer userId);

  void cancelFriendRequest(Integer senderId, Integer receiverId);

  void removeFriend(Integer userId1, Integer userId2);

  List<Map<String, Object>> getSuggestedFriends(Integer userId);

  List<Map<String, Object>> getFriendRequests(Integer userId);

  void acceptFriendRequest(Integer requestId);

  void rejectFriendRequest(Integer requestId);

}