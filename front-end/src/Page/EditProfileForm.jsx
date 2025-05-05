import React from "react";
import "../CSS/EditProfileForm.css";

const EditProfileForm = ({ userName, onSave, onCancel }) => {
  const handleSubmit = (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);

    // Lấy dữ liệu từ form
    const updatedData = {
      userName: formData.get("user-name"),
      profilePic: formData.get("profile-pic"),
      coverPic: formData.get("cover-pic"),
    };

    // Gửi dữ liệu lên component cha (App.js) để xử lý
    onSave(updatedData);
  };

  return (
    <div className="edit-profile-overlay">
      <div className="edit-profile-container">
        <h2 className="edit-profile-title">Chỉnh sửa trang cá nhân</h2>
        <form onSubmit={handleSubmit}>
          <div className="edit-profile-group">
            <label htmlFor="profile-pic" className="edit-profile-label">
              Ảnh đại diện:
            </label>
            <input
              type="file"
              id="profile-pic"
              name="profile-pic"
              accept="image/*"
              className="edit-profile-input"
            />
          </div>
          <div className="edit-profile-group">
            <label htmlFor="cover-pic" className="edit-profile-label">
              Ảnh bìa:
            </label>
            <input
              type="file"
              id="cover-pic"
              name="cover-pic"
              accept="image/*"
              className="edit-profile-input"
            />
          </div>
          <div className="edit-profile-group">
            <label htmlFor="user-name" className="edit-profile-label">
              Tên người dùng:
            </label>
            <input
              type="text"
              id="user-name"
              name="user-name"
              defaultValue={userName}
              className="edit-profile-input"
            />
          </div>
          <div className="edit-profile-actions">
            <button type="submit" className="edit-profile-save">
              Lưu
            </button>
            <button
              type="button"
              onClick={onCancel}
              className="edit-profile-cancel"
            >
              Hủy
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default EditProfileForm;