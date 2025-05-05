package com.example.backend.Service;

import com.example.backend.DTO.BanBeDTO;
import com.example.backend.Entity.BanBeEntity;

import java.util.List;

public interface BanBeService {
  void sendFriendRequest(Integer senderId, Integer receiverId);

  void respondToFriendRequest(Integer senderId, Integer receiverId, String response);

  List<BanBeDTO> getFriendList(Integer userId);

  void cancelFriendRequest(Integer senderId, Integer receiverId);

  void removeFriend(Integer userId1, Integer userId2);
}