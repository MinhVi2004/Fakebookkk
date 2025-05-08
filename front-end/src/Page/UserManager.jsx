     import React, { useState, useEffect } from "react";
     import "../CSS/UserManager.css";
     import { toast } from "react-toastify";
     import confirm from "../Other/Confirm";
     function UserList() {
          const [users, setUsers] = useState([]);
          const [selectedUser, setSelectedUser] = useState(null);

          // Fetch từ API Spring Boot
          useEffect(() => {
               fetch("http://localhost:8080/api/fakebook/admin/users")
                    .then((res) => res.json())
                    .then((data) => {
                         setUsers(data); // data là mảng user
                    })
                    .catch((err) =>
                         console.error("Lỗi khi lấy danh sách người dùng:", err)
                    );
          }, []);

          const handleClose = () => setSelectedUser(null);

          return (
               <div className="user-list-container">
                    <h2>Danh sách người dùng</h2>
                    <ul className="user-list">
                         {users
                              .filter((user) => user.phanQuyen === "Người Dùng")
                              .map((user) => (
                                   <li
                                        key={user.maTK}
                                        className="user-item"
                                        onClick={() => setSelectedUser(user)}
                                   >
                                        <div style={{ display: "flex", alignItems: "center" }}>
                                        <img
                                             src={`../Resource/Avatar/${user.profilePic}`}
                                             alt=""
                                        />
                                        <div>
                                             <h4>{user.hoTen}</h4>
                                             <p>{user.email}</p>
                                        </div>
                                        </div>
                                          <span
                                                className={`user-status ${
                                                      user.trangThai === "Bình Thường"
                                                      ? "Bình Thường"
                                                      : "Vô Hiệu Hóa"}`}
                                                      >
                                                Trạng Thái : {user.trangThai === "Bình Thường"?"Đang Hoạt Động":"Bị Khóa"}
                                          </span>
                                   </li>
                              ))}
                    </ul>

                    {selectedUser && (
                         <UserDetailModal
                              user={selectedUser}
                              onClose={handleClose}
                              setUsers={setUsers}
                              setSelectedUser={setSelectedUser}
                         />
                    )}
               </div>
          );
     }

     function UserDetailModal({ user, onClose, setUsers, setSelectedUser }) {
      const handleToggleStatus = async () => {
        const isDisabled = user.trangThai === "Vô Hiệu Hóa";
        const confirmed = await confirm({
          title: "Xác nhận",
          text: `Bạn có chắc chắn muốn ${
            isDisabled ? "mở khóa" : "khóa"
          } tài khoản này ?`,
        });
    
        if (!confirmed) return;
    
        const url = isDisabled
          ? `http://localhost:8080/api/fakebook/admin/users/enable/${user.maTK}`
          : `http://localhost:8080/api/fakebook/admin/users/disable/${user.maTK}`;
    
        fetch(url, { method: "PUT" })
          .then((res) => {
            if (!res.ok) throw new Error("Lỗi khi cập nhật trạng thái");
            return res.json();
          })
          .then((data) => {
            toast.success(`Đã ${isDisabled ? "mở khóa" : "khóa"} tài khoản thành công!`);
            setUsers((prev) => prev.map((u) => (u.maTK === user.maTK ? data : u)));
            setSelectedUser(data);
          })
          .catch((err) => {
            console.error("Lỗi khi thay đổi trạng thái:", err);
            toast.error("Đã có lỗi xảy ra khi cập nhật trạng thái.");
          });
      };
    
      return (
        <div className="modal-overlay" onClick={onClose}>
          <div className="modal-container" onClick={(e) => e.stopPropagation()}>
            <button className="modal-close" onClick={onClose}>×</button>
    
            {/* Ảnh bìa */}
            <div className="cover-pic-container">
              <img
                src={
                  user.coverPic
                    ? `../Resource/Background/${user.coverPic}`
                    : "../Resource/Background/default-cover.jpg"
                }
                alt="Cover"
                className="cover-pic"
              />
            </div>
    
            {/* Ảnh đại diện */}
            <img
              src={
                user.profilePic
                  ? `../Resource/Avatar/${user.profilePic}`
                  : "../Resource/Avatar/default.png"
              }
              alt="Avatar"
              className="modal-avatar"
            />
    
            <div className="modal-info">
              <p><strong>Họ Tên: </strong> {user.hoTen}</p>
              <p><strong>Email: </strong> {user.email}</p>
              <p><strong>Vai trò: </strong> {user.phanQuyen}</p>
              <p><strong>SĐT: </strong> {user.soDienThoai || "Chưa cập nhật"}</p>
              <p><strong>Ngày tạo: </strong> {user.ngayTao || "Không rõ"}</p>
              <p>
                <strong>Trạng thái: </strong>
                <span style={{ color: user.trangThai === "Bình Thường" ? "green" : "red" }}>
                  {user.trangThai}
                </span>
              </p>
            </div>
    
            <button className="status-button" onClick={() => handleToggleStatus(user)}>
              {user.trangThai === "Vô Hiệu Hóa" ? "Mở khóa tài khoản" : "Khóa tài khoản"}
            </button>
          </div>
        </div>
      );
    }

     export default UserList;
