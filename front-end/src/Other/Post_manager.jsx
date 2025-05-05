import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import axios from "axios";
import CommentSection from "./CommentSection";
import "../CSS/PostManager.css";
import { toast } from "react-toastify";

const Post_manager = ({ post, onDelete, onUpdate }) => {
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
  const [isPostHidden, setIsPostHidden] = useState(post.trangThai === 'Đã Ẩn'); // Track the post's hidden status

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

  const handleHide = async () => {
    if (!user) {
      toast.info("Bạn cần đăng nhập để thay đổi trạng thái bài viết");
      return;
    }

    try {
      // Gửi yêu cầu PUT để thay đổi trạng thái bài viết thành ẩn
      const response = await axios.put(
        `http://localhost:8080/api/fakebook/admin/posts/${post.maBV}/hidePost`,
      );

      if (response.status === 200) {
        setIsPostHidden(true); // Cập nhật trạng thái bài viết thành ẩn
        toast.success("Bài viết đã được ẩn.");
        if (onUpdate) {
          // Cập nhật lại trạng thái bài viết nếu cần
          onUpdate(post);
        }
      }
    } catch (error) {
      console.error("Có lỗi xảy ra khi ẩn bài viết:", error);
      toast.error("Có lỗi khi ẩn bài viết.");
    }
  };

  const handleUnHide = async () => {
    if (!user) {
      toast.info("Bạn cần đăng nhập để thay đổi trạng thái bài viết");
      return;
    }

    try {
      // Gửi yêu cầu PUT để thay đổi trạng thái bài viết thành hiện
      const response = await axios.put(
        `http://localhost:8080/api/fakebook/admin/posts/${post.maBV}/unHidePost`,
      );

      if (response.status === 200) {
        setIsPostHidden(false); // Cập nhật trạng thái bài viết thành hiện
        toast.success("Bài viết đã được hiển thị.");
        if (onUpdate) {
          // Cập nhật lại trạng thái bài viết nếu cần
          onUpdate(post);
        }
      }
    } catch (error) {
      console.error("Có lỗi xảy ra khi hiện bài viết:", error);
      toast.error("Có lỗi khi hiện bài viết.");
    }
  };

  return (
    <div className="post-card">
      <>
        <div className="post-header">
          <img
            src={`../Resource/Avatar/${
              post?.nguoiDang?.profilePic || "default.png"
            }`}
            alt=""
            className="profile-pic"
          />
          <div className="d-flex flex-column align-items-start">
            <Link
              to={`/profile/${post?.nguoiDang?.maTK}`}
              className="post-author"
            >
              {post?.nguoiDang?.hoTen}
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
                {isPostHidden ? (
                  <button
                    onClick={() => {
                      handleUnHide();
                      setShowMenu(false);
                    }}
                  >
                    Hiện bài viết
                  </button>
                ) : (
                  <button
                    onClick={() => {
                      handleHide();
                      setShowMenu(false);
                    }}
                  >
                    Ẩn bài viết
                  </button>
                )}
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
      </>
    </div>
  );
};

export default Post_manager;
