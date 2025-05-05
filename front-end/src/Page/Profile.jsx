import React, { useState, useEffect } from "react";
import { toast } from "react-toastify";
import EditProfileForm from "./EditProfileForm"; // Import form chỉnh sửa
import "../CSS/Profile.css";
import { useNavigation } from "../Other/Navigation"; // Import useNavigation từ file navigation.js

export default function Profile() {
     const { goToSignin } = useNavigation();
     const storedUserData = JSON.parse(sessionStorage.getItem("userSignin"));
   
     // Check if user is not signed in, and navigate using useEffect
     useEffect(() => {
       console.log(sessionStorage.getItem("userSignin"));
       if (!storedUserData) {
        
         toast.error("Bạn chưa đăng nhập, vui lòng đăng nhập để tiếp tục");
         goToSignin(); // Chuyển hướng đến trang đăng nhập nếu không có thông tin người dùng
       }
     }, [storedUserData, goToSignin]);
   
     // Trạng thái cho thông tin người dùng
     const [userName, setUserName] = useState(storedUserData?.hoTen || "");
     const [profilePic, setProfilePic] = useState(
          storedUserData?.profilePic ? `Resource/Avatar/${storedUserData.profilePic}` : ""
        );
     const [coverPic, setCoverPic] = useState(
          storedUserData?.coverPic ? `Resource/Avatar/${storedUserData.coverPic}` : ""
        );
        
     const [isEditFormOpen, setIsEditFormOpen] = useState(false);
   
     // Xử lý mở/đóng form chỉnh sửa
     const showEditForm = () => setIsEditFormOpen(true);
     const hideEditForm = () => setIsEditFormOpen(false);
   
     // Xử lý cập nhật thông tin từ form
     const updateProfile = (updatedData) => {
       // Cập nhật tên người dùng
       if (updatedData.userName) setUserName(updatedData.userName);
   
       // Cập nhật ảnh đại diện
       if (updatedData.profilePic && updatedData.profilePic.size > 0) {
         const reader = new FileReader();
         reader.onload = (e) => setProfilePic(e.target.result);
         reader.readAsDataURL(updatedData.profilePic);
       }
   
       // Cập nhật ảnh bìa
       if (updatedData.coverPic && updatedData.coverPic.size > 0) {
         const reader = new FileReader();
         reader.onload = (e) => setCoverPic(e.target.result);
         reader.readAsDataURL(updatedData.coverPic);
       }
   
       // Cập nhật lại sessionStorage
       const updatedUserData = { 
         userName: updatedData.userName, 
         profilePic: updatedData.profilePic, 
         coverPic: updatedData.coverPic 
       };
       sessionStorage.setItem("userSignin", JSON.stringify(updatedUserData));
   
       // Đóng form sau khi lưu
       hideEditForm();
     };
   
     if (!storedUserData) {
       // Return null to avoid rendering the profile if not signed in
       return null;
     }
   
     return (
       <div className="profile-container">
         <div className="max-w-6xl mx-auto">
           {/* Phần ảnh bìa */}
           <div className="cover-photo">
             {coverPic ? (
               <img src={coverPic} alt="Ảnh bìa" />
             ) : (
               <div className="default-cover">Chưa có ảnh bìa</div> // Fallback content if no cover photo
             )}
             <button onClick={() => console.log("Change cover photo")}>📷 Thêm ảnh bìa</button>
           </div>
           {/* Phần thông tin cá nhân */}
           <div className="profile-info">
             <div className="profile-avatar">
               {profilePic ? (
                 <img src={profilePic} alt="Ảnh đại diện" />
               ) : (
                 <div className="default-avatar">Không có ảnh</div> // Fallback content if no profile pic
               )}
             </div>
             <div className="profile-name">
               <h1>{userName}</h1>
               <p>1.2K người bạn</p>
             </div>
             <div className="profile-buttons">
               <button onClick={showEditForm}>✏️ Chỉnh sửa trang cá nhân</button>
             </div>
             {isEditFormOpen && (
               <EditProfileForm
                 userName={userName}
                 onSave={(updatedData) => {
                   updateProfile(updatedData); // Cập nhật thông tin
                 }}
                 onCancel={hideEditForm} // Đóng form khi nhấn "Hủy"
               />
             )}
           </div>
   
           {/* Phần bài viết */}
           <div className="post-container">
             <div className="post-input">
               <div className="post-input-header">
                 {profilePic ? (
                   <img src={profilePic} alt="Ảnh đại diện" />
                 ) : (
                   <div className="default-avatar">Không có ảnh</div> // Fallback content if no profile pic
                 )}
                 <input type="text" placeholder="Bạn đang nghĩ gì?" />
               </div>
               <div className="post-options">
                 <button>📷 Thêm ảnh</button>
                 <button>🎥 Thêm video</button>
                 <button>📍 Địa điểm</button>
               </div>
             </div>
   
             <div className="post-content">
               <div className="post-header">
                 {profilePic ? (
                   <img src={profilePic} alt="Ảnh đại diện" />
                 ) : (
                   <div className="default-avatar">Không có ảnh</div> // Fallback content if no profile pic
                 )}
                 <div className="post-header-info">
                   <h3>{userName}</h3>
                   <p>2 giờ trước</p>
                 </div>
               </div>
               <p>Hihihi</p>
               <img src="/Images/8.jpg" alt="Ảnh bài viết" className="picture" />
               <div className="post-actions">
                 <button>Thích </button>
                 <button>Bình luận</button>
                 <button>Chia sẻ</button>
               </div>
             </div>
           </div>
         </div>
       </div>
     );
}