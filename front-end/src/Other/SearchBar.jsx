import React, { useState, useEffect } from "react";
import axios from "axios";
import "../CSS/SearchBar.css";
import { Link } from "react-router-dom";

function SearchBar() {
  const [searchKeyWord, setSearchKeyWord] = useState("");
  const [suggestedFriends, setSuggestedFriends] = useState([]);

  useEffect(() => {
    const fetchSuggestions = async () => {
      if (!searchKeyWord.trim()) {
        setSuggestedFriends([]);
        return;
      }

      try {
        const response = await axios.get("http://localhost:8080/api/fakebook/searchUser", {
          params: { keyWord: searchKeyWord }
        });
        setSuggestedFriends(response.data);
      } catch (error) {
        console.error("Lỗi khi tìm kiếm:", error);
        setSuggestedFriends([]);
      }
    };

    const delayDebounce = setTimeout(fetchSuggestions, 300); // debounce 300ms
    return () => clearTimeout(delayDebounce);
  }, [searchKeyWord]);

//   const handleSendRequest = (friendId) => {
//     // Gửi yêu cầu kết bạn (tuỳ logic bạn có)
//     console.log("Gửi yêu cầu kết bạn tới:", friendId);
//   };

  return (
    <div className="search-bar">
      <input
        type="text"
        className="search-input"
        placeholder="Tìm kiếm bạn bè"
        value={searchKeyWord}
        onChange={(e) => setSearchKeyWord(e.target.value)}
      />
      {searchKeyWord && (
        <div className="search-dropdown-extended">
          {suggestedFriends.length === 0 ? (
            <p className="no-suggestion">Không có gợi ý kết bạn nào.</p>
          ) : (
            <ul className="friend-suggestions-list">
              {suggestedFriends.map((friend) => (
                <li 
                  key={friend.maTK} 
                  className="friend-suggestion-item"
                        onClick={() => {
                              setSearchKeyWord("");
                              setSuggestedFriends([]);
                            }}>
                  <Link
                        to={`/profile?id=${friend.maTK}`}
                        className="post-author"
                  >
                        <div className="friend-info">
                              <img
                              src={
                                    friend.profilePic
                                    ? `/Resource/Avatar/${friend.profilePic}`
                                    : `/Resource/Avatar/default.png`
                              }
                              alt="Avatar"
                              className="friend-avatar"
                              />
                              <span className="friend-name">{friend.hoTen}</span>
                        </div>
                  </Link>
                </li>
              ))}
            </ul>
          )}
        </div>
      )}
    </div>
  );
}

export default SearchBar;
