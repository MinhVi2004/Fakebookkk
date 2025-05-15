import React, { useState, useEffect } from "react";
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Link,
  useLocation,
} from "react-router-dom";
import { ToastContainer } from "react-toastify";
import { toast } from "react-toastify";
import { RequestProvider } from "./Page/RequestContext"; // Import the provider
import Signin from "./Page/Signin";
import Signup from "./Page/Signup";
import Profile from "./Page/Profile";
import TopBar from "./Page/TopBar";
import Admin from "./Page/Admin";
import UserManager from "./Page/UserManager";
import PostManager from "./Page/PostManager";
import Home from "./Page/Home";
import AllFriends from "./Page/AllFriends";
import FriendRequests from "./Page/FriendRequests";
import FriendSuggestions from "./Page/FriendSuggestions";
import ChatSidebar from "./Page/ChatSideBar";
import ChatWindow from "./Page/ChatWindow"; // đổi lại đúng tên file

import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";
const AppContent = () => {

      const userSignin = JSON.parse(sessionStorage.getItem("userSignin"));
      const [selectedFriend, setSelectedFriend] = useState(null);
      const [unreadCounts, setUnreadCounts] = useState({});
      const [stompClient, setStompClient] = useState(null);

      useEffect(() => {
           const socket = new SockJS("http://localhost:8080/ws");
           const client = new Client({
                webSocketFactory: () => socket,
                onConnect: () => {
                     console.log("Connected");

                     client.subscribe("/topic/messages", (message) => {
                          const msg = JSON.parse(message.body);
                          if (msg.nguoiNhanId === userSignin.maTK) {
                               if (
                                    selectedFriend?.friendId !== msg.nguoiGuiId
                               ) {
                                    // Nếu không đang mở với người gửi => tăng unread
                                    setUnreadCounts((prev) => ({
                                         ...prev,
                                         [msg.nguoiGuiId]:
                                              (prev[msg.nguoiGuiId] || 0) + 1,
                                    }));
                               }
                          }
                     });
                },
           });

           client.activate();
           setStompClient(client);

           return () => {
                client.deactivate();
           };
      }, [selectedFriend]);

      const handleSelectFriend = (friend) => {
           setSelectedFriend(friend);
           setUnreadCounts((prev) => ({
                ...prev,
                [friend.friendId]: 0,
           }));
      };


  const location = useLocation();
  const hideLayout =
    location.pathname === "/signin" ||
    location.pathname === "/signup" ||
    location.pathname === "/";


  return hideLayout ? (
    <Routes>
      <Route path="/" element={<Signin />} />
      <Route path="/signin" element={<Signin />} />
      <Route path="/signup" element={<Signup />} />
    </Routes>
  ) : (
    <div className="flex h-screen">
      <ChatSidebar
        onSelectFriend={handleSelectFriend}
        unreadCounts={unreadCounts}
      />

      <div className="flex flex-col flex-grow">
        <TopBar />

        <div className="flex flex-grow overflow-hidden">
          <div className="flex-grow overflow-auto">
            <Routes>
              <Route path="/home" element={<Home />} />
              <Route path="/profile" element={<Profile />} />
              <Route path="/friends/all" element={<AllFriends />} />
              <Route path="/friends/suggested" element={<FriendSuggestions />} />
              <Route path="/friends/requests" element={<FriendRequests />} />
              <Route path="/admin" element={<Admin />}>
                <Route path="" element={<UserManager />} />
                <Route path="users" element={<UserManager />} />
                <Route path="posts" element={<PostManager />} />
              </Route>
            </Routes>
          </div>

          {/* Chat Window */}
          {selectedFriend && (
            <div className="w-[400px] border-l border-gray-300">
              <ChatWindow
                  friend={selectedFriend}
                  onClose={() => setSelectedFriend(null)}
                  stompClient={stompClient}
                  />
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

function App() {
  return (
    <RequestProvider> {/* Wrap the app content with RequestProvider */}
      <Router> {/* Wrap your entire app with Router */}
        <ToastContainer position="top-center" /> {/* Đây là nơi để toast hiện */}
        <AppContent />
      </Router>
    </RequestProvider>
  );
}

export default App; 