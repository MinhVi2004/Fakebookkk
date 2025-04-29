import React, { useState, useEffect } from "react";
import "../CSS/UserManager.css"; // Dùng lại CSS, có thể tách sau
import { toast } from "react-toastify";
import confirm from "../Other/Confirm";

function PostManager() {
     const [posts, setPosts] = useState([]);
     const [selectedPost, setSelectedPost] = useState(null);

     useEffect(() => {
          fetch("http://localhost:8080/api/fakebook/admin/posts")
               .then((res) => res.json())
               .then((data) => {
                    setPosts(data); // data là mảng bài viết
               })
               .catch((err) =>
                    console.error("Lỗi khi lấy danh sách bài viết:", err)
               );
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
                              onClick={() => setSelectedPost(post)}
                         >
                              <img
                                   src={`../Resource/Post/${post.hinhAnh}`}
                                   alt="Thumbnail"
                              />
                              <div>
                                   <h4>{post.tieuDe}</h4>
                                   <p>Người đăng: {post.nguoiDang?.hoTen}</p>
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
                    />
               )}
          </div>
     );
}

function PostDetailModal({ post, onClose, setPosts, setSelectedPost }) {
     const handleToggleStatus = async () => {
          const isHidden = post.trangThai === "Đã Ẩn";

          const confirmed = await confirm({
               title: "Xác nhận",
               text: isHidden
                    ? "Bạn có chắc muốn hiển thị lại bài viết này?"
                    : "Bạn có chắc muốn ẩn bài viết này?",
          });

          if (!confirmed) return;

          const url = isHidden
               ? `http://localhost:8080/api/fakebook/admin/posts/show/${post.maBV}`
               : `http://localhost:8080/api/fakebook/admin/posts/hide/${post.maBV}`;

          fetch(url, { method: "PUT" })
               .then((res) => {
                    if (!res.ok)
                         throw new Error("Lỗi khi cập nhật trạng thái bài viết");
                    return res.json();
               })
               .then((data) => {
                    toast.success(
                         `Đã ${
                              isHidden ? "hiển thị" : "ẩn"
                         } bài viết thành công!`
                    );
                    setPosts((prev) =>
                         prev.map((p) =>
                              p.maBV === post.maBV ? data : p
                         )
                    );
                    setSelectedPost(data);
               })
               .catch((err) => {
                    console.error("Lỗi cập nhật trạng thái bài viết:", err);
                    toast.error("Đã có lỗi xảy ra.");
               });
     };

     return (
          <div className="modal-info">
          <p><strong>Nội dung:</strong> {post.noiDung}</p>
          <p><strong>Người đăng:</strong> {post.nguoiDang?.hoTen}</p>
          <p><strong>Thời gian:</strong> {post.thoiGian}</p>
          <p><strong>Đính kèm:</strong></p>
          <div className="attachments">
               {post.dinhKems?.map((dk) => (
                    <div key={dk.maBV_DK} className="attachment-item">
                         {dk.loaiDK === "image" ? (
                              <img
                                   src={`../Resource/Attachment/${dk.linkDK}`}
                                   alt="Đính kèm"
                                   style={{ maxWidth: "100px", marginRight: "10px" }}
                              />
                         ) : (
                              <a
                                   href={`../Resource/Attachment/${dk.linkDK}`}
                                   target="_blank"
                                   rel="noopener noreferrer"
                              >
                                   {dk.linkDK}
                              </a>
                         )}
                    </div>
               ))}
          </div>
     </div>
     );
}

export default PostManager;
