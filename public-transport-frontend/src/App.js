import { BrowserRouter, Route, Routes, useLocation } from "react-router-dom";
import Header from "./components/layout/Header";
import Footer from "./components/layout/Footer";
import Home from "./components/Home";
import Login from "./components/Login";
import 'bootstrap/dist/css/bootstrap.min.css';
import { useEffect } from "react";
import Register from "./components/Register";
import TrafficReport from "./components/TrafficReport";
import MapView from "./components/MapView";
import Profile from "./components/Profile";
import SearchStreet from "./components/SearchStreet";

const LayoutWrapper = () => {
  const location = useLocation();


  const isLoginPage = location.pathname === "/";
  const isRegisterPage = location.pathname === "/";

  useEffect(() => {
    window.scrollTo(0, 0);
  }, [location.pathname]);

  return (
    <>
      {!isLoginPage && <Header /> || !isRegisterPage && <Header />}
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/map" element={<MapView/>}/>
        <Route path="/report" element={<TrafficReport/>} />
        <Route path="/profile" element={<Profile />} />
        <Route path="/search" element={<SearchStreet/>}/>
        <Route path="/" element={<Home />} />
      </Routes>
      {!isLoginPage && <Footer /> || !isRegisterPage && <Footer />}
    </>
  );
};

const App = () => {
  return (
    <BrowserRouter>
      <LayoutWrapper />
    </BrowserRouter>
  );
};

export default App;
