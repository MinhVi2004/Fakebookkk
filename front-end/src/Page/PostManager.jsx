import React, { useState, useEffect } from "react";
import axios from "axios";
import "../CSS/UserManager.css"; // Dùng lại CSS
import "../CSS/PostManager.css";
import { toast } from "react-toastify";
import Post_manager from "../Other/Post_manager";

function PostManager() {
  const [posts, setPosts] = useState([]);
  const [selectedPost, setSelectedPost] = useState(null);
  const [filterStatus, setFilterStatus] = useState("Tất cả"); // Sử dụng giá trị mặc định là "Tất cả"
  const [filteredPosts, setFilteredPosts] = useState([]); // Bài viết đã lọc

  // Tách hàm fetch ra để dùng lại
  const fetchPosts = async (status) => {
    try {
      let url = "http://localhost:8080/api/fakebook/posts"; // Mặc định lấy bài viết bình thường
      if (status === "Đã Ẩn") {
        url = "http://localhost:8080/api/fakebook/posts/hided"; // Lấy bài viết đã ẩn
      }
      
      const res = await axios.get(url);
      if (Array.isArray(res.data)) {
        setPosts(res.data.sort((a, b) => b.maBV - a.maBV)); // Sắp xếp bài viết
      }
    } catch (err) {
      console.error("Lỗi khi lấy bài viết:", err);
      toast.error("Không thể lấy dữ liệu bài viết.");
    }
  };

  // Gọi khi component mount
  useEffect(() => {
    fetchPosts(filterStatus); // Lấy bài viết theo trạng thái lọc
  }, []);

  // Lọc bài viết khi trạng thái lọc thay đổi
  useEffect(() => {
    fetchPosts(filterStatus); // Gọi lại API khi filterStatus thay đổi
  }, [filterStatus]);

  const handleFilterChange = (e) => {
    setFilterStatus(e.target.value); // Cập nhật trạng thái lọc
  };
  const handleUpdate = () => {
      fetchPosts(filterStatus); // Gọi lại fetch để reload danh sách
    };
  const handleClose = () => setSelectedPost(null);

  return (
    <div className="user-list-container">
      <h2>Danh sách bài viết</h2>

      {/* Dropdown lọc */}
      <div className="filter-dropdown">
        <label htmlFor="statusFilter">Chọn trạng thái bài viết:</label>
        <select
          id="statusFilter"
          value={filterStatus}
          onChange={handleFilterChange}
        >
          <option value="Bình Thường">Bình Thường</option>
          <option value="Đã Ẩn">Đã Ẩn</option>
        </select>
      </div>

      {/* Danh sách bài viết đã lọc */}
      <ul className="user-list">
        {posts.map((post) => (
          <li
            key={post.maBV}
            className="user-item"
            onClick={() => {
              setSelectedPost(post);
            }}
          >
            <div style={{ display: "flex", alignItems: "center" }}>
              <img
                src={
                  post?.taiKhoanBVAndBL?.profilePic
                    ? `../Resource/Avatar/${post?.taiKhoanBVAndBL.profilePic}`
                    : "../Resource/default-avatar.png"
                }
                alt="Avatar"
                style={{
                  width: "40px",
                  height: "40px",
                  borderRadius: "50%",
                  objectFit: "cover",
                  marginRight: "10px",
                }}
              />
              <div>
                <h4>{post?.taiKhoanBVAndBL?.hoTen}</h4>
                <p>{post.noiDung}</p>
              </div>
            </div>
          </li>
        ))}
      </ul>

      {/* Modal chi tiết bài viết */}
      {selectedPost && (
        <div className="modal-overlay" onClick={handleClose}>
          <div onClick={(e) => e.stopPropagation()}>
            <PostDetailModal
              post={selectedPost}
              onClose={() => setSelectedPost(null)}
              onUpdate={handleUpdate} // Gọi lại hàm fetchPosts khi cập nhật
            />
          </div>
        </div>
      )}
    </div>
  );
}

function PostDetailModal({ post, onClose, onUpdate }) {
  return (
    <div className="modal-info">
      <Post_manager key={post.maBV} post={post} onUpdate={onUpdate}/>
    </div>
  );
}

export default PostManager;
