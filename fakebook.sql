-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th4 22, 2025 lúc 06:41 PM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `fakebook`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `baiviet`
--

CREATE TABLE `baiviet` (
  `MaBV` int(11) NOT NULL,
  `MaTK` int(11) NOT NULL,
  `LoaiChiaSe` enum('Tất Cả','Bạn Bè','Chỉ Mình Tôi') NOT NULL,
  `NoiDung` text DEFAULT NULL,
  `ThoiGian` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `baiviet`
--

INSERT INTO `baiviet` (`MaBV`, `MaTK`, `LoaiChiaSe`, `NoiDung`, `ThoiGian`) VALUES
(1, 9, 'Tất Cả', 'Bài viết 001', '2025-04-22 09:57:07');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `baiviet_dinhkem`
--

CREATE TABLE `baiviet_dinhkem` (
  `MaBV_DK` int(11) NOT NULL,
  `MaBV` int(11) NOT NULL,
  `LinkDK` varchar(255) NOT NULL,
  `LoaiDK` enum('image','video') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `baiviet_dinhkem`
--

INSERT INTO `baiviet_dinhkem` (`MaBV_DK`, `MaBV`, `LinkDK`, `LoaiDK`) VALUES
(1, 1, 'https://res.cloudinary.com/dzom7z5wm/image/upload/v1741857632/GiftShopSanPham/sanpham_SP011.jpg', 'image');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `banbe`
--

CREATE TABLE `banbe` (
  `MaBB` int(11) NOT NULL,
  `MaTK_1` int(11) NOT NULL,
  `MaTK_2` int(11) NOT NULL,
  `TrangThaiBB` enum('Chờ Chấp Nhận','Đã Đồng Ý','Không Đồng Ý','Đã Xóa') DEFAULT 'Chờ Chấp Nhận',
  `NgayTao` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `binhluan`
--

CREATE TABLE `binhluan` (
  `MaBL` int(11) NOT NULL,
  `MaTK` int(11) NOT NULL,
  `MaBV` int(11) NOT NULL,
  `NoiDung` text NOT NULL,
  `ThoiGian` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `luotthich`
--

CREATE TABLE `luotthich` (
  `MaLT` int(11) NOT NULL,
  `MaTK` int(11) NOT NULL,
  `MaBV` int(11) NOT NULL,
  `ThoiGian` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `taikhoan`
--

CREATE TABLE `taikhoan` (
  `MaTK` int(11) NOT NULL,
  `TenDangNhap` varchar(255) NOT NULL,
  `MatKhau` varchar(255) NOT NULL,
  `ProfilePic` varchar(255) DEFAULT NULL,
  `CoverPic` varchar(255) DEFAULT NULL,
  `HoTen` varchar(255) NOT NULL,
  `GioiTinh` varchar(255) NOT NULL,
  `SoDienThoai` varchar(255) DEFAULT NULL,
  `Email` varchar(255) NOT NULL,
  `NgaySinh` date NOT NULL,
  `NgayTao` datetime NOT NULL,
  `TrangThai` enum('Bình Thường','Vô Hiệu Hóa') DEFAULT NULL,
  `PhanQuyen` enum('Người Dùng','Quản Trị Viên') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `taikhoan`
--

INSERT INTO `taikhoan` (`MaTK`, `TenDangNhap`, `MatKhau`, `ProfilePic`, `CoverPic`, `HoTen`, `GioiTinh`, `SoDienThoai`, `Email`, `NgaySinh`, `NgayTao`, `TrangThai`, `PhanQuyen`) VALUES
(9, 'minhvi2', 'minhvi', '13.jpg', '13.jpg', 'Minh Vi 2', 'Nam', '0912345677', 'dvmv2017@gmail.com', '2025-04-03', '2025-03-23 23:32:23', 'Bình Thường', 'Quản Trị Viên'),
(17, 'minhvi123', 'minhvi', 'default.png', 'default.png', 'Dương Văn Minh Vi 123', 'Nam', '0772912452', 'dvmv2021@gmail.com', '2004-11-21', '2025-04-05 15:16:27', 'Bình Thường', 'Người Dùng'),
(23, 'minhvi41', 'minhvi', 'default.png', 'default.png', 'Test12123123', 'Nam', '0772912450', 'vi.duongvanminh@gmail.com', '2004-11-22', '2025-04-05 17:03:47', 'Bình Thường', 'Người Dùng');

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `baiviet`
--
ALTER TABLE `baiviet`
  ADD PRIMARY KEY (`MaBV`),
  ADD KEY `MaTK` (`MaTK`);

--
-- Chỉ mục cho bảng `baiviet_dinhkem`
--
ALTER TABLE `baiviet_dinhkem`
  ADD PRIMARY KEY (`MaBV_DK`),
  ADD KEY `MaBV` (`MaBV`);

--
-- Chỉ mục cho bảng `banbe`
--
ALTER TABLE `banbe`
  ADD PRIMARY KEY (`MaBB`),
  ADD UNIQUE KEY `MaTK_1` (`MaTK_1`,`MaTK_2`),
  ADD KEY `MaTK_2` (`MaTK_2`);

--
-- Chỉ mục cho bảng `binhluan`
--
ALTER TABLE `binhluan`
  ADD PRIMARY KEY (`MaBL`),
  ADD KEY `MaTK` (`MaTK`),
  ADD KEY `MaBV` (`MaBV`);

--
-- Chỉ mục cho bảng `luotthich`
--
ALTER TABLE `luotthich`
  ADD PRIMARY KEY (`MaLT`),
  ADD UNIQUE KEY `MaTK` (`MaTK`,`MaBV`),
  ADD KEY `MaBV` (`MaBV`);

--
-- Chỉ mục cho bảng `taikhoan`
--
ALTER TABLE `taikhoan`
  ADD PRIMARY KEY (`MaTK`),
  ADD KEY `TenDangNhap` (`TenDangNhap`),
  ADD KEY `SoDienThoai` (`SoDienThoai`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `baiviet`
--
ALTER TABLE `baiviet`
  MODIFY `MaBV` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT cho bảng `baiviet_dinhkem`
--
ALTER TABLE `baiviet_dinhkem`
  MODIFY `MaBV_DK` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT cho bảng `banbe`
--
ALTER TABLE `banbe`
  MODIFY `MaBB` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `binhluan`
--
ALTER TABLE `binhluan`
  MODIFY `MaBL` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `luotthich`
--
ALTER TABLE `luotthich`
  MODIFY `MaLT` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `taikhoan`
--
ALTER TABLE `taikhoan`
  MODIFY `MaTK` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `baiviet`
--
ALTER TABLE `baiviet`
  ADD CONSTRAINT `baiviet_ibfk_1` FOREIGN KEY (`MaTK`) REFERENCES `taikhoan` (`MaTK`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `baiviet_dinhkem`
--
ALTER TABLE `baiviet_dinhkem`
  ADD CONSTRAINT `baiviet_dinhkem_ibfk_1` FOREIGN KEY (`MaBV`) REFERENCES `baiviet` (`MaBV`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `banbe`
--
ALTER TABLE `banbe`
  ADD CONSTRAINT `banbe_ibfk_1` FOREIGN KEY (`MaTK_1`) REFERENCES `taikhoan` (`MaTK`) ON DELETE CASCADE,
  ADD CONSTRAINT `banbe_ibfk_2` FOREIGN KEY (`MaTK_2`) REFERENCES `taikhoan` (`MaTK`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `binhluan`
--
ALTER TABLE `binhluan`
  ADD CONSTRAINT `binhluan_ibfk_1` FOREIGN KEY (`MaTK`) REFERENCES `taikhoan` (`MaTK`) ON DELETE CASCADE,
  ADD CONSTRAINT `binhluan_ibfk_2` FOREIGN KEY (`MaBV`) REFERENCES `baiviet` (`MaBV`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `luotthich`
--
ALTER TABLE `luotthich`
  ADD CONSTRAINT `luotthich_ibfk_1` FOREIGN KEY (`MaTK`) REFERENCES `taikhoan` (`MaTK`) ON DELETE CASCADE,
  ADD CONSTRAINT `luotthich_ibfk_2` FOREIGN KEY (`MaBV`) REFERENCES `baiviet` (`MaBV`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
