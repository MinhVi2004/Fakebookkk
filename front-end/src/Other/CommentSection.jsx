import React, { useState } from "react";
import "../CSS/CommentSection.css";

const CommentSection = ({ comments = [], onAddComment }) => {
  const [newComment, setNewComment] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();
    if (newComment.trim()) {
      onAddComment(newComment.trim());
      setNewComment("");
    }
  };

  return (
    <div className="comment-section">
      <h4>Bình luận</h4>

      <ul className="comment-list">
        {comments.map((comment, index) => (
          <li key={index} className="comment-item">
            <strong>{comment.user}:</strong> {comment.text}
          </li>
        ))}
      </ul>

      <form onSubmit={handleSubmit} className="comment-form">
        <input
          type="text"
          placeholder="Viết bình luận..."
          value={newComment}
          onChange={(e) => setNewComment(e.target.value)}
        />
        <button type="submit">Gửi</button>
      </form>
    </div>
  );
};

export default CommentSection;