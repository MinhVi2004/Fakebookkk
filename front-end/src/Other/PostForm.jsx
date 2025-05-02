import { useState, useEffect, useRef } from "react";
import "../CSS/PostForm.css";

const PostForm = ({ onPostSubmit, postToEdit, onEditDone }) => {
  const [caption, setCaption] = useState("");
  const [images, setImages] = useState([]);
  const [videos, setVideos] = useState([]);

  const imageInputRef = useRef(null);
  const videoInputRef = useRef(null);

  useEffect(() => {
    if (postToEdit) {
      setCaption(postToEdit.caption || "");
      setImages(postToEdit.images || []);
      setVideos(postToEdit.videos || []);
    }
  }, [postToEdit]);

  const handlePostSubmit = () => {
    const post = {
      id: postToEdit ? postToEdit.id : Date.now(),
      user: { id: 101, name: "Nguyễn Văn A" },
      caption,
      images,
      videos,
      time: "Vài giây trước",
    };

    onPostSubmit(post);
    setCaption("");
    setImages([]);
    setVideos([]);
    if (onEditDone) onEditDone();
  };

  const handleImageChange = (e) => {
    const files = Array.from(e.target.files);
    const newImages = files
      .map((file) => URL.createObjectURL(file))
      .filter((url) => !images.includes(url));
    setImages((prev) => [...prev, ...newImages]);
  };

  const handleVideoChange = (e) => {
    const files = Array.from(e.target.files);
    const newVideos = files
      .map((file) => URL.createObjectURL(file))
      .filter((url) => !videos.includes(url));
    setVideos((prev) => [...prev, ...newVideos]);
  };

  const removeImage = (index) => {
    setImages((prev) => prev.filter((_, i) => i !== index));
  };

  const removeVideo = (index) => {
    setVideos((prev) => prev.filter((_, i) => i !== index));
  };

  // Kiểm tra hợp lệ: ít nhất có caption hoặc image hoặc video
  const isPostValid =
    caption.trim().length > 0 || images.length > 0 || videos.length > 0;

  return (
    <div className="post-form">
      <textarea
        placeholder="Bạn đang nghĩ gì?"
        value={caption}
        onChange={(e) => setCaption(e.target.value)}
        style={{ minHeight: "100px", maxHeight: "300px", overflowY: "auto" }}
      />

      <div className="media-upload">
        <label className="upload-btn">
          Chọn ảnh
          <input
            type="file"
            accept="image/*"
            multiple
            onChange={handleImageChange}
            ref={imageInputRef}
          />
        </label>

        <label className="upload-btn">
          Chọn video
          <input
            type="file"
            accept="video/*"
            multiple
            onChange={handleVideoChange}
            ref={videoInputRef}
          />
        </label>
      </div>

      {/* Hiển thị preview ảnh */}
      {images.length > 0 && (
        <div className="preview-container">
          {images.map((img, idx) => (
            <div key={idx} className="preview-item">
              <img src={img} alt={`preview-${idx}`} className="preview-img" />
              <button className="remove-btn" onClick={() => removeImage(idx)}>
                ×
              </button>
            </div>
          ))}
        </div>
      )}

      {/* Hiển thị preview video */}
      {videos.length > 0 && (
        <div className="preview-container">
          {videos.map((vid, idx) => (
            <div key={idx} className="preview-item">
              <video controls className="preview-video">
                <source src={vid} type="video/mp4" />
                Trình duyệt không hỗ trợ video.
              </video>
              <button className="remove-btn" onClick={() => removeVideo(idx)}>
                ×
              </button>
            </div>
          ))}
        </div>
      )}

      <div className="actions-row">
        <button onClick={handlePostSubmit} disabled={!isPostValid}>
          {postToEdit ? "Cập nhật" : "Đăng bài"}
        </button>
      </div>
    </div>
  );
};

export default PostForm;
