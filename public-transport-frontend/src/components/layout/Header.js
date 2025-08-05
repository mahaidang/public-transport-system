import { Container, Nav, Navbar, NavDropdown, Button, Image } from "react-bootstrap";
import { signOut } from "firebase/auth";
import { auth } from "../../configs/firebaseConfig";
import { useNavigate, Link } from "react-router-dom";
import { useEffect, useState } from "react";
import { CgProfile } from "react-icons/cg";

const Header = () => {
  const navigate = useNavigate();
  const [user, setUser] = useState(null);

  useEffect(() => {
    const userInfo = localStorage.getItem("user");
    if (userInfo) {
      setUser(JSON.parse(userInfo));
    } else {
      setUser(null);
    }
  }, []);

  const handleLogout = async () => {
    try {
      await signOut(auth);
      localStorage.removeItem("user");
      setUser(null);
      navigate("/login");
    } catch (err) {
      console.error("Đăng xuất thất bại:", err);
      alert("Đăng xuất thất bại");
    }
  };
  useEffect(() => {
  const loadUser = () => {
    const userInfo = localStorage.getItem("user");
    if (userInfo) setUser(JSON.parse(userInfo));
    else setUser(null);
  };

  loadUser(); // Lần đầu load

  // 👇 Lắng nghe sự kiện thay đổi từ localStorage (từ Login.js)
  window.addEventListener("storage", loadUser);

  return () => window.removeEventListener("storage", loadUser);
}, []);


  return (
    <Navbar expand="lg" className="bg-body-tertiary px-3">
      <Container>
        <Navbar.Brand as={Link} to="/">Thông tin giao thông</Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            <Link to="/home" className="nav-link">Trang chủ</Link>
            <Link to="/report" className="nav-link">Báo cáo</Link>
            <Link to="/profile" className="nav-link">Hồ sơ cá nhân</Link>

            <NavDropdown title="Thông tin tuyến" id="basic-nav-dropdown">
              <Link to="/map" className="dropdown-item">Tra cứu lộ trình bus</Link>
              <Link to="/search" className="dropdown-item">Tra cứu tuyến</Link>
              <Link to="/favorite" className="dropdown-item">Tuyến đường yêu thích</Link>
            </NavDropdown>
          </Nav>

          <Nav className="ms-auto align-items-center gap-2">
            {!user ? (
              <>
                <Link to="/login" className="nav-link d-flex align-items-center">
                  <CgProfile size={20} className="me-1" />
                  Đăng nhập
                </Link>
                <Button as={Link} to="/register" variant="outline-primary" size="sm">
                  Đăng ký
                </Button>
              </>
            ) : (
              <>
                <Image
                  src={user.avatar}
                  roundedCircle
                  width="32"
                  height="32"
                  alt="avatar"
                  className="me-2"
                />
                <span className="me-2">Xin chào {user.username}</span>
                <Button variant="outline-danger" size="sm" onClick={handleLogout}>
                  Đăng xuất
                </Button>
              </>
            )}
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default Header;
