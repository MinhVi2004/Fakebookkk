// import React, { useEffect, useState } from "react";
// import { toast } from "react-toastify";
// import { useNavigation } from "../Other/Navigation"; // Import useNavigation
// import "./../CSS/AllFriends.css"; // Add styles for the component

// function AllFriends() {
//   const [friends, setFriends] = useState([]);
//   const { goToSignin } = useNavigation();

//   useEffect(() => {
//     const userSignin = sessionStorage.getItem("userSignin");
//     console.log("SessionStorage:", userSignin); // Kiểm tra sessionStorage

//     if (!userSignin) {
//       toast.error("Bạn chưa đăng nhập, vui lòng đăng nhập để tiếp tục");
//       goToSignin(); // Chuyển hướng đến trang đăng nhập
//       return;
//     }

//     const storedUserData = JSON.parse(userSignin);
//     const userId = storedUserData?.maTK;

//     if (!userId) {
//       console.error("User ID is undefined!");
//       return;
//     }

//     console.log("User ID:", userId); // Kiểm tra userId

//     fetch(`http://localhost:8080/api/friends/list/details?userId=${userId}`)
//       .then((response) => {
//         if (!response.ok) {
//           throw new Error("Network response was not ok");
//         }
//         return response.json();
//       })
//       .then((data) => {
//         console.log("Friends data:", data); // Kiểm tra dữ liệu trả về
//         setFriends(data);
//       })
//       .catch((error) => console.error("Error fetching friends:", error));
//   }, []); // Dependency array rỗng để chỉ chạy một lần khi component mount

//   return (
//     <div className="all-friends-container">
//       <h2>Tất cả bạn bè</h2>
//       {friends.length === 0 ? (
//         <p>Bạn chưa có bạn bè nào.</p>
//       ) : (
//         <>
//           <p>{friends.length} người bạn</p>
//           <ul className="friend-list">
//             {friends.map((friend) => (
//               <li key={friend.maBB} className="friend-item">
//                 <div className="friend-info">
//                   <img
//                     src={
//                       friend.profilePic
//                         ? `/Resource/Avatar/${friend.profilePic}`
//                         : `/Resource/Avatar/default.png`
//                     }
//                     alt="Avatar"
//                     className="friend-avatar"
//                   />
//                   <span className="friend-name">{friend.hoTen}</span>{" "}
//                   {/* Hiển thị tên bạn bè */}
//                 </div>
//               </li>
//             ))}
//           </ul>
//         </>
//       )}
//     </div>
//   );
// }

// export default AllFriends;
import React, { useEffect, useState } from "react";
import { toast } from "react-toastify";
import { useNavigation } from "../Other/navigation"; // Import useNavigation
import "./../CSS/AllFriends.css"; // Add styles for the component

function AllFriends() {
  const [friends, setFriends] = useState([]);
  const [activeFriendId, setActiveFriendId] = useState(null); // ID của bạn bè đang mở menu
  const { goToSignin, goToProfileById } = useNavigation();

  useEffect(() => {
    const userSignin = sessionStorage.getItem("userSignin");
    if (!userSignin) {
      toast.error("Bạn chưa đăng nhập, vui lòng đăng nhập để tiếp tục");
      goToSignin();
      return;
    }

    const storedUserData = JSON.parse(userSignin);
    const userId = storedUserData?.maTK;

    if (!userId) {
      console.error("User ID is undefined!");
      return;
    }

    fetch(`http://localhost:8080/api/friends/list/details?userId=${userId}`)
      .then((response) => {
        if (!response.ok) {
          throw new Error("Network response was not ok");
        }
        return response.json();
      })
      .then((data) => {
        setFriends(data);
      })
      .catch((error) => console.error("Error fetching friends:", error));
  }, []);

  const handleUnfriend = (friendId) => {
    const userSignin = JSON.parse(sessionStorage.getItem("userSignin"));
    const userId = userSignin?.maTK;

    fetch(
      `http://localhost:8080/api/friends/remove-friend?userId1=${userId}&userId2=${friendId}`,
      {
        method: "DELETE",
      }
    )
      .then((response) => {
        if (!response.ok) {
          throw new Error("Failed to unfriend");
        }
        toast.success("Đã hủy kết bạn thành công!");
        setFriends(friends.filter((friend) => friend.friendId !== friendId)); // Cập nhật danh sách bạn bè
      })
      .catch((error) => {
        console.error("Error unfriending:", error);
        toast.error("Hủy kết bạn thất bại!");
      });
  };

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
                    onClick={() => goToProfileById(friend.friendId)}
                    style={{ cursor: "pointer" }}
                  />
                  <span className="friend-name"
                  onClick={() => goToProfileById(friend.friendId)} 
                    style={{ cursor: "pointer" }}>{friend.hoTen}</span>
                </div>
                <div className="friend-actions">
                  <button
                    className="action-button"
                    onClick={() =>
                      setActiveFriendId(
                        activeFriendId === friend.friendId
                          ? null
                          : friend.friendId
                      )
                    }
                  >
                    ...
                  </button>
                  {activeFriendId === friend.friendId && (
                    <div className="dropdown-menu">
                      <button
                        className="dropdown-item"
                        onClick={() => handleUnfriend(friend.friendId)}
                      >
                        Hủy kết bạn
                      </button>
                    </div>
                  )}
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
