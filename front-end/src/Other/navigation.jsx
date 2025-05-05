import { useNavigate } from "react-router-dom";

export const useNavigation = () => {
  const navigate = useNavigate();

  return {
    goToSignup: () => navigate("/signup"),
    goToSignin: () => navigate("/signin"),
    goToProfile: () => navigate("/profile"),
    goToHomePage: () => navigate("/home"),
    goToChat: () => navigate("/chat"),
  };
};
