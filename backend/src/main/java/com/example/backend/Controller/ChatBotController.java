package com.example.backend.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/chatbot")
@CrossOrigin(origins = "http://localhost:3000")
public class ChatBotController {

    // ✅ Inner class Message
    public static class Message {
        private String role;
        private String content;

        public Message() {}

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }

        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }

    // ✅ Inner class ChatRequest
    public static class ChatRequest {
        private String model;
        private List<Message> messages;

        public String getModel() { return model; }
        public void setModel(String model) { this.model = model; }

        public List<Message> getMessages() { return messages; }
        public void setMessages(List<Message> messages) { this.messages = messages; }
    }

    // ✅ Inner class ChatResponse
    public static class ChatResponse {
        private String content;

        public ChatResponse(String content) {
            this.content = content;
        }

        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }

    @PostMapping
    public ResponseEntity<ChatResponse> chatWithBot(@RequestBody Map<String, String> body) {
        String userPrompt = body.get("prompt");

        // Tạo payload
        List<Message> messages = new ArrayList<>();
        messages.add(new Message("system", "Bạn là một chatbot của Fakebook. Bạn hãy trả lời ngắn gọn và thân thiện các tin nhắn của người dùng"));
        messages.add(new Message("user", userPrompt));

        ChatRequest requestPayload = new ChatRequest();
        requestPayload.setModel("gemma-3-4b-it"); // hoặc model phù hợp bạn đang dùng
        requestPayload.setMessages(messages);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ChatRequest> entity = new HttpEntity<>(requestPayload, headers);

        try {
            String lmStudioUrl = "http://localhost:1234/v1/chat/completions";

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response = restTemplate.postForEntity(lmStudioUrl, entity, Map.class);

            Map responseBody = response.getBody();
            List choices = (List) responseBody.get("choices");
            Map firstChoice = (Map) choices.get(0);
            Map message = (Map) firstChoice.get("message");

            String content = (String) message.get("content");
            return ResponseEntity.ok(new ChatResponse(content));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ChatResponse("Lỗi: Không thể xử lý yêu cầu."));
        }
    }
}
