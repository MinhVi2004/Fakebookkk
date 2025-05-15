import React, { useState, useEffect } from "react";
import { toast } from "react-toastify";
import EditProfileForm from "./EditProfileForm"; // Import form chỉnh sửa
import "../CSS/Profile.css";
import { useNavigation } from "../Other/navigation"; // Import useNavigation từ file navigation.js
import Post from "../Other/Post";
import PostForm from "../Other/PostForm";
import axios from "axios";
import { useLocation } from "react-router-dom";

export default function Profile() {
  const { goToSignin } = useNavigation();
  useEffect(() => {
    const userData = sessionStorage.getItem("userSignin");
    if (!userData) {
            console.warn("Chưa có userSignin trong sessionStorage.");
          toast.error("Vui lòng đăng nhập để tiếp tục.");
          goToSignin();
    }
  }, []);
  const storedUserData = JSON.parse(sessionStorage.getItem("userSignin"));
  const [postsUser, setPostsUser] = useState([]);
  const [maTK, setMaTK] = useState([]);
  const [isMyProfile, setIsMyProfile] = useState(false); // To check if the profile is the user's own
  const [isFriend, setIsFriend] = useState(false); // To track friendship status
  const [friendRequestSent, setFriendRequestSent] = useState(false); // To track if request was sent
  const [isFriendRequestPending, setIsFriendRequestPending] = useState(false); // To track if there’s a pending friend request
  const [requestId, setRequestId] = useState(null); // dùng để chấp nhận kết bạn
  const [friendCount, setFriendCount] = useState(0);
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const userIdFromUrl = queryParams.get("id");

  const [userName, setUserName] = useState("");
  const [profilePic, setProfilePic] = useState("");
  const [coverPic, setCoverPic] = useState("");
  const [isEditFormOpen, setIsEditFormOpen] = useState(false);
  const [hoveringCancelRequest, setHoveringCancelRequest] = useState(false); // Trạng thái khi rê chuột vào nút

  const showEditForm = () => setIsEditFormOpen(true);
  const hideEditForm = () => setIsEditFormOpen(false);

  const updateProfile = (updatedData) => {
    if (updatedData.hoTen) setUserName(updatedData.hoTen);
    if (updatedData.profilePic)
      setProfilePic(`/Resource/Avatar/${updatedData.profilePic}`);
    if (updatedData.coverPic)
      setCoverPic(`/Resource/Background/${updatedData.coverPic}`);

    const updatedUserData = {
      ...storedUserData,
      hoTen: updatedData.hoTen,
      profilePic: updatedData.profilePic,
      coverPic: updatedData.coverPic,
    };
    sessionStorage.setItem("userSignin", JSON.stringify(updatedUserData));

    hideEditForm();
  };

  const fetchUserData = async (id) => {
    if (!id) {
      const userSignin = sessionStorage.getItem("userSignin");
      if (userSignin) {
        try {
          const user = JSON.parse(userSignin);
          id = user.maTK;
        } catch (error) {
          console.error("Cannot parse userSignin from sessionStorage:", error);
          return;
        }
      }
    }

    if (id) {
      try {
        const res = await axios.get(
          `http://localhost:8080/api/fakebook/user/${id}`
        );
        const userData = res.data.data;

        if (userData.maTK === storedUserData.maTK) {
          setIsMyProfile(true);
        } else {
          setIsMyProfile(false);
        }

        setUserName(userData.hoTen);
        setProfilePic(
          userData.profilePic
            ? `/Resource/Avatar/${userData.profilePic}`
            : "/Resource/Avatar/default.png"
        );
        setCoverPic(
          userData.coverPic
            ? `/Resource/Background/${userData.coverPic}`
            : "/Resource/Avatar/default.png"
        );
        setMaTK(id);

        fetchPosts(id);
        checkFriendshipStatus(id);
      } catch (err) {
        console.error("Error fetching user data:", err);
        toast.error("Không thể lấy thông tin người dùng.");
      }
    }
  };
const handleDeletePost = () => {
    fetchPosts(maTK);
  };
  const fetchPosts = async (userId) => {
    try {
      const res = await axios.get(
        `http://localhost:8080/api/fakebook/posts/profile/${userId}`
      );
      setPostsUser(res.data.sort((a, b) => b.maBV - a.maBV)); // Sort posts by ID
    } catch (err) {
      console.error("Error fetching posts:", err);
    }
  };

  const checkFriendshipStatus = async (profileId) => {
    try {
      const currentUserId = storedUserData.maTK;

      // 1. Danh sách bạn bè
      const resFriends = await axios.get(
        `http://localhost:8080/api/friends/list/details?userId=${currentUserId}`
      );
      const friendsList = resFriends.data;
      setFriendCount(Array.isArray(friendsList) ? friendsList.length : 0);

      // Kiểm tra nếu người đăng nhập đã là bạn của profile
      const isAlreadyFriend = friendsList.some(
        (friend) => friend.friendId === parseInt(profileId)
      );
      setIsFriend(isAlreadyFriend);

      // 2. Đã gửi lời mời (người dùng hiện tại là sender)
      const resSent = await axios.get(
        "http://localhost:8080/api/friends/pending/sent",
        {
          params: { senderId: currentUserId },
        }
      );
      const sentList = resSent.data;
      const sent = sentList.find((item) => item.maTK2 === parseInt(profileId));
      setFriendRequestSent(!!sent);

      // 3. Đang chờ duyệt (người dùng hiện tại là receiver)
      const resReceived = await axios.get(
        "http://localhost:8080/api/friends/pending/received",
        {
          params: { receiverId: currentUserId },
        }
      );
      const receivedList = resReceived.data;
      const received = receivedList.find(
        (item) => item.maTK1 === parseInt(profileId)
      );
      setIsFriendRequestPending(!!received);

      // Gán requestId nếu có để dùng khi accept
      if (received) setRequestId(received.maBB); // bạn cần thêm useState cho requestId
    } catch (err) {
      console.error("Error checking friendship status:", err);
    }
  };

  useEffect(() => {
    fetchUserData(userIdFromUrl);
  }, [userIdFromUrl]);

  const handleSendRequest = async () => {
    try {
      await axios.post("http://localhost:8080/api/friends/send-request", null, {
        params: {
          senderId: storedUserData.maTK,
          receiverId: maTK,
        },
      });
      setFriendRequestSent(true);
      toast.success("Yêu cầu kết bạn đã được gửi.");
    } catch (err) {
      toast.error("Lỗi khi gửi yêu cầu kết bạn.");
    }
  };

  const handleAcceptRequest = async () => {
    try {
      if (!requestId) return toast.error("Không tìm thấy yêu cầu kết bạn.");

      await axios.post(`http://localhost:8080/api/friends/accept/${requestId}`);
      setIsFriend(true);
      setIsFriendRequestPending(false);
      toast.success("Lời mời kết bạn đã được chấp nhận.");
    } catch (err) {
      toast.error("Lỗi khi chấp nhận yêu cầu.");
    }
  };

  const handleRemoveFriend = async () => {
    try {
      await axios.delete("http://localhost:8080/api/friends/remove-friend", {
        params: {
          userId1: storedUserData.maTK,
          userId2: maTK,
        },
      });
      setIsFriend(false);
      toast.success("Đã hủy kết bạn.");
    } catch (err) {
      toast.error("Lỗi khi hủy kết bạn.");
    }
  };

  const handleCancelRequest = async () => {
    try {
      await axios.delete("http://localhost:8080/api/friends/cancel-request", {
        params: {
          senderId: storedUserData.maTK,
          receiverId: maTK,
        },
      });
      setFriendRequestSent(false);
      toast.success("Đã hủy lời mời kết bạn.");
    } catch (err) {
      toast.error("Lỗi khi hủy lời mời kết bạn.");
    }
  };

  return (
    <div className="profile-container">
      <div className="max-w-6xl mx-auto">
        <div className="cover-photo">
          {coverPic ? (
            <img src={coverPic} alt="Ảnh bìa" />
          ) : (
            <div className="default-cover">Chưa có ảnh bìa</div>
          )}
        </div>
        <div className="profile-info">
          <div className="profile-avatar">
            {profilePic ? (
              <img src={profilePic} alt="Ảnh đại diện" />
            ) : (
              <div className="default-avatar">Không có ảnh</div>
            )}
          </div>
          <div className="profile-name">
            <h1>{userName}</h1>
            <p>{friendCount} bạn bè</p>
          </div>
          <div className="profile-buttons">
            {isMyProfile && (
              <button onClick={showEditForm}>✏️ Chỉnh sửa trang cá nhân</button>
            )}

            {!isMyProfile && (
              <div className="profile-buttons">
                {isFriend ? (
                  <button onClick={handleRemoveFriend}>Hủy kết bạn</button>
                ) : isFriendRequestPending ? (
                  <button onClick={handleAcceptRequest}>
                    Chấp nhận lời mời kết bạn
                  </button>
                ) : friendRequestSent ? (
                  <button
                    onMouseEnter={() => setHoveringCancelRequest(true)} // Khi rê chuột vào
                    onMouseLeave={() => setHoveringCancelRequest(false)} // Khi rời chuột
                    onClick={handleCancelRequest} // Hủy lời mời kết bạn khi bấm
                  >
                    {hoveringCancelRequest
                      ? "Hủy lời mời kết bạn"
                      : "Đã gửi lời mời kết bạn"}
                  </button>
                ) : (
                  <button onClick={handleSendRequest}>Kết bạn</button>
                )}
              </div>
            )}
          </div>
          {isEditFormOpen && (
            <EditProfileForm
              userName={userName}
              storedUserData={storedUserData}
              onSave={updateProfile}
              onCancel={hideEditForm}
            />
          )}
        </div>

        <div className="post-container">
          {maTK && <PostForm maTK={maTK} onPostSubmit={fetchPosts} />}
          <div className="posts-list">
            {postsUser.length > 0 ? (
              postsUser.map((post) => <Post key={post.maBV} post={post} 
                onDelete={handleDeletePost} />)
            ) : (
              <p>Không có bài viết nào.</p>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
