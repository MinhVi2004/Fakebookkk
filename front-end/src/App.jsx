import React from "react";
import { BrowserRouter as Router, Routes, Route, Link, useLocation  } from "react-router-dom";
import { ToastContainer } from "react-toastify";
import { toast } from "react-toastify";
import HomePage from "./Page/HomePage";
import Signin from "./Page/Signin";
import Signup from "./Page/Signup";
import Profile from "./Page/Profile";
import TopBar from "./Page/TopBar";
import Admin from "./Page/Admin";
import UserManager from "./Page/UserManager";
import PostManager from "./Page/PostManager";

const AppContent = () => {
     const location = useLocation();
     const hideTopBar = location.pathname === "/signin" || location.pathname === "/signup"|| location.pathname === "/";
   
     return (
       <>
         {!hideTopBar && <TopBar />}
         <Routes>
          <Route path="/" element={<Signin />} />
          <Route path="/home" element={<HomePage />} />
          <Route path="/signin" element={<Signin />} />
          <Route path="/signup" element={<Signup />} />
          <Route path="/profile" element={<Profile />} />
          <Route path="/admin" element={<Admin />}> 
               <Route path="" element={<UserManager />} />
               <Route path="users" element={<UserManager />} />
               <Route path="posts" element={<PostManager />} />
          </Route>
         </Routes>
       </>
     );
   };


function App() {
     return (
          <Router>
            <AppContent />
            <ToastContainer position="top-center" autoClose={2000} />
          </Router>
        );
}

export default App;
