import { useState } from "react";
import { Link } from "react-router-dom";
import CommentSection from "./CommentSection";
import PostForm from "./PostForm";
import "../CSS/Post.css";

const Post = ({ post, onDelete, onUpdate }) => {
  const [likes, setLikes] = useState(0);
  const [showComments, setShowComments] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [showFullCaption, setShowFullCaption] = useState(false);
  const [showMenu, setShowMenu] = useState(false);
  const [showModal, setShowModal] = useState(false);

  const images = Array.isArray(post.images) ? post.images : [];
  const videos = Array.isArray(post.videos) ? post.videos : [];
  const maxPreview = 4;

  const maxCaptionLength = 200; // Độ dài tối đa để hiển thị "Xem thêm"
  const captionLength = post.caption.length;

  // Tính tổng media
  const totalMedia = images.length + videos.length;

  return (
    <div className="post-card">
      {isEditing ? (
        <PostForm
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
            <Link to={`/profile/${post.user.id}`} className="post-author">
              {post.user.name}
            </Link>

            <div style={{ position: "relative" }}>
              <button className="menu-toggle" onClick={() => setShowMenu(!showMenu)}>
                ⋮
              </button>
              {showMenu && (
                <div className="menu-dropdown">
                  <button onClick={() => { setIsEditing(true); setShowMenu(false); }}>
                    ✏️ Sửa
                  </button>
                  <button onClick={() => { onDelete(post.id); setShowMenu(false); }}>
                    🗑️ Xoá
                  </button>
                </div>
              )}
            </div>
          </div>

          <span className="post-time">{post.time}</span>

          {/* Nội dung bài viết */}
          <p
            className={`post-caption ${showFullCaption ? "expanded" : "collapsed"}`}
          >
            {post.caption}
          </p>

          {/* Hiển thị nút "Xem thêm" nếu caption dài */}
          {captionLength > maxCaptionLength && !showFullCaption && (
            <button
              className="toggle-caption"
              onClick={() => setShowFullCaption(!showFullCaption)}
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

          {/* Hiển thị media */}
          {(images.length > 0 || videos.length > 0) && (
            <div
              className={`media-container ${
                totalMedia === 1 ? "single" : totalMedia === 2 ? "double" : "multiple"
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
                <div className="media-item more-overlay" onClick={() => setShowModal(true)}>
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

          {/* Các nút Like, Comment, Share */}
          <div className="post-actions">
            <button onClick={() => setLikes((prev) => prev + 1)}>👍 Like ({likes})</button>
            <button onClick={() => setShowComments((prev) => !prev)}>💬 Comment</button>
            <button>↪️ Share</button>
          </div>

          {/* Khu vực bình luận */}
          {showComments && <CommentSection />}

          {/* Modal xem toàn bộ ảnh */}
          {showModal && (
            <div className="media-modal" onClick={() => setShowModal(false)}>
              <div className="media-modal-content" onClick={(e) => e.stopPropagation()}>
                {images.map((src, i) => (
                  <img key={i} src={src} className="modal-image" alt={`modal-${i}`} />
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
