import React, { useState, useEffect } from "react";
import "../CSS/UserManager.css"; // Dùng lại CSS, có thể tách sau
import { toast } from "react-toastify";
import confirm from "../Other/Confirm";

function PostManager() {
     const [posts, setPosts] = useState([]);
     const [selectedPost, setSelectedPost] = useState(null);
     const [userInfo, setUserInfo] = useState(null); // Thêm state để lưu thông tin người dùng

     useEffect(() => {
      fetch("http://localhost:8080/api/fakebook/admin/posts")
        .then((res) => res.json())
        .then(async (data) => {
          const postsWithUserInfo = await Promise.all(
            data.map(async (post) => {
              try {
                const response = await fetch(`http://localhost:8080/api/fakebook/admin/users/${post.maTK}`);
                const userData = await response.json();
                return { ...post, nguoiDang: userData }; // Gán user vào post
              } catch (error) {
                console.error(`Lỗi lấy user cho post ${post.maBV}:`, error);
                return post; // Nếu lỗi thì giữ nguyên post
              }
            })
          );
          setPosts(postsWithUserInfo); // Cập nhật luôn posts có user
        })
        .catch((err) => console.error("Lỗi khi lấy danh sách bài viết:", err));
    }, []);
    

     const handleClose = () => setSelectedPost(null);

     return (
          <div className="user-list-container">
               <h2>Danh sách bài viết</h2>
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
                             src={post.nguoiDang?.profilePic ? `../Resource/Avatar/${post.nguoiDang.profilePic}` : "../Resource/default-avatar.png"}
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
                             <h4>{post.nguoiDang?.hoTen}</h4> {/* hoặc tiêu đề bài viết nếu có */}
                             <p> {post.noiDung}</p>
                           </div>
                         </div>
                       </li>
                       
                    ))}
               </ul>

               {selectedPost && (
                    <PostDetailModal
                         post={selectedPost}
                         onClose={handleClose}
                         setPosts={setPosts}
                         setSelectedPost={setSelectedPost}
                         userInfo={userInfo} // Chuyển thông tin người dùng cho modal
                    />
               )}
          </div>
     );
}

function PostDetailModal({ post, onClose, setPosts, setSelectedPost, userInfo }) {
     const handleToggleStatus = async () => {
          const isHidden = post.trangThai === "Đã Ẩn";

          const confirmed = await confirm({
               title: "Xác nhận",
               text: isHidden
                    ? "Bạn có chắc muốn hiển thị lại bài viết này?"
                    : "Bạn có chắc muốn ẩn bài viết này?",
          });

          if (!confirmed) return;

          const newTrangThai = isHidden ? "Mới" : "Đã Ẩn";
          const url = `http://localhost:8080/api/fakebook/admin/posts/${
               post.maBV
          }/status?trangThai=${encodeURIComponent(newTrangThai)}`;

          fetch(url, { method: "PUT" })
               .then((res) => {
                    if (!res.ok)
                         throw new Error(
                              "Lỗi khi cập nhật trạng thái bài viết"
                         );
                    return res.json(); // đảm bảo backend trả BaiVietDTO mới
               })
               .then((updatedPost) => {
                    toast.success(
                         `Đã ${
                              isHidden ? "hiển thị" : "ẩn"
                         } bài viết thành công!`
                    );
                    setPosts((prev) =>
                         prev.map((p) =>
                              p.maBV === post.maBV ? updatedPost : p
                         )
                    );
                    setSelectedPost(updatedPost);
               })
               .catch((err) => {
                    console.error("Lỗi cập nhật trạng thái bài viết:", err);
                    toast.error("Đã có lỗi xảy ra.");
               });
     };

     return (
          <div className="modal-info">
               <p>
                    <strong>Nội dung:</strong> {post.noiDung}
               </p>
               <p>
                    <strong>Người đăng:</strong> {post.nguoiDang.hoTen}
               </p>
               <p>
                    <strong>Thời gian:</strong> {post.thoiGian}
               </p>
               <p>
                    <strong>Đính kèm:</strong>
               </p>
               <div className="attachments">
                    {post.dinhKems?.map((dk) => (
                         <div key={dk.maBV_DK} className="attachment-item">
                              {dk.loaiDK === "image" ? (
                                   <img
                                        src={`../Resource/Attachment/${dk.linkDK}`}
                                        alt="Đính kèm"
                                        style={{
                                             maxWidth: "100px",
                                             marginRight: "10px",
                                        }}
                                   />
                              ) : (
                                   <video
                                        href={`../Resource/Attachment/${dk.linkDK}`}
                                        target="_blank"
                                        rel="noopener noreferrer"
                                   >
                                        {dk.linkDK}
                                   </video>
                              )}
                         </div>
                    ))}
               </div>
          </div>
     );
}

export default PostManager;
