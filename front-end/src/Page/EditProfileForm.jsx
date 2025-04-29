import React from "react";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "../CSS/EditProfileForm.css";

const EditProfileForm = ({ userName, storedUserData, onSave, onCancel }) => {
  const handleSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData();

    // Kiểm tra và thêm tên người dùng nếu thay đổi
    const userNameInput = e.target["user-name"].value;
    if (userNameInput !== storedUserData.userName) {
      formData.append("userName", userNameInput);
    }

    // Kiểm tra và thêm ảnh đại diện nếu có file được chọn
    const profilePic = e.target["profile-pic"].files[0];
    if (profilePic && profilePic.size > 0) {
      formData.append("profilePic", profilePic);
    }

    // Kiểm tra và thêm ảnh bìa nếu có file được chọn
    const coverPic = e.target["cover-pic"].files[0];
    if (coverPic && coverPic.size > 0) {
      formData.append("coverPic", coverPic);
    }

    // Log dữ liệu trong formData để kiểm tra
    for (let [key, value] of formData.entries()) {
      console.log(`${key}:`, value);
    }

    try {
      const response = await fetch(
        `http://localhost:8080/api/fakebook/update-profile/${storedUserData.maTK}`,
        {
          method: "PUT",
          body: formData,
        }
      );

      if (response.ok) {
        const updatedUser = await response.json();
        toast.success("Cập nhật thông tin thành công");
        onSave(updatedUser.data); // Gửi dữ liệu cập nhật lên component cha
      } else {
        toast.error("Cập nhật thất bại");
      }
    } catch (error) {
      console.error("Error:", error);
      toast.error("Có lỗi xảy ra khi cập nhật thông tin");
    }
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
