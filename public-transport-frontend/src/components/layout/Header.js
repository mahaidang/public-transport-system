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

  return (
    <Navbar expand="lg" className="bg-body-tertiary px-3">
      <Container>
        <Navbar.Brand as={Link} to="/">Thông tin giao thông</Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            <Nav.Link as={Link} to="/home">Trang chủ</Nav.Link>
            <Nav.Link href="#about">Giới thiệu</Nav.Link>
            <NavDropdown title="Thông tin tuyến" id="basic-nav-dropdown">
              <NavDropdown.Item href="#route">Tuyến</NavDropdown.Item>
              <NavDropdown.Item href="#search">Tra cứu đường</NavDropdown.Item>
              <NavDropdown.Item href="#favorite">Tuyến đường yêu thích</NavDropdown.Item>
            </NavDropdown>
          </Nav>

          <Nav className="ms-auto align-items-center gap-2">
            {!user ? (
              <>
                <Nav.Link as={Link} to="/login">
                  <CgProfile size={20} style={{ marginBottom: "3px" }} /> Đăng nhập
                </Nav.Link>
                <Button as={Link} to="/register" variant="outline-primary" size="sm">
                  Đăng ký
                </Button>
              </>
            ) : (
              <>
                <Image
                  src={user.photoURL}
                  roundedCircle
                  width="32"
                  height="32"
                  alt="avatar"
                  className="me-2"
                />
                <span className="me-2">{user.displayName}</span>
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
