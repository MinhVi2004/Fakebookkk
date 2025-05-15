import React, { useEffect, useState } from "react";
import "../CSS/ChatSideBar.css"; // tạo file CSS nếu cần
import axios from "axios";

const ChatSidebar = ({ onSelectFriend, unreadCounts }) => {
      const [friends, setFriends] = useState([]);
      const userSignin = JSON.parse(sessionStorage.getItem("userSignin"));
      const userId = userSignin.maTK;
      useEffect(() => {
      const fetchFriends = async () => {
            try {
            const response = await axios.get(`http://localhost:8080/api/friends/list/details?userId=${userId}`);
            console.log("Friends response:", response.data);
            setFriends(response.data);
            } catch (error) {
            console.error("Error fetching friends:", error);
            }
      };

      if (userId) {
            fetchFriends();
      }
      }, [userId]);

  return (
    <div className="chat-sidebar">
      <h3>Tin Nhắn</h3>
      <ul>
        {friends.map((friend) => (
          <li key={friend.friendId} onClick={() => onSelectFriend(friend)}>
            <img src={`../Resource/Avatar/${friend.profilePic}`} alt="" className="profile-pic"/>
            <span  className="profile-name">{friend.hoTen}</span>
            {unreadCounts[friend.friendId] > 0 && (
                  <span className="badge">{unreadCounts[friend.friendId]}</span>
            )}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default ChatSidebar;