import React, { useState } from 'react';
import { Button, Container, Row, Col, Card, Form, Alert } from 'react-bootstrap';
import { signInWithPopup, GoogleAuthProvider } from 'firebase/auth';
import { auth } from '../configs/firebaseConfig';
import { useNavigate, Link } from 'react-router-dom';
import { FcGoogle } from 'react-icons/fc';
import cookie from 'react-cookies';
import Apis, { endpoints, authApis } from '../configs/Apis';

const Login = () => {
  const navigate = useNavigate();
  const [form, setForm] = useState({ email: '', password: '' });
  const [error, setError] = useState('');

  const handleGoogleLogin = async () => {
    const provider = new GoogleAuthProvider();
    try {
      const result = await signInWithPopup(auth, provider);
      const userInfo = result.user;

      const userData = {
        uid: userInfo.uid,
        email: userInfo.email,
        displayName: userInfo.displayName || userInfo.email,
        photoURL: userInfo.photoURL || `https://i.pravatar.cc/150?u=${userInfo.email}`
      };

      localStorage.setItem('user', JSON.stringify(userData));
      window.dispatchEvent(new Event("storage"));
      navigate('/');
    } catch (error) {
      console.error('Google login error:', error);
      setError('Đăng nhập Google thất bại');
    }
  };

  const handleEmailLogin = async (e) => {
    e.preventDefault();
    setError("");

    try {
      // 1. Gửi request login → nhận JWT
      const res = await Apis.post(endpoints.login, {
        username: form.email,
        passwordHash: form.password
      });

      const token = res.data.token;
      cookie.save("token", token); // lưu JWT vào cookie

      // 2. Gọi profile → lấy thông tin user
      const profileRes = await authApis().get(endpoints.profile);
      const userData = profileRes.data;

      // 3. Lưu vào localStorage và thông báo cập nhật header
      localStorage.setItem("user", JSON.stringify(userData));
      window.dispatchEvent(new Event("storage"));
      navigate("/");
    } catch (error) {
      console.error("Login error:", error);
      setError("Đăng nhập thất bại. Vui lòng kiểm tra tài khoản hoặc máy chủ.");
    }
  };

  return (
    <Container className="d-flex justify-content-center align-items-center" style={{ minHeight: '100vh' }}>
      <Row className="w-100 justify-content-center">
        <Col xs={12} md={6} lg={5}>
          <Card className="p-4 shadow">
            <h4 className="text-center mb-2 text-success fw-bold">Chào mừng bạn đã quay trở lại</h4>

            {error && <Alert variant="danger">{error}</Alert>}

            <Form onSubmit={handleEmailLogin}>
              <Form.Group className="mb-3">
                <Form.Label>Email</Form.Label>
                <Form.Control
                  type="email"
                  placeholder="Nhập email"
                  value={form.email}
                  onChange={(e) => setForm({ ...form, email: e.target.value })}
                  required
                />
              </Form.Group>

              <Form.Group className="mb-3">
                <Form.Label>Mật khẩu</Form.Label>
                <Form.Control
                  type="password"
                  placeholder="Nhập mật khẩu"
                  value={form.password}
                  onChange={(e) => setForm({ ...form, password: e.target.value })}
                  required
                />
              </Form.Group>

              <div className="d-flex justify-content-between align-items-center mb-3">
                <span></span>
                <Link to="#" className="text-decoration-none text-success">Quên mật khẩu</Link>
              </div>

              <Button type="submit" className="w-100 bg-success border-0">Đăng nhập</Button>
            </Form>

            <div className="text-center text-muted mt-3">Hoặc đăng nhập bằng</div>

            <Button variant="light" onClick={handleGoogleLogin} className="w-100 border mb-3 d-flex align-items-center justify-content-center">
              <FcGoogle size={22} className="me-2" />
              Đăng nhập bằng Google
            </Button>

            <div className="text-center mt-3 text-muted" style={{ fontSize: '0.9rem' }}>
              Bạn chưa có tài khoản? <Link to="/register" className="text-success fw-bold">Đăng ký ngay</Link>
            </div>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default Login;
