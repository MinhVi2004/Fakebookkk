  import React, { useEffect, useState } from "react";
  import SockJS from "sockjs-client";
  import { Client } from "@stomp/stompjs";
  import "../CSS/ChatWindow.css";
  import axios from "axios";

  let stompClient = null;

  const ChatWindow = ({ friend, onClose, stompClient  }) => {
    const [messages, setMessages] = useState([]);
    const [input, setInput] = useState("");
    const userSignin = JSON.parse(sessionStorage.getItem("userSignin"));

    useEffect(() => {

        const fetchHistory = async () => {
        try {
              const res = await axios.get("http://localhost:8080/api/messages/history", {
              params: {
              user1: userSignin.maTK,
              user2: friend.friendId,
              },
              });
              setMessages(res.data); // Gán tin nhắn cũ
        } catch (err) {
              console.error("Failed to fetch message history:", err);
        }
        };

      fetchHistory();

      const socket = new SockJS("http://localhost:8080/ws");

      stompClient = new Client({
        webSocketFactory: () => socket,
        onConnect: () => {
          console.log("Connected");

          stompClient.subscribe("/topic/messages", (message) => {
            const msg = JSON.parse(message.body);
            if (
              (msg.nguoiGuiId === friend.friendId && msg.nguoiNhanId === userSignin.maTK) ||
              (msg.nguoiGuiId === userSignin.maTK && msg.nguoiNhanId === friend.friendId)
            ) {
              setMessages((prev) => [...prev, msg]);
            }
          });
        },
        onStompError: (frame) => {
          console.error("Broker error: ", frame.headers["message"]);
        },
      });

      stompClient.activate();

      return () => {
        if (stompClient) {
          stompClient.deactivate();
        }
      };
    }, [friend]);

    const sendMessage = () => {
      if (input.trim() === "") return;

      const payload = {
        nguoiGuiId: userSignin.maTK,
        nguoiNhanId: friend.friendId,
        noiDung: input,
      };

      stompClient.publish({
        destination: "/app/chat.send",
        body: JSON.stringify(payload),
      });

  //     setMessages((prev) => [...prev, payload]); // cập nhật luôn UI người gửi
      setInput("");
    };

    return (
      <div className="chat-window">
        <div className="chat-header">
          <img
            src={`../Resource/Avatar/${friend.profilePic}`}
            alt="avatar"
            className="avatar"
          />
          <h4>{friend.hoTen}</h4>
          <button onClick={onClose}>X</button>
        </div>
        <div className="chat-messages">
          {messages.map((msg, index) => (
            <div
              key={index}
              className={`chat-message ${
                msg.nguoiGuiId === friend.friendId ? "received" : "sent"
              }`}
            >
              <div className="message-content">{msg.noiDung}</div>
            </div>
          ))}
        </div>
        <div className="chat-send">
          <input
            value={input}
            onChange={(e) => setInput(e.target.value)}
            placeholder="Nhập tin nhắn..."
            onKeyDown={(e) => {
              if (e.key === "Enter") {
                sendMessage();
              }
            }}
          />
          <button onClick={sendMessage}>Gửi</button>
        </div>
      </div>
    );
  };

  export default ChatWindow;