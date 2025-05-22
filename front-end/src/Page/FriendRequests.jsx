import React, { useEffect, useState } from "react";
import { toast } from "react-toastify";
import "./../CSS/FriendRequests.css"; 
import { useNavigation } from "../Other/navigation"; 
import { useRequestContext } from "./RequestContext"; 

function FriendRequests({ onRefreshFriends }) {
  const { updateRequestCount } = useRequestContext();
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(false); 
  const { goToProfileById } = useNavigation(); 

  const formatDateTime = (isoString) => {
      const date = new Date(isoString);
      return date.toLocaleString("vi-VN", {
        hour: '2-digit',
        minute: '2-digit',
        day: '2-digit',
        month: '2-digit',
        year: 'numeric'
      });
  };

  const fetchFriends = () => {
    const userId = JSON.parse(sessionStorage.getItem("userSignin"))?.maTK;
    if (!userId) {
      setError(true);
      setLoading(false);
      return;
    }

    fetch(`http://localhost:8080/api/friends/list/details?userId=${userId}`)
      .then((response) => response.json())
      .then((data) => console.log("Updated friends list:", data)) 
      .catch((error) => console.error("Error fetching friends:", error));
  };

  useEffect(() => {
    const userId = JSON.parse(sessionStorage.getItem("userSignin"))?.maTK;
    if (!userId) {
      console.error("User ID is missing");
      setError(true);
      setLoading(false);
      return;
    }

    fetch(`http://localhost:8080/api/friends/requests?userId=${userId}`)
      .then((response) => response.json())
      .then((data) => {
        setRequests(data);
        setLoading(false);
        updateRequestCount(data.length); 
      })
      .catch((error) => {
        console.error("Error fetching friend requests:", error);
        toast.error("Không thể tải danh sách lời mời kết bạn.");
        setError(true);
        setLoading(false);
      });
  }, [updateRequestCount]);

  const handleAccept = (requestId) => {
    fetch(`http://localhost:8080/api/friends/accept/${requestId}`, { method: "POST" })
      .then((response) => {
        if (response.ok) {
          toast.success("Đã chấp nhận lời mời kết bạn!");
          setRequests((prev) => prev.filter((req) => req.maBB !== requestId));
          updateRequestCount(requests.length - 1);
          fetchFriends();
          if (onRefreshFriends) {
            onRefreshFriends();
          }
        } else {
          toast.error("Không thể chấp nhận lời mời kết bạn.");
        }
      })
      .catch((error) => console.error("Error accepting friend request:", error));
  };

  const handleReject = (requestId) => {
    fetch(`http://localhost:8080/api/friends/reject/${requestId}`, { method: "POST" })
      .then((response) => {
        if (response.ok) {
          toast.success("Đã từ chối lời mời kết bạn!");
          setRequests((prev) => prev.filter((req) => req.maBB !== requestId));
          updateRequestCount(requests.length - 1);
        } else {
          toast.error("Không thể từ chối lời mời kết bạn.");
        }
      })
      .catch((error) => console.error("Error rejecting friend request:", error));
  };

  if (loading) {
    return <p>Đang tải...</p>;
  }   

  if (error) {
    return <p>Đã xảy ra lỗi khi tải lời mời kết bạn.</p>;
  }

  return (
    <div className="friend-requests-container">
      <h2>Lời mời kết bạn</h2>
      {requests.length === 0 ? (
        <p>Không có lời mời kết bạn nào.</p>
      ) : (
        <ul className="friend-requests-list">
          {requests.map((request) => (
            <li key={request.maTK1} className="friend-request-item">
              <img
                src={`/Resource/Avatar/${request.profilePic}`}
                alt={request.hoTen}
                className="friend-request-avatar"
                onClick={() => goToProfileById(request.maTK1)}
                style={{ cursor: "pointer" }}
              />
              <div className="friend-request-info">
                <span
                  className="friend-request-name"
                  onClick={() => goToProfileById(request.maTK1)}
                  style={{ cursor: "pointer" }}
                >
                  {request.hoTen}
                </span>
                <br />
                <span className="friend-request-time">{formatDateTime(request.ngayTao)}</span>
              </div>
              <div className="friend-request-actions">
                <button
                  className="accept-button"
                  onClick={() => handleAccept(request.maBB)}
                >
                  Xác nhận
                </button>
                <button
                  className="reject-button"
                  onClick={() => handleReject(request.maBB)}
                >
                  Xóa
                </button>
              </div>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

export default FriendRequests;
