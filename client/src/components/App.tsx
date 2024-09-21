import { initializeApp } from "firebase/app";
import "../styles/App.css";
import HomePage from "./pages/HomePage";
import SavedPage from "./pages/SavedPage";
import NavBar from "./navigation/NavBar";
import {
  BrowserRouter,
  Route,
  Routes,
} from "react-router-dom";
import { useEffect, useState, Dispatch, SetStateAction } from "react";
import AuthRoute from "./auth/AuthRoute";
import ClosetPage from "./pages/ClosetPage";
import GeneratePage from "./pages/GeneratePage";
import { updateClothing } from "../utils/api";

/**
 * App class that starts everything!
 */

const firebaseConfig = {
  apiKey: process.env.API_KEY,
  authDomain: process.env.AUTH_DOMAIN,
  projectId: process.env.PROJECT_ID,
  storageBucket: process.env.STORAGE_BUCKET,
  messagingSenderId: process.env.MESSAGING_SENDER_ID,
  appId: process.env.APP_ID,
};

initializeApp(firebaseConfig);

function App() {
  // Stores clothes in a map with the key being the id of the clothing item, and the
  // value being an array of the image url, primary color, and category of the clothing item.
  const [clothes, setClothes] = useState<Map<string, [string, string, string, string]>>(
    new Map()
  );
  const navBar = <NavBar />;

  // Use Effect
  useEffect(() => {
    updateClothing(setClothes);
  }, []);

  
  return (
    <div className="App">
      <BrowserRouter>
        <Routes>
          <Route
            path="/login"
            element={
              <AuthRoute
                gatedContent={
                  <div>
                    {navBar} <HomePage />
                  </div>
                }
              />
            }
          />
          <Route
            path="/"
            element={
              <AuthRoute
                gatedContent={
                  <div>
                    {navBar} <HomePage />
                  </div>
                }
              />
            }
          />
          <Route
            path="/home"
            element={
              <AuthRoute
                gatedContent={
                  <div>
                    {navBar} <HomePage />
                  </div>
                }
              />
            }
          />
          <Route
            path="/saved"
            element={
              <AuthRoute
                gatedContent={
                  <div>
                    {navBar} <SavedPage clothing={clothes} setClothing={setClothes}/>
                  </div>
                }
              />
            }
          />
          <Route
            path="/closet"
            element={
              <AuthRoute
                gatedContent={
                  <div>
                    {navBar}
                    <ClosetPage clothes={clothes} setClothes={setClothes} />
                  </div>
                }
              />
            }
          />
          <Route
            path="/generate"
            element={
              <AuthRoute
                gatedContent={
                  <div>
                    {navBar} <GeneratePage clothes={clothes} />
                  </div>
                }
              />
            }
          />
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
