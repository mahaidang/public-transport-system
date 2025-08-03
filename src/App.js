import { BrowserRouter, Route, Routes, useLocation } from "react-router-dom";
import Header from "./components/layout/Header";
import Footer from "./components/layout/Footer";
import Home from "./components/Home";
import Login from "./components/Login";
import 'bootstrap/dist/css/bootstrap.min.css';
import { useEffect } from "react";
import Register from "./components/Register";

const LayoutWrapper = () => {
  const location = useLocation();

  // Ẩn Header/Footer nếu đang ở trang đăng nhập
  const isLoginPage = location.pathname === "/";
  const isRegisterPage = location.pathname === "/";

  useEffect(() => {
    window.scrollTo(0, 0); // Optional: cuộn lên đầu trang khi chuyển route
  }, [location.pathname]);

  return (
    <>
      {!isLoginPage && <Header /> || !isRegisterPage && <Header />}
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/home" element={<Home />} />
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
