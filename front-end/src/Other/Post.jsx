import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import axios from "axios";
import CommentSection from "./CommentSection";
import PostForm from "./PostForm";
import "../CSS/Post.css";
import { toast } from "react-toastify";
import confirm from "../Other/Confirm";
const Post = ({ post, onDelete }) => {
      const [likes, setLikes] = useState(post.luotThichList?.length || 0);
      const [hasLiked, setHasLiked] = useState(() => {
        const user = JSON.parse(sessionStorage.getItem("userSignin"));
        return post.luotThichList?.some((like) => like.maTK === user?.maTK);
      });
      const [comments, setComments] = useState(post.binhLuanList || []);
      const [showComments, setShowComments] = useState(false);
      const [showFullCaption, setShowFullCaption] = useState(false);
      const [showMenu, setShowMenu] = useState(false);
      const [showModal, setShowModal] = useState(false);
    
      const maxPreview = 4;
      const maxCaptionLength = 200;
      const caption = post.noiDung || "";
      const captionLength = caption.length;
    
      // Phân tách ảnh/video từ baiVietDinhKemResponseList
      const images = post.baiVietDinhKemResponseList
        ?.filter((dk) => dk.loaiDK === "image")
        ?.map((dk) => dk.fileData && dk.fileData.startsWith("data:image") ? dk.fileData : `data:image/jpeg;base64,${dk.fileData}`) || [];
    
      const videos = post.baiVietDinhKemResponseList
        ?.filter((dk) => dk.loaiDK === "video")
        ?.map((dk) => dk.fileData && dk.fileData.startsWith("data:video") ? dk.fileData : `data:video/mp4;base64,${dk.fileData}`) || [];
    
      const totalMedia = images.length + videos.length;
    
      const user = JSON.parse(sessionStorage.getItem("userSignin"));
    
      if (post.trangThai !== "Bình Thường") {
        return null; // Don't render the post if its status is not "normal"
      }
      const handleDelete = async () => {
            // Hiển thị thông báo xác nhận trước khi xóa bài viết
            const confirmed = await confirm({
                  title: "Xóa bài viết",
                  text: "Bạn có chắc chắn muốn xóa bài viết này?",
                });
            
            if (confirmed) {
              try {
                // Gửi yêu cầu xóa bài viết lên backend
                await axios.delete(`http://localhost:8080/api/fakebook/posts/${post.maBV}`);
                
                // Thông báo thành công
                toast.success("Bài viết đã được xóa.");
                
                // Gọi lại callback onDelete từ props để xóa bài viết khỏi UI
                onDelete(post.maBV);
              } catch (error) {
                // Xử lý lỗi nếu có
                console.error("Có lỗi xảy ra khi xóa bài viết:", error);
                toast.error("Có lỗi xảy ra khi xóa bài viết. Vui lòng thử lại.");
              }
            }
          };
          
      const toggleLike = async () => {
        if (!user) {
          toast.info("Bạn cần đăng nhập để thích bài viết");
          return;
        }
    
        const luotThichDTO = {
          maTK: user.maTK,
          maBV: post.maBV,
        };
    
        try {
          // Gửi yêu cầu lên backend để toggle like
          const response = await axios.post(
            "http://localhost:8080/api/fakebook/posts/like",
            luotThichDTO
          );
          const isLikedNow = response.data === true; // true nếu đã like, false nếu unlike
    
          // Cập nhật trạng thái like theo phản hồi từ backend
          setHasLiked(isLikedNow);
          setLikes((prev) => (isLikedNow ? prev + 1 : prev - 1));
        } catch (error) {
          console.error("Có lỗi xảy ra khi thích bài viết:", error);
        }
      };
    
      return (
        <div className="post-card">
          <div className="post-header">
            <Link
              to={`/profile?id=${post?.taiKhoanBVAndBL?.maTK}`}
              className="post-author"
            >
              <img
                src={`Resource/Avatar/${
                  post?.taiKhoanBVAndBL?.profilePic || "default.png"
                }`}
                alt=""
                className="profile-pic"
              />
            </Link>
            <div className="d-flex flex-column align-items-start">
              <Link
                to={`/profile/${post?.taiKhoanBVAndBL?.maTK}`}
                className="post-author"
              >
                {post?.taiKhoanBVAndBL?.hoTen}
              </Link>
              <span className="post-time">
                {new Date(post?.thoiGian).toLocaleString("vi-VN", {
                  day: "2-digit",
                  month: "2-digit",
                  year: "numeric",
                  hour: "2-digit",
                  minute: "2-digit",
                  second: "2-digit",
                })}
              </span>
            </div>
    
            {/* Check if the current user is the author of the post */}
            {user?.maTK === post?.taiKhoanBVAndBL?.maTK && (
              <div className="ms-auto" style={{ position: "relative" }}>
                <button
                  className="menu-toggle"
                  onClick={() => setShowMenu(!showMenu)}
                >
                  ⋮
                </button>
                {showMenu && (
                  <div className="menu-dropdown">
                  <button
                        onClick={() => {
                        handleDelete(); // Gọi hàm handleDelete thay vì trực tiếp gọi onDelete
                        setShowMenu(false);
                        }}
                  >
                        🗑️ Xoá
                  </button>
                  </div>
                  )}
              </div>
            )}
          </div>
    
          <p
            className={`post-caption ${showFullCaption ? "expanded" : "collapsed"}`}
          >
            {caption}
          </p>
    
          {captionLength > maxCaptionLength && !showFullCaption && (
            <button
              className="toggle-caption"
              onClick={() => setShowFullCaption(true)}
            >
              Xem thêm
            </button>
          )}
          {showFullCaption && (
            <button
              className="toggle-caption"
              onClick={() => setShowFullCaption(false)}
            >
              Ẩn bớt
            </button>
          )}
    
          {(images.length > 0 || videos.length > 0) && (
            <div
              className={`media-container ${
                totalMedia === 1
                  ? "single"
                  : totalMedia === 2
                  ? "double"
                  : "multiple"
              }`}
            >
              {images.slice(0, maxPreview).map((src, i) => (
                <img
                  key={i}
                  src={src}
                  className="media-item"
                  alt={`img-${i}`}
                  onClick={() => {
                    if (images.length > maxPreview && i === maxPreview - 1) {
                      setShowModal(true);
                    }
                  }}
                />
              ))}
              {images.length > maxPreview && (
                <div
                  className="media-item more-overlay"
                  onClick={() => setShowModal(true)}
                >
                  +{images.length - maxPreview}
                </div>
              )}
    
              {videos.map((src, i) => (
                <video key={i} controls className="media-item">
                  <source src={src} type="video/mp4" />
                  Trình duyệt không hỗ trợ video.
                </video>
              ))}
            </div>
          )}
    
          <div className="post-actions">
            <button onClick={toggleLike}>
              {hasLiked ? "❤️ Đã thích" : "👍 Thích"} ({likes})
            </button>
            <button onClick={() => setShowComments((prev) => !prev)}>
              💬 Comment ({post.binhLuanList?.length || 0})
            </button>
            <button>↪️ Share</button>
          </div>
    
          {showComments && (
            <CommentSection
              comments={comments}
              setComments={setComments}
              onAddComment={(newComment) =>
                setComments((prev) => [...prev, newComment])
              }
              maBV={post.maBV}
            />
          )}
    
          {showModal && (
            <div className="media-modal" onClick={() => setShowModal(false)}>
              <div
                className="media-modal-content"
                onClick={(e) => e.stopPropagation()}
              >
                {images.map((src, i) => (
                  <img
                    key={i}
                    src={src}
                    className="modal-image"
                    alt={`modal-${i}`}
                  />
                ))}
              </div>
            </div>
          )}
        </div>
      );
    };
    
    export default Post;
    