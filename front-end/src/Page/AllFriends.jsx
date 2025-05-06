import React, { useEffect, useState } from "react";
import { toast } from "react-toastify";
import { useNavigation } from "../Other/Navigation"; // Import useNavigation
import "./../CSS/AllFriends.css"; // Add styles for the component

function AllFriends() {
  const [friends, setFriends] = useState([]);
  const { goToSignin } = useNavigation();

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

    fetch(`http://localhost:8080/api/friends/list/details?userId=${userId}`)
      .then((response) => {
        if (!response.ok) {
          throw new Error("Network response was not ok");
        }
        return response.json();
      })
      .then((data) => {
        console.log("Friends data:", data); // Kiểm tra dữ liệu trả về
        setFriends(data);
      })
      .catch((error) => console.error("Error fetching friends:", error));
  }, []); // Dependency array rỗng để chỉ chạy một lần khi component mount

  return (
    <div className="all-friends-container">
      <h2>Tất cả bạn bè</h2>
      {friends.length === 0 ? (
        <p>Bạn chưa có bạn bè nào.</p>
      ) : (
        <>
          <p>{friends.length} người bạn</p>
          <ul className="friend-list">
            {friends.map((friend) => (
              <li key={friend.maBB} className="friend-item">
                <div className="friend-info">
                  <img
                    src={
                      friend.profilePic
                        ? `/Resource/Avatar/${friend.profilePic}`
                        : `/Resource/Avatar/default.png`
                    }
                    alt="Avatar"
                    className="friend-avatar"
                  />
                  <span className="friend-name">{friend.hoTen}</span>{" "}
                  {/* Hiển thị tên bạn bè */}
                </div>
              </li>
            ))}
          </ul>
        </>
      )}
    </div>
  );
}

export default AllFriends;
