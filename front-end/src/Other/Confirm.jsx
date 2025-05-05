// utils/confirm.js
import Swal from "sweetalert2";
import withReactContent from "sweetalert2-react-content";
import "../CSS/Confirm.css"; // Thêm file CSS tùy chỉnh

const MySwal = withReactContent(Swal);

const confirm = async ({
  title = "Xác nhận?",
  text = "Bạn có chắc chắn muốn thực hiện hành động này?",
  confirmButtonText = "Đồng ý",
  cancelButtonText = "Hủy",
}) => {
  const result = await MySwal.fire({
    title,
    text,
    icon: "warning",
    showCancelButton: true,
    confirmButtonText,
    cancelButtonText,
    reverseButtons: true,
    customClass: {
      popup: "custom-swal", // Gắn class tùy chỉnh
    },
  });

  return result.isConfirmed;
};

export default confirm;
