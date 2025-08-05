import React, { useState } from 'react';
import { Button, Form, Row, Col, Alert, Image, Spinner } from 'react-bootstrap';
import { FaMapMarkerAlt, FaUpload, FaCheck, FaSpinner } from 'react-icons/fa';
import axios from 'axios';
import ENV from '../configs/env'; // ✅ import ENV

const TrafficReport = () => {
  const [description, setDescription] = useState('');
  const [address, setAddress] = useState('');
  const [coords, setCoords] = useState(null);
  const [imageFile, setImageFile] = useState(null);
  const [preview, setPreview] = useState('');
  const [status, setStatus] = useState('');
  const [loading, setLoading] = useState(false);

  const getCoordsFromAddress = async () => {
    try {
      const res = await axios.get('https://rsapi.goong.io/Geocode', {
        params: {
          address: address,
          api_key: ENV.GOONG_API_KEY
        }
      });

      if (res.data.status !== 'OK' || res.data.results.length === 0)
        throw new Error('Không tìm thấy địa chỉ.');

      const { lat, lng } = res.data.results[0].geometry.location;
      setCoords({ lat, lng });
      setStatus('✅ Đã lấy tọa độ thành công từ Goong');
    } catch (err) {
      setStatus('❌ Lỗi lấy tọa độ (Goong): ' + err.message);
    }
  };

  const getCurrentLocation = () => {
    if (!navigator.geolocation) return setStatus('⚠️ Trình duyệt không hỗ trợ định vị.');

    navigator.geolocation.getCurrentPosition(
      async (pos) => {
        const { latitude, longitude } = pos.coords;
        setCoords({ lat: latitude, lng: longitude });

        try {
          const res = await axios.get('https://rsapi.goong.io/Geocode', {
            params: {
              latlng: `${latitude},${longitude}`,
              api_key: ENV.GOONG_API_KEY
            }
          });

          if (res.data.status === 'OK' && res.data.results.length > 0) {
            setAddress(res.data.results[0].formatted_address);
            setStatus('📍 Đã định vị vị trí hiện tại và cập nhật địa chỉ');
          } else {
            setStatus('⚠️ Không thể tìm thấy địa chỉ từ vị trí hiện tại.');
          }
        } catch (err) {
          console.error(err);
          setStatus('❌ Lỗi khi truy xuất địa chỉ từ Goong');
        }
      },
      () => setStatus('❌ Không thể lấy vị trí hiện tại.')
    );
  };

  const handleUploadImage = async () => {
    const form = new FormData();
    form.append('file', imageFile);
    form.append('upload_preset', ENV.CLOUDINARY_UPLOAD_PRESET); // ✅
    const res = await axios.post(ENV.CLOUDINARY_API, form);       // ✅
    return res.data.secure_url;
  };

  const handleSubmit = async () => {
    if (!description || !imageFile || !coords) {
      return setStatus('⚠️ Vui lòng nhập đầy đủ mô tả, vị trí và hình ảnh.');
    }

    try {
      setLoading(true);
      setStatus('⏳ Đang gửi báo cáo...');

      const imageUrl = await handleUploadImage();

      await axios.post('http://localhost:8080/TransportApp/api/reports', {
        description,
        latitude: coords.lat,
        longitude: coords.lng,
        imageUrl
      });

      setStatus('✅ Báo cáo đã gửi thành công!');
      setDescription('');
      setAddress('');
      setCoords(null);
      setImageFile(null);
      setPreview('');
    } catch (err) {
      console.error(err);
      setStatus('❌ Gửi báo cáo thất bại.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-3">
      <h4 className="mb-3 text-primary">📝 Báo cáo tình trạng kẹt xe</h4>

      <Form>
        <Form.Group className="mb-3">
          <Form.Label>Mô tả tình trạng</Form.Label>
          <Form.Control
            as="textarea"
            rows={3}
            placeholder="Nhập mô tả ngắn gọn..."
            value={description}
            onChange={(e) => setDescription(e.target.value)}
          />
        </Form.Group>

        <Row className="mb-3 g-2">
          <Col md={8}>
            <Form.Control
              placeholder="Nhập địa chỉ hoặc tên đường"
              value={address}
              onChange={(e) => setAddress(e.target.value)}
            />
          </Col>
          <Col>
            <Button variant="primary" onClick={getCoordsFromAddress}>
              <FaMapMarkerAlt className="me-1" /> Tìm
            </Button>
          </Col>
          <Col>
            <Button variant="outline-secondary" onClick={getCurrentLocation}>
              📍 Hiện tại
            </Button>
          </Col>
        </Row>

        <Form.Group className="mb-3">
          <Form.Label>Hình ảnh minh hoạ</Form.Label>
          <Form.Control
            type="file"
            accept="image/*"
            onChange={(e) => {
              setImageFile(e.target.files[0]);
              setPreview(URL.createObjectURL(e.target.files[0]));
            }}
          />
        </Form.Group>

        {preview && (
          <Image src={preview} thumbnail className="mb-3" style={{ maxHeight: '300px' }} />
        )}

        <div className="d-grid mb-2">
          <Button variant="success" onClick={handleSubmit} disabled={loading}>
            {loading ? <><FaSpinner className="spin me-2" /> Đang gửi...</> : <><FaUpload className="me-2" /> Gửi báo cáo</>}
          </Button>
        </div>

        {status && <Alert variant="info" className="mt-2">{status}</Alert>}
      </Form>
    </div>
  );
};

export default TrafficReport;
