import React, { useEffect, useState } from "react";
import "../CSS/ChatSideBar.css"; // tạo file CSS nếu cần
import axios from "axios";

const ChatSidebar = ({ onSelectFriend, unreadCounts,visible, onToggle, refreshTrigger  }) => {
      const [friends, setFriends] = useState([]);
      const userSignin = JSON.parse(sessionStorage.getItem("userSignin"));
      const userId = userSignin.maTK;
      useEffect(() => {
        const fetchFriends = async () => {
          try {
            const response = await axios.get(
              `http://localhost:8080/api/friends/list/details?userId=${userId}`
            );
            setFriends(response.data);
          } catch (error) {
            console.error("Error fetching friends:", error);
          }
        };

        if (userId) {
          fetchFriends();
        }
      }, [userId, refreshTrigger]);

  return (
    <div className="chat-sidebar">
  <div className="chat-sidebar-header">
    <span className="chat-title">Bạn Bè</span>
    <button className="toggle-button" onClick={onToggle}>
      {visible ? '▼' : '▲'}
    </button>
  </div>

  <ul className={`chat-sidebar-content ${visible ? 'visible' : 'hidden'}`}>
    {friends.map((friend) => (
      <li className="friend-item" key={friend.friendId} onClick={() => onSelectFriend(friend)}>
        <img src={`../Resource/Avatar/${friend.profilePic}`} alt="avatar" className="profile-pic"/>
        <span className="profile-name">{friend.hoTen}</span>
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