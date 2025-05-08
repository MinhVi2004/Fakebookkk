package com.example.backend.Service;

import com.example.backend.DTO.BanBeDTO;
import com.example.backend.Entity.BanBeEntity;

import java.util.List;
import java.util.Map;

public interface BanBeService {
  void sendFriendRequest(Integer senderId, Integer receiverId);

  List<Map<String, Object>> getFriendList(Integer userId);

  void cancelFriendRequest(Integer senderId, Integer receiverId);

  void removeFriend(Integer userId1, Integer userId2);

  List<Map<String, Object>> getSuggestedFriends(Integer userId);

  List<Map<String, Object>> getFriendRequests(Integer userId);

  void acceptFriendRequest(Integer requestId);

  void rejectFriendRequest(Integer requestId);

  List<BanBeEntity> getPendingRequestsBySender(int maTK1);

  List<BanBeEntity> getPendingRequestsByReceiver(int maTK2);

}