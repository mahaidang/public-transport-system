import React, { useState } from "react";
import { Form, Button, Container, Alert } from "react-bootstrap";
import emailjs from "@emailjs/browser";
import { useNavigate } from "react-router-dom";

const Register = () => {
  const navigate = useNavigate();

  const [form, setForm] = useState({
    firstName: "",
    lastName: "",
    phone: "",
    email: "",
    password: "",
    confirmPassword: "",
    otp: "",
  });

  const [generatedOTP, setGeneratedOTP] = useState("");
  const [step, setStep] = useState(1);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const generateOTP = () => {
    return Math.floor(100000 + Math.random() * 900000).toString();
  };

  const sendOTP = async () => {
    const otp = generateOTP();
    setGeneratedOTP(otp);

    try {
      await emailjs.send(
        "service_ty03utj",
        "template_e6yvzrh",
        {
          to_email: form.email,
          otp_code: otp,
        },
        "pMlJ1FSBKzh_30n19"
      );
      setStep(2);
      setSuccess("Mã OTP đã được gửi đến Gmail!");
    } catch (err) {
      console.error(err);
      setError("Gửi mã OTP thất bại. Vui lòng kiểm tra Gmail hoặc cấu hình EmailJS.");
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess("");

    if (!form.phone || !form.email || !form.password || !form.confirmPassword) {
      setError("Vui lòng điền đầy đủ các trường bắt buộc.");
      return;
    }

    if (form.password !== form.confirmPassword) {
      setError("Mật khẩu và xác nhận mật khẩu không trùng khớp.");
      return;
    }

    await sendOTP();
  };

  const handleVerify = () => {
    setError("");
    setSuccess("");

    if (form.otp === generatedOTP) {
      alert("Xác thực thành công! Tài khoản đã đăng ký.");
      navigate("/login");
    } else {
      setError("Mã OTP không chính xác!");
    }
  };

  return (
    <Container style={{ maxWidth: "500px", marginTop: "50px" }}>
      <h3 className="mb-4 text-center">Đăng ký tài khoản</h3>

      {error && <Alert variant="danger">{error}</Alert>}
      {success && <Alert variant="success">{success}</Alert>}

      {step === 1 ? (
        <Form onSubmit={handleSubmit}>
          <Form.Group className="mb-3">
            <Form.Label>Họ</Form.Label>
            <Form.Control
              type="text"
              value={form.lastName}
              onChange={(e) => setForm({ ...form, lastName: e.target.value })}
            />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Tên</Form.Label>
            <Form.Control
              type="text"
              value={form.firstName}
              onChange={(e) => setForm({ ...form, firstName: e.target.value })}
            />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Số điện thoại <span className="text-danger">*</span></Form.Label>
            <Form.Control
              type="text"
              required
              value={form.phone}
              onChange={(e) => setForm({ ...form, phone: e.target.value })}
            />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Email <span className="text-danger">*</span></Form.Label>
            <Form.Control
              type="email"
              required
              value={form.email}
              onChange={(e) => setForm({ ...form, email: e.target.value })}
            />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Mật khẩu <span className="text-danger">*</span></Form.Label>
            <Form.Control
              type="password"
              required
              value={form.password}
              onChange={(e) => setForm({ ...form, password: e.target.value })}
            />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Xác nhận mật khẩu <span className="text-danger">*</span></Form.Label>
            <Form.Control
              type="password"
              required
              value={form.confirmPassword}
              onChange={(e) => setForm({ ...form, confirmPassword: e.target.value })}
            />
          </Form.Group>
          <div className="mb-3">
          <Button type="submit" className="w-100">Gửi mã OTP</Button>
          </div>
        </Form>
      ) : (
        <Form onSubmit={(e) => { e.preventDefault(); handleVerify(); }}>
          <Form.Group className="mb-10">
            <Form.Label>Nhập mã OTP đã gửi tới Gmail</Form.Label>
            <Form.Control
              type="text"
              required
              value={form.otp}
              onChange={(e) => setForm({ ...form, otp: e.target.value })}
            />
          </Form.Group>
          <Button type="submit" className="w-100">Xác nhận</Button>
        </Form>
      )}
    </Container>
  );
};

export default Register;
