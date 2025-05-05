import React, { useState, useEffect } from "react";
import { toast } from "react-toastify";
import { Link } from "react-router-dom";
import { useNavigation } from "../Other/Navigation"; // Import useNavigation từ file navigation.js
import UserList from "./UserManager";
import "./../CSS/Admin.css";
import { Outlet } from "react-router-dom"; // Chỗ này để render các route con
function Admin() {
     const { goToSignin } = useNavigation();
     const storedUserData = JSON.parse(sessionStorage.getItem("userSignin"));
     
     // Check if user is not signed in, and navigate using useEffect
     useEffect(() => {
          if (!storedUserData) {
          toast.error("Bạn chưa đăng nhập, vui lòng đăng nhập để tiếp tục");
          goToSignin(); // Chuyển hướng đến trang đăng nhập nếu không có thông tin người dùng
          }
     }, [storedUserData, goToSignin]);

     async function handlesignin(e) {
          e.preventDefault(); // Ngừng sự kiện mặc định (ngừng reload trang)

          const response = await fetch(
               "http://localhost:8080/api/fakebook/signin",
               {
                    method: "POST",
                    headers: {
                         "Content-Type": "application/json",
                    },
                    body: JSON.stringify({ tenDangNhap, matKhau }), // Gửi dữ liệu đăng nhập dưới dạng JSON
               }
          );

          const data = await response.json();
          if (response.ok) {
               const userData = data.data;
               sessionStorage.setItem('userSignin', JSON.stringify(userData));
               toast.success("Đăng nhập thành công");

               // Chuyển hướng đến trang profile
               goToProfile(); 
          } else {
               toast.error(data.message || "Đăng nhập thất bại");
          }
     }
     return (
          <div id="admin-wrapper">
               <div id="admin-leftbar">
                    <div id="admin-leftbar-profile">
                         <div className="profile-avatar">
                              <img
                                   src={storedUserData?.profilePic ? `../Resource/Avatar/${storedUserData.profilePic}` : ""}
                                   alt="Profile"
                                   id="admin-leftbar-profile-pic"
                              />
                         </div>
                         <h2 id="admin-leftbar-profile-name">{storedUserData?.hoTen}</h2>
                    </div>
                    <ul id="admin-leftbar-nav">
                         <Link to={"/admin/users"} className="admin-leftbar-nav-item">Người Dùng</Link>
                         <Link to={"/admin/posts"} className="admin-leftbar-nav-item">Bài Viết</Link>
                    </ul>
               </div>
               <div id="admin-content">
                    <Outlet />
               </div>
          </div>
     );
}
export default Admin;
