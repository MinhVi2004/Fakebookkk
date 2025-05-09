import React, { useState, useEffect, useRef } from "react";
import { Link, useNavigate } from "react-router-dom";
import "../CSS/TopBar.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faUser } from "@fortawesome/free-solid-svg-icons/faUser";
import confirm from "../Other/Confirm";
import SearchBar from "../Other/SearchBar";
import { useRequestContext } from "./RequestContext"; 

const TopBar = () => {
  const { requestCount } = useRequestContext(); // Access the request count from the context
  const [dropdownOpen, setDropdownOpen] = useState(false);
  const userData = JSON.parse(sessionStorage.getItem("userSignin") || "{}");
  const role = userData.phanQuyen;
  const profilePic = userData.profilePic;
  const username = userData.hoTen;
  const navigate = useNavigate();
  const dropdownRef = useRef(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(false);

  const handleClick = () => {
    navigate('/profile');
  };

  const handleLogout = async () => {
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
  }, [userData.maTK]);

  return (
    <div className="top-bar">
      <div className="logo">
        <Link to="/home">Fakebook</Link>
      </div>
      <SearchBar />
      <div className="nav-links">
        <Link to="/home" className="home-button category-button">Trang Chủ</Link>
        <Link to="/friends/all" className="category-button">Tất cả bạn bè</Link>
        <Link to="/friends/suggested" className="category-button">Gợi ý kết bạn</Link>
        <Link to="/friends/requests" className="category-button">
          Lời mời kết bạn
          {requestCount > 0 && <span className="badge">{requestCount}</span>}
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
          <img src={`../Resource/Avatar/${profilePic}`} alt="" className="topbar-avatar" />
          {username}
          {dropdownOpen && (
            <div className="dropdown-menu">
              <button className="profile-button" onClick={handleClick}>Trang cá nhân</button>
              <button onClick={handleLogout}>Đăng xuất</button>
            </div>
          )}
        </span>
      </div>
    </div>
  );
};

export default TopBar;
