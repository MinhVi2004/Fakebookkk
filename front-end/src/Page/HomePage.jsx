import { useState } from "react";
import { Link } from "react-router-dom";
import CommentSection from "../Other/CommentSection";
import Post from "../Other/Post";
import PostForm from "../Other/PostForm";
import postsData from "../../data/dummyPost";
import "../CSS/HomePage.css";

const HomePage = () => {
  const [posts, setPosts] = useState(postsData);

  const handlePostSubmit = (newPost) => {
    setPosts([newPost, ...posts]);
  };

  const handleDeletePost = (id) => {
    setPosts(posts.filter((post) => post.id !== id));
  };

  const handleUpdatePost = (updatedPost) => {
    setPosts(posts.map((post) => (post.id === updatedPost.id ? updatedPost : post)));
  };

  return (
    <div className="home-page">
      <div className="home-container">
        <PostForm onPostSubmit={handlePostSubmit} />
        <div className="posts-list">
          {posts.map((post) => (
            <Post
              key={post.id}
              post={post}
              onDelete={handleDeletePost}
              onUpdate={handleUpdatePost}
            />
          ))}
        </div>
      </div>
    </div>
  );
};

export default HomePage;