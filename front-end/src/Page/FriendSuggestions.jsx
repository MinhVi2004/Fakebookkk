import React, { useEffect, useState } from "react";
import { toast } from "react-toastify";
import { useNavigation } from "../Other/navigation"; // Import useNavigation
import "./../CSS/FriendSuggestions.css" // Add styles for the component

function FriendSuggestions() {
  const [suggestedFriends, setSuggestedFriends] = useState([]);
  const { goToSignin, goToProfileById } = useNavigation();

  useEffect(() => {
    const userSignin = sessionStorage.getItem("userSignin");
    console.log("SessionStorage:", userSignin); // Kiểm tra sessionStorage

    if (!userSignin) {
      toast.error("Bạn chưa đăng nhập, vui lòng đăng nhập để tiếp tục");
      goToSignin(); // Chuyển hướng đến trang đăng nhập
      return;
    }

    const storedUserData = JSON.parse(userSignin);
    const userId = storedUserData?.maTK;

    if (!userId) {
      console.error("User ID is undefined!");
      return;
    }

    console.log("User ID:", userId); // Kiểm tra userId

    fetch(
      `http://localhost:8080/api/friends/suggested-friends?userId=${userId}`
    )
      .then((response) => {
        if (!response.ok) {
          throw new Error("Network response was not ok");
        }
        return response.json();
      })
      .then((data) => {
        console.log("Suggested friends data:", data); // Kiểm tra dữ liệu trả về
        setSuggestedFriends(data);
      })
      .catch((error) =>
        console.error("Error fetching suggested friends:", error)
      );
  }, []); // Dependency array rỗng để chỉ chạy một lần khi component mount

  const handleSendRequest = (friendId) => {
    const userSignin = sessionStorage.getItem("userSignin");
    const storedUserData = JSON.parse(userSignin);
    const userId = storedUserData?.maTK;

    fetch(
      `http://localhost:8080/api/friends/send-request?senderId=${userId}&receiverId=${friendId}`,
      {
        method: "POST",
      }
    )
      .then((response) => {
        if (response.ok) {
          toast.success("Đã gửi lời mời kết bạn!");
          setSuggestedFriends((prev) =>
            prev.filter((friend) => friend.id !== friendId)
          );
        } else {
          toast.error("Không thể gửi lời mời kết bạn.");
        }
      })
      .catch((error) => console.error("Error sending friend request:", error));
  };

  return (
    <div className="friend-suggestions-container">
      <h2>Gợi ý kết bạn</h2>
      {suggestedFriends.length === 0 ? (
        <p>Không có gợi ý kết bạn nào.</p>
      ) : (
        <ul className="friend-suggestions-list">
          {suggestedFriends.map((friend) => (
            <li key={friend.id} className="friend-suggestion-item">
              <div className="friend-info">
                <img
                  src={
                    friend.profilePic
                      ? `/Resource/Avatar/${friend.profilePic}`
                      : `/Resource/Avatar/default.png`
                  }
                  alt="Avatar"
                  className="friend-avatar"
                onClick={() => goToProfileById(friend.id)} 
                style={{ cursor: "pointer" }}
                />
                <span className="friend-name"
                onClick={() => goToProfileById(friend.id)} 
                style={{ cursor: "pointer" }}>{friend.name}</span>
              </div>
              <button
                className="add-friend-button"
                onClick={() => handleSendRequest(friend.id)}
              >
                Thêm bạn bè
              </button>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default FriendSuggestions;
