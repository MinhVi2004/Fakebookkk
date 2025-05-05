import { useState, useEffect, useRef } from "react";
import "../CSS/PostForm.css";
import { toast } from "react-toastify";
import axios from "axios";

const PostForm = ({ maTK, onPostSubmit, postToEdit, onEditDone }) => {
  const [noiDung, setNoiDung] = useState("");
  const [loaiChiaSe, setLoaiChiaSe] = useState("Tất Cả");
  const [imagesPreview, setImagesPreview] = useState([]);
  const [videosPreview, setVideosPreview] = useState([]);
  const [imageFiles, setImageFiles] = useState([]);
  const [videoFiles, setVideoFiles] = useState([]);

  const imageInputRef = useRef(null);
  const videoInputRef = useRef(null);

  useEffect(() => {
    if (postToEdit) {
      setNoiDung(postToEdit.noiDung || "");
      setLoaiChiaSe(postToEdit.loaiChiaSe || "Tất Cả");

      const dinhKems = postToEdit.dinhKems || [];
      setImagesPreview(
        dinhKems
          .filter((dk) => dk.loaiDK === "image")
          .map((dk) => `http://localhost:8080/Resource/DinhKem/${dk.linkDK}`)
      );
      setVideosPreview(
        dinhKems
          .filter((dk) => dk.loaiDK === "video")
          .map((dk) => `http://localhost:8080/Resource/DinhKem/${dk.linkDK}`)
      );
    }
  }, [postToEdit]);

  const handlePostSubmit = async () => {
    if (!maTK) {
      toast.info("Vui lòng đăng nhập để đăng bài.");
      return;
    }

    const formData = new FormData();
const baiVietDTO = { maTK, noiDung, loaiChiaSe };
formData.append("baiViet", JSON.stringify(baiVietDTO));

// Hàm chuyển file sang Base64
const fileToBase64 = (file) => {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.onloadend = () => {
      const result = reader.result;
      // const base64 = result.split(',')[1]; // Chỉ lấy phần sau dấu phẩy
      resolve(result);
    };
    reader.onerror = reject;
    reader.readAsDataURL(file);
  });
};


const dinhKems = [];
const loaiDKs = [];

for (const file of imageFiles) {
  const base64File = await fileToBase64(file);
  dinhKems.push(base64File);
  loaiDKs.push("image");
}

for (const file of videoFiles) {
  const base64File = await fileToBase64(file);
  dinhKems.push(base64File);
  loaiDKs.push("video");
}

// Append mảng JSON vào formData
formData.append("dinhKems", JSON.stringify(dinhKems));
formData.append("loaiDKs", JSON.stringify(loaiDKs));

//     const formData = new FormData();
// const baiVietDTO = { maTK, noiDung, loaiChiaSe };
// formData.append("baiViet", JSON.stringify(baiVietDTO));

// // Hàm mã hóa file sang Base64
// const fileToBase64 = (file) => {
//   return new Promise((resolve, reject) => {
//     const reader = new FileReader();
//     reader.onloadend = () => {
//       resolve(reader.result); // Trả về chuỗi Base64 có header
//     };
//     reader.onerror = reject;
//     reader.readAsDataURL(file); // Đọc file dưới dạng Base64
//   });
// };

// const dinhKems = []; // Mảng chứa base64 (đã bỏ header)
// const loaiDKs = [];  // Mảng loại file

// // Xử lý file ảnh
// for (const file of imageFiles) {
//   const base64File = await fileToBase64(file);
//   const base64Data = base64File.split(",")[1]; // Loại bỏ phần header
//   dinhKems.push(base64Data);
//   loaiDKs.push("image");
// }

// // Xử lý file video
// for (const file of videoFiles) {
//   const base64File = await fileToBase64(file);
//   const base64Data = base64File.split(",")[1]; // Loại bỏ phần header
//   dinhKems.push(base64Data);
//   loaiDKs.push("video");
// }

// // Gửi lên backend 1 lần duy nhất
// formData.append("dinhKems", JSON.stringify(dinhKems));
// formData.append("loaiDKs", JSON.stringify(loaiDKs));

    try {
      const response = await axios.post(
        `http://localhost:8080/api/fakebook/posts/create`,
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        }
      );

      if (response.status !== 200) throw new Error("Tạo bài viết thất bại.");

      onPostSubmit(); // Gọi lại danh sách bài viết

      // Reset các giá trị
      setNoiDung("");
      setLoaiChiaSe("Tất Cả");
      setImagesPreview([]);
      setVideosPreview([]);
      setImageFiles([]);
      setVideoFiles([]);
      if (imageInputRef.current) imageInputRef.current.value = null;
      if (videoInputRef.current) videoInputRef.current.value = null;
      if (onEditDone) onEditDone();
    } catch (err) {
      console.error(err);
      toast.error("Tạo bài viết thất bại. Vui lòng thử lại sau.");
    }
  };

  const handleImageChange = (e) => {
    const files = Array.from(e.target.files);
    const urls = files.map((file) => URL.createObjectURL(file));
    setImagesPreview((prev) => [...prev, ...urls]);
    setImageFiles((prev) => [...prev, ...files]);
    e.target.value = null; // Cho phép chọn lại file đã chọn
  };

  const handleVideoChange = (e) => {
    const files = Array.from(e.target.files);
    const urls = files.map((file) => URL.createObjectURL(file));
    setVideosPreview((prev) => [...prev, ...urls]);
    setVideoFiles((prev) => [...prev, ...files]);
    e.target.value = null;
  };

  const removeImage = (index) => {
    setImagesPreview((prev) => prev.filter((_, i) => i !== index));
    setImageFiles((prev) => prev.filter((_, i) => i !== index));
  };

  const removeVideo = (index) => {
    setVideosPreview((prev) => prev.filter((_, i) => i !== index));
    setVideoFiles((prev) => prev.filter((_, i) => i !== index));
  };

  const isPostValid =
    noiDung.trim().length > 0 || imageFiles.length > 0 || videoFiles.length > 0;

  return (
    <div className="post-form">
      <textarea
        placeholder="Bạn đang nghĩ gì?"
        value={noiDung}
        onChange={(e) => setNoiDung(e.target.value)}
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

        <div className="share-option">
          <label>Chia sẻ với:</label>&nbsp;
          <select
            value={loaiChiaSe}
            onChange={(e) => setLoaiChiaSe(e.target.value)}
          >
            <option value="Tất Cả">Tất Cả</option>
            <option value="Bạn Bè">Bạn Bè</option>
            <option value="Chỉ Mình Tôi">Chỉ Mình Tôi</option>
          </select>
        </div>
      </div>

      {imagesPreview.length > 0 && (
        <div className="preview-container">
          {imagesPreview.map((img, idx) => (
            <div key={idx} className="preview-item">
              <img src={img} alt={`preview-${idx}`} className="preview-img" />
              <button className="remove-btn" onClick={() => removeImage(idx)}>
                ×
              </button>
            </div>
          ))}
        </div>
      )}

      {videosPreview.length > 0 && (
        <div className="preview-container">
          {videosPreview.map((vid, idx) => (
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
