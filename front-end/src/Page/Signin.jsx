import React, { useState } from "react";
import { toast } from "react-toastify";
import { Link } from "react-router-dom";

import "./../CSS/Signin.css";
import { useNavigation } from "../Other/Navigation"; // Import useNavigation từ file navigation.js
function Signin() {
     sessionStorage.removeItem("userSignin"); // Xóa thông tin người dùng trong sessionStorage khi vào trang đăng nhập
     const { goToSignup, goToProfile } = useNavigation();
     const [tenDangNhap, setTenDangNhap] = useState(""); // Khai báo state cho username
     const [matKhau, setMatKhau] = useState(""); // Khai báo state cho password
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
          <div className="d-flex justify-content-center align-items-center vw-100  vh-100">
               <div className="m-4">
                    <p className="text-primary fw-bold fs-1 ">facebook</p>
                    <p className="text-gray-700 text-lg mt-2 fs-4">
                         Connect with friends and the world around you on
                         Facebook.
                    </p>
               </div>
               <form
                    className="shadow p-3 bg-white rounded d-flex flex-column justify-content-center align-items-center"
                    style={{ width: "400px" }}
               >
                    <input
                         type="text"
                         placeholder="Email or phone number"
                         id="signin-username"
                         className="border border-2 rounded w-100 p-2 m-2 signin-input"
                         value={tenDangNhap}
                         onChange={(e) => setTenDangNhap(e.target.value)}
                    />
                    <input
                         type="password"
                         placeholder="Password"
                         id="signin-password"
                         className="border border-2 rounded w-100 p-2 m-2 signin-input"
                         value={matKhau}
                         onChange={(e) => setMatKhau(e.target.value)}
                    />
                    <button
                         className="border border-0 rounded w-100 p-2 text-white"
                         id="signin-signin"
                         onClick={handlesignin}
                    >
                         signin
                    </button>
                    <a src="#" id="signin-forget">
                         Forgotten password ?
                    </a>
                    <span className="line"></span>
                    <Link to="/signup" className="text-decoration-none">
                         <button
                              className="border border-0 rounded w-100 p-2 text-white"
                              id="signin-signup"
                         >
                              Create new account
                         </button>
                    </Link>
               </form>
          </div>
     );
}
export default Signin;
