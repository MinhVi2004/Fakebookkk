import React, { createContext, useContext, useActionState, useState} from "react";

import { toast } from "react-toastify";
// Create the context
const RequestContext = createContext();

// Create a provider component
export const RequestProvider = ({ children }) => {
  const [requestCount, setRequestCount] = useState(0);
  const fetchRequestCount = async () => {
      const userId = JSON.parse(sessionStorage.getItem("userSignin"))?.maTK;
      fetch(`http://localhost:8080/api/friends/requests?userId=${userId}`)
            .then((response) => response.json())
            .then((data) => {
              updateRequestCount(data.length); 
            })
            .catch((error) => {
              console.error("Error fetching friend requests:", error);
              toast.error("Không thể tải danh sách lời mời kết bạn.");
            });
        }
  const updateRequestCount = (count) => {
    setRequestCount(count);
  };

  return (
    <RequestContext.Provider value={{ requestCount, updateRequestCount, fetchRequestCount }}>
      {children}
    </RequestContext.Provider>
  );
};

// Custom hook to use the request context
export const useRequestContext = () => {
  return useContext(RequestContext);
};
