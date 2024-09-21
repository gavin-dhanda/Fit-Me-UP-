import { useState, SetStateAction, Dispatch } from "react";
import { Navigate } from "react-router-dom";
import { getLoginCookie } from "../../utils/cookie";
import LoginLogout from "./LoginLogout";

/**
 * A class that authorizes user login.
 */
interface AuthRouteProps {
  gatedContent: React.ReactNode;
}

function AuthRoute(props: AuthRouteProps) {
  const [loggedIn, setLogin] = useState(false);
  

  // SKIP THE LOGIN BUTTON IF YOU HAVE ALREADY LOGGED IN.
  if (!loggedIn && getLoginCookie() !== undefined) {
    setLogin(true);
  }

  return (
    <>
      <LoginLogout loggedIn={loggedIn} setLogin={setLogin} />
      {loggedIn ? props.gatedContent : <Navigate to="/login" />}
    </>
  );
}

export default AuthRoute;
