package com.example.backend.Controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.backend.DTO.ChatMessage;
import com.example.backend.Entity.TinNhanEntity;
import com.example.backend.Service.TinNhanService;

@Controller
@CrossOrigin(origins = "http://localhost:3000") // Thêm dòng này react được phép truy cập
public class ChatWebSocketController {

    @Autowired
    private TinNhanService tinNhanService;

      @MessageMapping("/chat.send")           // tương ứng /app/chat.send
      @SendTo("/topic/messages")              // broadcast đến tất cả (có thể lọc theo người nhận)
      public ChatMessage sendMessage(ChatMessage msg) {
            // 1) lưu vào DB
            TinNhanEntity saved = tinNhanService.guiTinNhan(
                  msg.getNguoiGuiId(),
                  msg.getNguoiNhanId(),
                  msg.getNoiDung()
            );
            // 2) trả về client (kèm ID và timestamp)
            ChatMessage out = new ChatMessage();
            out.setMaTinNhan(saved.getMaTinNhan());
            out.setNguoiGuiId(saved.getNguoiGui().getMaTK());
            out.setNguoiNhanId(saved.getNguoiNhan().getMaTK()); 
            out.setNoiDung(saved.getNoiDung());
            out.setTimestamp(saved.getThoiGianGui().toString());
            return out;
      }
      @GetMapping("/api/messages/history")
      @ResponseBody
      public ResponseEntity<List<ChatMessage>> getChatHistory(@RequestParam Integer user1, @RequestParam Integer user2) {
            List<TinNhanEntity> messages = tinNhanService.layTinNhanGiuaHaiNguoi(user1, user2);

            // convert sang DTO (ChatMessage)
            List<ChatMessage> result = messages.stream().map(msg -> {
                  ChatMessage dto = new ChatMessage();
                  dto.setMaTinNhan(msg.getMaTinNhan());
                  dto.setNguoiGuiId(msg.getNguoiGui().getMaTK());
                  dto.setNguoiNhanId(msg.getNguoiNhan().getMaTK());
                  dto.setNoiDung(msg.getNoiDung());
                  dto.setTimestamp(msg.getThoiGianGui().toString());
                  return dto;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(result);
      }
}
