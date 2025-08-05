import React, { useEffect, useState } from "react";
import { Container, Card, Button, Form, Image, Alert } from "react-bootstrap";
import axios from "axios";
import ENV from "../configs/env"; // ✅ Import key từ file cấu hình

const Profile = () => {
  const [user, setUser] = useState(null);
  const [newAvatar, setNewAvatar] = useState(null);
  const [preview, setPreview] = useState(null);
  const [success, setSuccess] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    const storedUser = localStorage.getItem("user");
    if (storedUser) {
      setUser(JSON.parse(storedUser));
    }
  }, []);

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setNewAvatar(file);
      setPreview(URL.createObjectURL(file));
    }
  };

  const uploadAvatar = async () => {
    const formData = new FormData();
    formData.append("file", newAvatar);
    formData.append("upload_preset", ENV.CLOUDINARY_UPLOAD_PRESET);

    try {
      const res = await axios.post(ENV.CLOUDINARY_API, formData);

      const updatedUser = { ...user, avatar: res.data.secure_url };
      setUser(updatedUser);
      localStorage.setItem("user", JSON.stringify(updatedUser));
      setSuccess("Cập nhật ảnh đại diện thành công!");
      setError("");
    } catch (err) {
      console.error(err);
      setError("Tải ảnh lên thất bại.");
      setSuccess("");
    }
  };

  if (!user) {
    return <Container className="mt-5 text-center">Không tìm thấy thông tin người dùng.</Container>;
  }

  return (
    <Container style={{ maxWidth: "500px", marginTop: "50px" }}>
      <Card className="text-center p-4">
        <h4 className="mb-4">Thông tin tài khoản</h4>

        {success && <Alert variant="success">{success}</Alert>}
        {error && <Alert variant="danger">{error}</Alert>}

        <Image
          src={preview || user.avatar || "https://via.placeholder.com/150"}
          roundedCircle
          width="150"
          height="150"
          className="mb-3"
        />
        <h5>{user.username || `${user.lastName} ${user.firstName}`}</h5>
        <p>{user.email}</p>

        <Form.Group className="my-3">
          <Form.Label>Chọn ảnh đại diện mới</Form.Label>
          <Form.Control type="file" accept="image/*" onChange={handleFileChange} />
        </Form.Group>

        <Button variant="primary" onClick={uploadAvatar} disabled={!newAvatar}>
          Cập nhật ảnh
        </Button>
      </Card>
    </Container>
  );
};

export default Profile;
