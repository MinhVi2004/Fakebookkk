import React, { useState, useEffect } from "react";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { toast } from "react-toastify";
import { Link } from "react-router-dom";

import "./../CSS/Signup.css";
import { validateFormsignup } from "../Other/ValidateInput";
import { useNavigation } from "../Other/Navigation";
function Signup() {
     const { goToSignin } = useNavigation();
     const [hoTen, setHoTen] = useState(""); // Khai báo state cho username
     const [ngaySinh, setNgaySinh] = useState(null);
     const [gioiTinh, setGioiTinh] = useState(""); // Khai báo state cho password
     const [soDienThoai, setSoDienThoai] = useState(""); // Khai báo state cho password
     const [tenDangNhap, setTenDangNhap] = useState(""); // Khai báo state cho username
     const [matKhau, setMatKhau] = useState(""); // Khai báo state cho password
     const [xacNhanMatKhau, setXacNhanMatKhau] = useState(""); // Khai báo state cho password
     const handlesignup = async (e) => {
          e.preventDefault(); // Ngừng sự kiện mặc định (ngừng reload trang)
          //? Kiểm tra dữ liệu đầu vào
          const result = validateFormsignup(
               hoTen,
               ngaySinh,
               gioiTinh,
               soDienThoai,
               tenDangNhap,
               matKhau,
               xacNhanMatKhau
          );
          if (result !== null) {
               toast.error(result);
               return;
          }
          const formattedDate = new Date(ngaySinh);
          formattedDate.setDate(formattedDate.getDate() + 1); // Cộng thêm 1 ngày
          const formattedDateWithAddedDay = formattedDate
               .toISOString()
               .slice(0, 10);
          const response = await fetch(
               "http://localhost:8080/api/fakebook/signup",
               {
                    method: "POST",
                    headers: {
                         "Content-Type": "application/json",
                    },
                    body: JSON.stringify({
                         tenDangNhap: tenDangNhap,
                         matKhau: matKhau,
                         hoTen: hoTen,
                         gioiTinh: gioiTinh,
                         soDienThoai: soDienThoai,
                         ngaySinh: formattedDateWithAddedDay,
                    }), // Gửi dữ liệu đăng nhập dưới dạng JSON
               }
          );
          const data = await response.json();
          if (response.ok) {
               toast.success("Đăng ký thành công");
               goToSignin();
          } else {
               toast.error(data.message || "Đăng ký thất bại");
          }
     };
     return (
          <div className="sigin">
               <div className="sigin_name">
                    <h1>facebook</h1>
               </div>
               <form className="sigin_form">
                    <div className="sigin_create">
                         <h1 className="">Tạo tài khoản mới</h1>
                         <p>Nhanh chóng và dễ dàng</p>
                    </div>

                    <div className="sigin_input  text-start">
                         <div className="sigin_input">
                              <input
                                   type="text"
                                   name="fullname"
                                   placeholder="Họ tên"
                                   value={hoTen}
                                   onChange={(e) => setHoTen(e.target.value)}
                              />
                         </div>

                         <div className="sigin_input text-start">
                              <DatePicker
                                   selected={ngaySinh}
                                   onChange={(date) => setNgaySinh(date)}
                                   dateFormat="dd/MM/yyyy"
                                   showYearDropdown
                                   scrollableYearDropdown
                                   yearDropdownItemNumber={100}
                                   maxDate={new Date()}
                                   placeholderText="Chọn ngày sinh"
                                   className="date-picker"
                              />
                         </div>

                         <div className="sigin_input_info select_gender">
                              <div className="sigin_input_selectGender">
                                   <label>
                                        Nam
                                        <input
                                             type="radio"
                                             name="gender"
                                             value="Nam"
                                             checked={gioiTinh === "Nam"}
                                             onChange={(e) =>
                                                  setGioiTinh(e.target.value)
                                             }
                                        />
                                   </label>
                                   <label>
                                        Nữ
                                        <input
                                             type="radio"
                                             name="gender"
                                             value="Nữ"
                                             checked={gioiTinh === "Nữ"}
                                             onChange={(e) =>
                                                  setGioiTinh(e.target.value)
                                             }
                                        />
                                   </label>
                              </div>
                         </div>
                         <div>
                              <input
                                   type="text"
                                   name="phone"
                                   placeholder="Nhập số điện thoại"
                                   value={soDienThoai}
                                   onChange={(e) =>
                                        setSoDienThoai(e.target.value)
                                   }
                              />
                         </div>
                         <div>
                              <input
                                   type="text"
                                   name="username"
                                   placeholder="Nhập tên đăng nhập"
                                   value={tenDangNhap}
                                   onChange={(e) =>
                                        setTenDangNhap(e.target.value)
                                   }
                              />
                         </div>
                         <div>
                              <input
                                   type="password"
                                   name="password"
                                   placeholder="Nhập mật khẩu"
                                   value={matKhau}
                                   onChange={(e) => setMatKhau(e.target.value)}
                              />
                         </div>
                         <div>
                              <input
                                   type="password"
                                   name="repassword"
                                   placeholder="Xác nhận lại mật khẩu"
                                   value={xacNhanMatKhau}
                                   onChange={(e) =>
                                        setXacNhanMatKhau(e.target.value)
                                   }
                              />
                         </div>

                         <button
                              type="submit"
                              className="btn_create_new_account"
                              onClick={handlesignup}
                         >
                              Đăng ký
                         </button>

                         <p className="sigin_describe_dn_1">
                              Bạn đã có tài khoản?{" "}
                              <Link to="/signin" className="sigin_describe_dn_2">
                                   <button className="btn-link">
                                        Đăng nhập ngay
                                   </button>
                              </Link>
                         </p>
                    </div>
               </form>
          </div>
     );
}

export default Signup;
