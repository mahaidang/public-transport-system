import { BrowserRouter, Route, Routes } from "react-router-dom";
import Header from "./components/layout/Header";
import Footer from "./components/layout/Footer";
import Home from "./components/Home";
import Login from "./components/Login";
import 'bootstrap/dist/css/bootstrap.min.css';
import Register from "./components/Register";
import TrafficReport from "./components/TrafficReport";
import MapView from "./components/MapView";
import Profile from "./components/Profile";
import SearchStreet from "./components/SearchStreet";
import RouteDetail from "./components/RouteDetail";
import RouteVariantDetail from "./components/RouteVariantDetail";
import FavoriteRoutes from './components/FavoriteRoutes';



const LayoutWrapper = () => {
  return (
    <>
    <Header/>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/map" element={<MapView/>}/>
        <Route path="/report" element={<TrafficReport/>} />
        <Route path="/profile" element={<Profile />} />
        <Route path="/search" element={<SearchStreet/>}/>
        <Route path="/" element={<Home />} />
        <Route path="/routes/:id" element={<RouteDetail />} />
        <Route path="/variants/:id" element={<RouteVariantDetail />} />
        <Route path="/favorite" element={<FavoriteRoutes />} />
      </Routes>
      <Footer/>
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
