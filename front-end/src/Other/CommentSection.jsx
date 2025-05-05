import { useState } from "react";
import axios from "axios";
import { toast } from "react-toastify";

const CommentSection = ({ comments, setComments, maBV }) => {
  const [text, setText] = useState("");
  const [showConfirm, setShowConfirm] = useState(false);
  const [commentToDelete, setCommentToDelete] = useState(null);

  const user = JSON.parse(sessionStorage.getItem("userSignin"));

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!text.trim()) return;

    const newComment = {
      noiDung: text,
      thoiGian: new Date().toLocaleString(),
      maBV: maBV,
      maTK: user.maTK,
      taiKhoanBVAndBL: {
        hoTen: user.hoTen,
        profilePic: user.profilePic || "Resoure/Avatar/default.jpg",
      },
    };

    setComments((prev) => [...prev, newComment]);
    setText("");
  };

  const handleDeleteClick = (cmt) => {
    setCommentToDelete(cmt);
    setShowConfirm(true);
  };

  const confirmDelete = async () => {
    if (!commentToDelete) return;

    try {
      await axios.post(
        `http://localhost:8080/api/fakebook/posts/comment/delete/${commentToDelete?.maBL}`
      );

      setComments((prev) =>
        prev.filter((c) => c.maBL !== commentToDelete.maBL)
      );
    } catch (error) {
      console.error("Lỗi khi xóa bình luận:", error);
    } finally {
      setShowConfirm(false);
      setCommentToDelete(null);
    }
  };

  return (
    <div className="comment-section mt-3">
      <ul className="list-unstyled">
        {comments.map((cmt, idx) => {
          const formattedTime = new Date(cmt.thoiGian).toLocaleString("vi-VN", {
            day: "2-digit",
            month: "2-digit",
            year: "numeric",
            hour: "2-digit",
            minute: "2-digit",
            second: "2-digit",
          });

          return (
            <li key={idx} className="d-flex align-items-start mb-3">
              <img
                src={`Resource/Avatar/${cmt.taiKhoanBVAndBL?.profilePic}`}
                alt="avatar"
                className="rounded-circle me-2"
                style={{ width: "40px", height: "40px", objectFit: "cover" }}
              />
              <div className="flex-grow-1">
                <div className="d-flex align-items-center gap-2">
                  <strong>{cmt.taiKhoanBVAndBL?.hoTen}</strong>
                  <span className="text-muted" style={{ fontSize: "12px" }}>
                    {formattedTime}
                  </span>
                </div>
                <div>{cmt.noiDung}</div>
              </div>

              {user?.maTK === cmt.maTK && (
                <button
                  className="btn btn-sm btn-danger ms-2"
                  onClick={() => handleDeleteClick(cmt)}
                >
                  Xoá
                </button>
              )}
            </li>
          );
        })}
      </ul>

      <form
        onSubmit={async (e) => {
          e.preventDefault();

          const user = JSON.parse(sessionStorage.getItem("userSignin"));
          if (!user) {
            toast.info("Vui lòng đăng nhập để bình luận.");
            return;
          }

          if (!text.trim()) return;

          try {
            const response = await axios.post(
              "http://localhost:8080/api/fakebook/posts/comment",
              {
                maTK: user?.maTK,
                maBV: maBV,
                noiDung: text,
              }
            );

            const newComment = {
              ...response.data, // Nếu backend trả về bình luận đã lưu
              taiKhoanBVAndBL: {
                hoTen: user.hoTen,
                profilePic: user.profilePic || "default.jpg",
              },
            };

            setComments((prev) => [...prev, newComment]);
            setText("");
          } catch (err) {
            console.error("Lỗi khi gửi bình luận:", err);
            alert("Không thể gửi bình luận.");
          }
        }}
        className="d-flex gap-2 mt-2"
      >
        <input
          type="text"
          className="form-control"
          placeholder="Viết bình luận..."
          value={text}
          onChange={(e) => setText(e.target.value)}
        />
        <button
          type="submit"
          className="btn btn-primary"
          disabled={!text.trim()}
        >
          Gửi
        </button>
      </form>

      {showConfirm && (
        <div
          className="modal d-block"
          tabIndex="-1"
          onClick={() => setShowConfirm(false)}
        >
          <div
            className="modal-dialog modal-dialog-centered"
            onClick={(e) => e.stopPropagation()}
          >
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title">Xác nhận xoá bình luận</h5>
              </div>
              <div className="modal-body">
                <p>Bạn có chắc muốn xoá bình luận này không?</p>
              </div>
              <div className="modal-footer">
                <button
                  className="btn btn-secondary"
                  onClick={() => setShowConfirm(false)}
                >
                  Huỷ
                </button>
                <button className="btn btn-danger" onClick={confirmDelete}>
                  Xoá
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default CommentSection;
