import React, { useState, useEffect, useRef } from "react";
import { Link, useNavigate } from "react-router-dom";
import "../CSS/TopBar.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faUser } from "@fortawesome/free-solid-svg-icons/faUser";
import confirm from "../Other/Confirm";


const TopBar = () => {
     const [dropdownOpen, setDropdownOpen] = useState(false);
     const userData = JSON.parse(sessionStorage.getItem("userSignin") || "{}");
     const role = userData.phanQuyen;
     const username = userData.hoTen;
     const navigate = useNavigate();
     const dropdownRef = useRef(null);
     const handleLogout = async  () => {
          const confirmed = await confirm({
               title: "Đăng xuất?",
               text: "Bạn có chắc chắn muốn đăng xuất?",
             });
         
             if (confirmed) {
               sessionStorage.clear();
               navigate("/signin");
             }
     };
     useEffect(() => {
          const handleClickOutside = (event) => {
            if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
              setDropdownOpen(false);
            }
          };
          document.addEventListener("mousedown", handleClickOutside);
          return () => {
            document.removeEventListener("mousedown", handleClickOutside);
          };
        }, []);
     return (
          <div className="top-bar">
               <div className="logo">Fakebook</div>

               <div className="nav-links">
                    <Link to="/home" className="home-button category-button">
                         Trang Chủ
                    </Link>
                    {role === "Quản Trị Viên" && (
                         <Link to="/admin" className="admin-button category-button">
                              Quản lý
                         </Link>
                    )}

                    <span
                         ref={dropdownRef}
                         className="user-dropdown"
                         onClick={() => setDropdownOpen(!dropdownOpen)}
                    >
                         <FontAwesomeIcon icon={faUser} /> {username}
                         {dropdownOpen && (
                              <div className="dropdown-menu">
                                   <button><Link to="/profile">Trang cá nhân</Link></button>
                                   <button onClick={handleLogout}>
                                        Đăng xuất
                                   </button>
                              </div>
                         )}
                    </span>
               </div>
          </div>
     );
};

export default TopBar;
