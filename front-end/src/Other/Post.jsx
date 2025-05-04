import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import axios from "axios";
import CommentSection from "./CommentSection";
import PostForm from "./PostForm";
import "../CSS/Post.css";
import { toast } from "react-toastify";

const Post = ({ post, onDelete, onUpdate }) => {
  const [likes, setLikes] = useState(post.luotThichList?.length || 0);
  const [hasLiked, setHasLiked] = useState(() => {
    const user = JSON.parse(sessionStorage.getItem("userSignin"));
    return post.luotThichList?.some((like) => like.maTK === user?.maTK);
  });
  const [comments, setComments] = useState(post.binhLuanList || []);
  const [showComments, setShowComments] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [showFullCaption, setShowFullCaption] = useState(false);
  const [showMenu, setShowMenu] = useState(false);
  const [showModal, setShowModal] = useState(false);

  const maxPreview = 4;
  const maxCaptionLength = 200;
  const caption = post.noiDung || ""; // xử lý khi noiDung là null
  const captionLength = caption.length;

  // Phân tách ảnh/video từ baiVietDinhKemResponseList
const images = post.baiVietDinhKemResponseList
?.filter((dk) => dk.loaiDK === "image")
?.map((dk) => dk.fileData && dk.fileData.startsWith('data:image') ? dk.fileData : `data:image/jpeg;base64,${dk.fileData}`) || []; // Chuyển đổi từ bytes thành ảnh

const videos = post.baiVietDinhKemResponseList
?.filter((dk) => dk.loaiDK === "video")
?.map((dk) => dk.fileData && dk.fileData.startsWith('data:video') ? dk.fileData : `data:video/mp4;base64,${dk.fileData}`) || []; // Chuyển đổi từ bytes thành video

const totalMedia = images.length + videos.length;


  const user = JSON.parse(sessionStorage.getItem("userSignin"));

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
      setHasLiked((prev) => (prev = isLikedNow));
      setLikes((prev) => (isLikedNow ? prev + 1 : prev - 1));

      // Cập nhật thông tin bài viết nếu cần
      if (onUpdate) {
        onUpdate(post);
      }
    } catch (error) {
      console.error("Có lỗi xảy ra khi thích bài viết:", error);
    }
  };

  return (
    <div className="post-card">
      {isEditing ? (
        <PostForm
          maTK={user?.maTK}
          postToEdit={post}
          onPostSubmit={(updatedPost) => {
            onUpdate(updatedPost);
            setIsEditing(false);
          }}
          onEditDone={() => setIsEditing(false)}
        />
      ) : (
        <>
          <div className="post-header">
            <Link
              to={`/profile/${post?.taiKhoanBVAndBL?.maTK}`}
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
                      setIsEditing(true);
                      setShowMenu(false);
                    }}
                  >
                    ✏️ Sửa
                  </button>
                  <button
                    onClick={() => {
                      onDelete(post.maBV);
                      setShowMenu(false);
                    }}
                  >
                    🗑️ Xoá
                  </button>
                </div>
              )}
            </div>
          </div>

          <p
            className={`post-caption ${
              showFullCaption ? "expanded" : "collapsed"
            }`}
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
        </>
      )}
    </div>
  );
};

export default Post;
