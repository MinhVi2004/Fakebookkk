import { useEffect, useState } from "react";
import axios from "axios";
import Post from "../Other/Post";
import PostForm from "../Other/PostForm";
import "../CSS/HomePage.css";
import { useNavigation } from "../Other/navigation"; // Import useNavigation từ file navigation.js
import { toast } from "react-toastify";

const Home = () => {
      
      const { goToSignin } = useNavigation();
  const [posts, setPosts] = useState([]);
  const [maTK, setMaTK] = useState("");
  // Lấy maTK từ sessionStorage
  useEffect(() => {
    const userData = sessionStorage.getItem("userSignin");
    if (userData) {
      try {
        const parsedUser = JSON.parse(userData);
        if (parsedUser.maTK) {
          setMaTK(parsedUser.maTK);
        } else {
          console.warn("Không tìm thấy mã tài khoản trong userSignin.");
          toast.error("Vui lòng đăng nhập để tiếp tục.");
          goToSignin();
        }
      } catch (e) {
        console.error("Lỗi khi parse userSignin:", e);
      }
    } else {
      console.warn("Chưa có userSignin trong sessionStorage.");
          toast.error("Vui lòng đăng nhập để tiếp tục.");
          goToSignin();
    }
  }, []);

  // Tách hàm fetch ra để dùng lại
  const fetchPosts = async () => {
    try {
      const res = await axios.get("http://localhost:8080/api/fakebook/posts");
      if (Array.isArray(res.data)) {
        setPosts(res.data.sort((a, b) => b.maBV - a.maBV));
      }
    } catch (err) {
      console.error("Lỗi khi lấy bài viết:", err);
    }
  };

  // Gọi khi component mount
  useEffect(() => {
    fetchPosts();
  }, []);

  // Khi tạo bài viết mới → gọi lại API
  const handlePostSubmit = () => {
    fetchPosts();
  };

  // Khi xoá bài viết → gọi lại API
  // Khi xóa bài viết → lọc ra khỏi state
  const handleDeletePost = () => {
    fetchPosts();
  };


  return (
    <div className="home-page">
      <div className="home-container">
        {maTK && (
          <PostForm maTK={maTK} onPostSubmit={handlePostSubmit} />
        )}
        <div className="posts-list">
          {posts.length > 0 ? (
            posts.map((post) => (
              <Post
                key={post.maBV}
                post={post}
                onDelete={handleDeletePost}
              />
            ))
          ) : (
            <p>Không có bài viết nào.</p>
          )}
        </div>
      </div>
    </div>
  );
};

export default Home;
