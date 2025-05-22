-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th5 20, 2025 lúc 06:37 PM
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
  `ThoiGian` timestamp NOT NULL DEFAULT current_timestamp(),
  `TrangThai` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `baiviet`
--

INSERT INTO `baiviet` (`MaBV`, `MaTK`, `LoaiChiaSe`, `NoiDung`, `ThoiGian`, `TrangThai`) VALUES
(51, 9, 'Tất Cả', 'Mưa', '2025-05-04 04:32:30', 'Đã Xóa'),
(53, 17, 'Tất Cả', 'Okie', '2025-05-04 04:34:13', 'Bình Thường'),
(66, 9, 'Tất Cả', 'hello ', '2025-05-06 16:26:47', 'Đã Xóa'),
(67, 26, 'Tất Cả', 'Minh Vi 3 đăng ảnh mới nè !', '2025-05-07 17:30:41', 'Bình Thường'),
(68, 24, 'Tất Cả', 'Đã tham gia !', '2025-05-08 03:38:20', 'Bình Thường'),
(69, 9, 'Tất Cả', 'Hello Mai báo cáo rồi', '2025-05-14 18:32:57', 'Đã Xóa'),
(70, 9, 'Bạn Bè', 'Mai báo cáo rồi sợ quá', '2025-05-14 18:33:17', 'Đã Xóa'),
(71, 9, 'Tất Cả', 'hello 123', '2025-05-14 18:40:39', 'Đã Xóa'),
(72, 9, 'Tất Cả', 'Để xem nào', '2025-05-14 18:41:05', 'Đã Xóa'),
(73, 9, 'Tất Cả', 'alo alo 123 ', '2025-05-14 18:42:23', 'Đã Xóa'),
(74, 9, 'Tất Cả', 'Test lần cúi nè', '2025-05-14 18:44:39', 'Đã Xóa'),
(75, 9, 'Tất Cả', 'abc', '2025-05-14 18:51:33', 'Đã Xóa'),
(76, 9, 'Tất Cả', 'aaaa', '2025-05-14 18:57:56', 'Đã Xóa'),
(77, 9, 'Tất Cả', 'aaaaa', '2025-05-14 18:59:47', 'Đã Xóa'),
(78, 9, 'Tất Cả', 'aaaaa', '2025-05-14 19:02:10', 'Đã Xóa'),
(79, 27, 'Chỉ Mình Tôi', 'alo mình tôi', '2025-05-15 00:47:00', 'Đã Xóa'),
(80, 27, 'Tất Cả', 'alo 1', '2025-05-15 01:25:46', 'Đã Xóa'),
(81, 9, 'Tất Cả', 'Alo', '2025-05-15 01:30:54', 'Đã Xóa'),
(82, 9, 'Tất Cả', 'Alo 1', '2025-05-15 01:33:08', 'Đã Xóa'),
(83, 9, 'Tất Cả', 'aaaaa', '2025-05-15 01:34:44', 'Đã Xóa'),
(84, 9, 'Tất Cả', 'aaaaa', '2025-05-15 01:36:02', 'Đã Xóa'),
(85, 9, 'Tất Cả', 'xin chao', '2025-05-15 01:38:37', 'Đã Xóa'),
(86, 9, 'Tất Cả', '123 ', '2025-05-15 01:41:42', 'Đã Xóa'),
(87, 9, 'Tất Cả', '12344556', '2025-05-15 01:41:53', 'Đã Xóa'),
(88, 27, 'Chỉ Mình Tôi', 'Bán sản phẩm', '2025-05-15 01:47:44', 'Bình Thường'),
(89, 9, 'Chỉ Mình Tôi', 'Project năm 2', '2025-05-15 01:51:34', 'Bình Thường'),
(90, 9, 'Chỉ Mình Tôi', 'Bài viết mình tôi hihi', '2025-05-15 02:07:19', 'Bình Thường');

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
(19, 53, '1746333253460_13a2e536-517c-46b1-ac85-68308b241454.png', 'image'),
(25, 66, '1746548807779_bcb21b57-91f1-4369-87aa-056ed72fe601.png', 'image'),
(26, 67, '1746639041814_dac96cbf-67bf-492c-a934-ca4f4c874277.jpg', 'image'),
(27, 68, '1746675500499_c207dfd3-df12-4eaf-a405-475b7feddac3.jpg', 'image'),
(28, 69, '1747247577219_f2c8fbeb-aa7f-429a-85e2-63685adc26f0.png', 'image'),
(29, 70, '1747247597780_3bd596a1-b793-4f69-919c-ee050d05cfc5.png', 'image'),
(30, 71, '1747248039946_2f08cfc9-a4bf-47a7-8254-0df67baca9b8.png', 'image'),
(31, 72, '1747248065432_b129ce3f-2e87-498a-ab90-b55f9fddcf3d.png', 'image'),
(32, 73, '1747248143881_54aa5015-5e68-4f97-b7aa-3f7d0abc8fbd.jpg', 'image'),
(33, 74, '1747248279869_99f6a8a3-4ae5-4ccd-837c-6be3b363fcd0.webp', 'image'),
(34, 75, '1747248693692_765054f9-07d5-4bea-9baf-c7cd578d67a3.png', 'image'),
(35, 76, '1747249076178_9406f43d-df12-4c4e-b31a-3023a31f5da0.png', 'image'),
(36, 77, '1747249187945_97dc01f2-7990-472f-a37a-4eb17f1cf09a.png', 'image'),
(37, 78, '1747249330937_b2cbe0f0-212a-46f1-95dd-3995bdc7975a.png', 'image'),
(38, 79, '1747270020209_a8f13ef0-7c11-4208-9a1a-b7ae94fe57cc.jpg', 'image'),
(39, 80, '1747272346195_fb34fa61-9a3f-45f3-99e1-d15f5ed2253d.png', 'image'),
(40, 81, '1747272654246_6173c93f-58cb-45e6-84b3-ea9f8546bade.png', 'image'),
(41, 82, '1747272788813_04a0ed11-72e8-4bb0-9df9-d1a27d076732.png', 'image'),
(42, 83, '1747272884143_0f24ba36-4d2c-4ac9-b7b6-b849e0f9111c.png', 'image'),
(43, 84, '1747272962211_991b2ba3-2bc7-455f-a501-a6b87bed77a9.png', 'image'),
(44, 85, '1747273117351_375b8930-e5ce-4b34-a0cc-9c4b807a0b33.png', 'image'),
(45, 86, '1747273302623_60370616-57b2-400b-979a-b1974c74548b.png', 'image'),
(46, 87, '1747273313395_aebc5cd3-ae00-41ed-b279-ed3aa67eb608.jpg', 'image'),
(47, 88, '1747273664064_255a8fc9-5901-4d16-a7bf-3d2002a55c4c.jpg', 'image'),
(48, 89, '1747273894776_87ef55ed-a52f-41f0-b541-8f3235819572.png', 'image'),
(49, 90, '1747274839649_1b218351-1193-4c1f-82dc-0828432b8532.webp', 'image');

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

--
-- Đang đổ dữ liệu cho bảng `banbe`
--

INSERT INTO `banbe` (`MaBB`, `MaTK_1`, `MaTK_2`, `TrangThaiBB`, `NgayTao`) VALUES
(22, 24, 17, 'Đã Đồng Ý', '2025-05-08 02:42:12'),
(26, 24, 26, 'Đã Đồng Ý', '2025-05-08 03:40:31'),
(28, 26, 23, 'Đã Đồng Ý', '2025-05-09 10:40:17'),
(31, 17, 23, 'Đã Đồng Ý', '2025-05-09 11:12:35'),
(32, 17, 25, 'Đã Đồng Ý', '2025-05-09 11:12:36'),
(33, 23, 24, 'Đã Đồng Ý', '2025-05-09 11:14:11'),
(34, 24, 25, 'Đã Đồng Ý', '2025-05-09 11:51:52'),
(35, 23, 25, 'Đã Đồng Ý', '2025-05-09 11:52:09'),
(37, 25, 26, 'Đã Đồng Ý', '2025-05-09 12:20:12'),
(41, 27, 23, 'Đã Đồng Ý', '2025-05-15 16:50:38'),
(42, 9, 27, 'Đã Đồng Ý', '2025-05-15 17:26:43');

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
  `MaTK` int(11) NOT NULL,
  `MaBV` int(11) NOT NULL,
  `ThoiGian` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `luotthich`
--

INSERT INTO `luotthich` (`MaTK`, `MaBV`, `ThoiGian`) VALUES
(9, 51, '2025-05-04 04:33:11'),
(17, 51, '2025-05-04 04:33:45'),
(17, 53, '2025-05-04 04:34:16');

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
(9, 'minhvi2', 'minhvi', '13.jpg', '13.jpg', 'Minh Vi ', 'Nam', '0912345677', 'dvmv2017@gmail.com', '2025-04-03', '2025-03-23 23:32:23', 'Bình Thường', 'Quản Trị Viên'),
(17, 'minhvi123', 'minhvi', 'default.png', 'default.png', 'Dương Văn Minh Vi 123', 'Nam', '0772912452', 'dvmv2021@gmail.com', '2004-11-21', '2025-04-05 15:16:27', 'Bình Thường', 'Người Dùng'),
(23, 'minhvi41', 'minhvi', 'default.png', 'default.png', 'Test12123123', 'Nam', '0772912450', 'vi.duongvanminh@gmail.com', '2004-11-22', '2025-04-05 17:03:47', 'Bình Thường', 'Người Dùng'),
(24, 'minhvi1', 'minhvi', 'default.png', 'default.png', 'Minh Vi 1', 'Nam', '0772912459', 'dvmv2025@gmail.com', '2004-04-27', '2025-05-06 21:49:49', 'Bình Thường', 'Người Dùng'),
(25, 'trianh123', 'trianh', 'default.png', 'default.png', 'Tri Anh', 'Nam', '0951287463', 'trianh@gmail.com', '2003-05-07', '2025-05-06 22:43:48', 'Bình Thường', 'Người Dùng'),
(26, 'minhvi3', '123456', '50.jpg', '42.jpg', 'Minh Vi 3', 'Nam', '0772912455', 'dvmv2026@gmail.com', '2004-05-04', '2025-05-08 00:29:57', 'Bình Thường', 'Người Dùng'),
(27, 'minhvi123p', '123456', 'GiaoDien01.png', 'GiaoDien.png', 'Minh Vi', 'Nam', '0772912456', 'dvmv2031@gmail.com', '2005-05-01', '2025-05-15 01:11:01', 'Bình Thường', 'Người Dùng'),
(28, 'minhvi123q', '123456', 'default.png', 'default.png', 'Dương Văn Minh Vi 2', 'Nữ', '0772912453', 'dvmv2041@gmail.com', '2005-05-01', '2025-05-15 08:53:22', 'Bình Thường', 'Người Dùng');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `tinnhan`
--

CREATE TABLE `tinnhan` (
  `MaTinNhan` int(11) NOT NULL,
  `NguoiGui` int(11) NOT NULL,
  `NguoiNhan` int(11) NOT NULL,
  `NoiDung` text NOT NULL,
  `ThoiGianGui` datetime DEFAULT current_timestamp(),
  `DaDoc` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `tinnhan`
--

INSERT INTO `tinnhan` (`MaTinNhan`, `NguoiGui`, `NguoiNhan`, `NoiDung`, `ThoiGianGui`, `DaDoc`) VALUES
(1, 23, 27, 'alo', '2025-05-15 23:55:55', 0),
(2, 23, 27, 'aaaaa', '2025-05-15 23:56:04', 0),
(3, 27, 23, 'chào bạn', '2025-05-16 00:10:44', 0),
(4, 27, 23, 'tôi là minh vi', '2025-05-16 00:12:43', 0),
(5, 23, 27, 'tôi là test', '2025-05-16 00:13:30', 0),
(6, 27, 23, 'tôi là minh vi 1', '2025-05-16 00:13:49', 0),
(7, 27, 23, 'test thử nha', '2025-05-16 00:14:46', 0),
(8, 27, 23, 'oke chưa', '2025-05-16 00:15:54', 0),
(9, 27, 9, 'xin chào', '2025-05-16 00:27:10', 0),
(10, 9, 27, 'alo', '2025-05-16 00:27:52', 0),
(11, 9, 27, 'rep đi', '2025-05-16 00:30:15', 0),
(12, 27, 9, 'xin chào', '2025-05-16 00:49:55', 0),
(13, 27, 9, 'ủa gì z', '2025-05-16 00:50:31', 0),
(14, 27, 9, 'là sao', '2025-05-16 00:50:46', 0),
(15, 27, 9, 'chào admin', '2025-05-16 00:57:41', 0),
(16, 27, 9, 'chào admin lần 2', '2025-05-16 01:02:18', 0),
(17, 27, 9, 'alo', '2025-05-16 01:04:34', 0),
(18, 27, 9, 'test thử nha', '2025-05-16 01:05:14', 0),
(19, 9, 27, 'nhắn thử', '2025-05-16 01:06:54', 0),
(20, 27, 9, 'alo', '2025-05-16 01:08:41', 0),
(21, 9, 27, 'alo 2', '2025-05-16 01:09:59', 0),
(22, 27, 9, 'xin chào', '2025-05-16 01:10:06', 0),
(23, 27, 9, 'aloaloa', '2025-05-16 01:11:48', 0),
(24, 23, 17, 'xin chào', '2025-05-16 01:12:37', 0),
(25, 23, 17, 'giờ sao', '2025-05-16 01:12:54', 0),
(26, 23, 26, 'chào bạn', '2025-05-16 01:21:41', 0),
(27, 23, 26, 'mình test websocket thử', '2025-05-16 01:22:57', 0),
(28, 26, 23, 'oke kh bạn', '2025-05-16 01:23:12', 0),
(29, 26, 23, 'được chưa', '2025-05-16 01:37:41', 0),
(30, 23, 26, 'được rồi', '2025-05-16 01:37:47', 0),
(31, 23, 26, 'xin chào', '2025-05-16 01:38:10', 0),
(32, 23, 26, '2 tin nhắn chưa xem', '2025-05-16 01:38:20', 0);

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
  ADD KEY `MaBV` (`MaBV`),
  ADD KEY `MaTK_2` (`MaTK`),
  ADD KEY `MaBV_2` (`MaBV`);

--
-- Chỉ mục cho bảng `luotthich`
--
ALTER TABLE `luotthich`
  ADD PRIMARY KEY (`MaTK`,`MaBV`),
  ADD UNIQUE KEY `MaTK` (`MaTK`,`MaBV`),
  ADD KEY `MaBV` (`MaBV`),
  ADD KEY `MaTK_2` (`MaTK`),
  ADD KEY `MaTK_3` (`MaTK`,`MaBV`);

--
-- Chỉ mục cho bảng `taikhoan`
--
ALTER TABLE `taikhoan`
  ADD PRIMARY KEY (`MaTK`),
  ADD KEY `TenDangNhap` (`TenDangNhap`),
  ADD KEY `SoDienThoai` (`SoDienThoai`);

--
-- Chỉ mục cho bảng `tinnhan`
--
ALTER TABLE `tinnhan`
  ADD PRIMARY KEY (`MaTinNhan`),
  ADD KEY `NguoiGui` (`NguoiGui`),
  ADD KEY `NguoiNhan` (`NguoiNhan`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `baiviet`
--
ALTER TABLE `baiviet`
  MODIFY `MaBV` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=91;

--
-- AUTO_INCREMENT cho bảng `baiviet_dinhkem`
--
ALTER TABLE `baiviet_dinhkem`
  MODIFY `MaBV_DK` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=50;

--
-- AUTO_INCREMENT cho bảng `banbe`
--
ALTER TABLE `banbe`
  MODIFY `MaBB` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=43;

--
-- AUTO_INCREMENT cho bảng `binhluan`
--
ALTER TABLE `binhluan`
  MODIFY `MaBL` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT cho bảng `taikhoan`
--
ALTER TABLE `taikhoan`
  MODIFY `MaTK` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- AUTO_INCREMENT cho bảng `tinnhan`
--
ALTER TABLE `tinnhan`
  MODIFY `MaTinNhan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

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

--
-- Các ràng buộc cho bảng `tinnhan`
--
ALTER TABLE `tinnhan`
  ADD CONSTRAINT `tinnhan_ibfk_1` FOREIGN KEY (`NguoiGui`) REFERENCES `taikhoan` (`MaTK`) ON DELETE CASCADE,
  ADD CONSTRAINT `tinnhan_ibfk_2` FOREIGN KEY (`NguoiNhan`) REFERENCES `taikhoan` (`MaTK`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
