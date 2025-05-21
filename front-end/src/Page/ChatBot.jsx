import React, { useState, useRef } from 'react';
import axios from 'axios';
import '../CSS/ChatBot.css';

const ChatBot = () => {
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState("");
  const [waitingForBot, setWaitingForBot] = useState(false);
  const timeoutRef = useRef(null);

  const handleSend = async () => {
    if (!input.trim() || waitingForBot) return;

    const userMsg = {
      sender: "User",
      content: input,
      type: "USER",
    };

    setMessages((prev) => [...prev, userMsg]);
    setInput("");
    setWaitingForBot(true);

    // 60s timeout nếu không có phản hồi
    timeoutRef.current = setTimeout(() => {
      setMessages((prev) => [
        ...prev,
        { sender: "BOT", content: "Không thể xử lý nội dung", type: "BOT" }
      ]);
      setWaitingForBot(false);
    }, 60000);

    try {
      const response = await axios.post("http://localhost:8080/api/chatbot", {
        prompt: input
      });

      clearTimeout(timeoutRef.current);

      setMessages((prev) => [
        ...prev,
        { sender: "BOT", content: response.data.content, type: "BOT" }
      ]);
    } catch (error) {
      clearTimeout(timeoutRef.current);
      console.error("Lỗi gửi yêu cầu:", error);
      setMessages((prev) => [
        ...prev,
        { sender: "BOT", content: "Lỗi trong khi xử lý yêu cầu.", type: "BOT" }
      ]);
    } finally {
      setWaitingForBot(false);
    }
  };

  return (
    <div className="chatbot-container">
      <div className="chatbot-header">🤖 AI ChatBot</div>
      <div className="chatbot-messages">
        {messages.map((msg, idx) => (
          <div key={idx} className={`chatbot-message ${msg.type === 'USER' ? 'user' : 'bot'}`}>
            <strong>{msg.sender}:</strong> {msg.content}
          </div>
        ))}
      </div>
      <div className="chatbot-input-area">
        <input
          value={input}
          onChange={(e) => setInput(e.target.value)}
          onKeyDown={(e) => e.key === "Enter" && handleSend()}
          placeholder="Nhập câu hỏi..."
          className="chatbot-input"
          disabled={waitingForBot}
        />
        <button
          className="chatbot-send-btn"
          onClick={handleSend}
          disabled={waitingForBot}
        >
          {waitingForBot ? "Đang xử lý..." : "Gửi"}
        </button>
      </div>
    </div>
  );
};

export default ChatBot;
