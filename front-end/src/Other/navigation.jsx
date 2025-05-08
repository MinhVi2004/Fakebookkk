import { useNavigate } from "react-router-dom";

export const useNavigation = () => {
  const navigate = useNavigate();

  return {
    goToSignup: () => navigate("/signup"),
    goToSignin: () => navigate("/signin"),
    goToProfile: () => navigate("/profile"),
    goToHomePage: () => navigate("/home"),
    goToChat: () => navigate("/chat"),
    goToProfileById: (id) => {
      const targetUrl = `/profile?id=${id}`;
      const currentUrl = window.location.pathname + window.location.search;
      if (currentUrl === targetUrl) {
        window.location.reload(); // ép reload
      } else {
        navigate(targetUrl);
      }
    },
  };
};
