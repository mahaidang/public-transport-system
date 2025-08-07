import React, { useState, useMemo, useEffect } from 'react';
import { Button, Form, Alert, Image, Spinner } from 'react-bootstrap';
import { FaUpload, FaSpinner } from 'react-icons/fa';
import axios from 'axios';
import AsyncSelect from 'react-select/async';
import debounce from 'lodash.debounce';
import ENV from '../configs/env';
import { authApis } from '../configs/Apis';
import { endpoints } from '../configs/Apis';

const GOONG_API_KEY = ENV.GOONG_PLACES_KEY;

const TrafficReport = () => {
  const [description, setDescription] = useState('');
  const [address, setAddress] = useState('');
  const [coords, setCoords] = useState(null);
  const [imageFile, setImageFile] = useState(null);
  const [preview, setPreview] = useState('');
  const [status, setStatus] = useState('');
  const [loading, setLoading] = useState(false);

  // 🔎 Fetch gợi ý địa chỉ từ Goong
  const fetchSuggestions = async (inputValue) => {
    if (!inputValue) return [];
    const res = await axios.get('https://rsapi.goong.io/Place/AutoComplete', {
      params: { input: inputValue, api_key: GOONG_API_KEY }
    });
    return res.data.predictions.map((p) => ({
      label: p.description,
      value: p.place_id
    }));
  };

  const debouncedFetch = useMemo(() => debounce(fetchSuggestions, 800), []);
  useEffect(() => () => debouncedFetch.cancel(), [debouncedFetch]);

  // 📍 Lấy toạ độ từ place_id
  const handlePlaceSelect = async (selected) => {
    if (!selected) return;

    try {
      const res = await axios.get('https://rsapi.goong.io/Place/Detail', {
        params: { place_id: selected.value, api_key: GOONG_API_KEY }
      });

      const { lat, lng } = res.data.result.geometry.location;
      setCoords({ lat, lng });
      setAddress(selected.label);
      setStatus('📍 Đã chọn địa chỉ và lấy toạ độ thành công');
    } catch (err) {
      console.error(err);
      setStatus('❌ Lỗi khi lấy toạ độ từ Goong');
    }
  };

  // ☁️ Upload ảnh lên Cloudinary
  const handleUploadImage = async () => {
    const form = new FormData();
    form.append('file', imageFile);
    form.append('upload_preset', ENV.CLOUDINARY_UPLOAD_PRESET);
    const res = await axios.post(ENV.CLOUDINARY_API, form);
    return res.data.secure_url;
  };

  // 📤 Gửi báo cáo
  const handleSubmit = async () => {
    if (!description || !imageFile || !coords) {
      return setStatus('⚠️ Vui lòng nhập đầy đủ mô tả, vị trí và hình ảnh.');
    }

    try {
      setLoading(true);
      setStatus('⏳ Đang gửi báo cáo...');

      const imageUrl = await handleUploadImage();

      await authApis().post(endpoints.reports, {
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

        <Form.Group className="mb-3">
          <Form.Label>Chọn địa điểm</Form.Label>
          <AsyncSelect
            cacheOptions
            loadOptions={debouncedFetch}
            onChange={handlePlaceSelect}
            placeholder="Nhập địa chỉ để tìm gợi ý..."
            defaultOptions
          />
        </Form.Group>

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
            {loading ? (
              <>
                <FaSpinner className="spin me-2" /> Đang gửi...
              </>
            ) : (
              <>
                <FaUpload className="me-2" /> Gửi báo cáo
              </>
            )}
          </Button>
        </div>

        {status && <Alert variant="info" className="mt-2">{status}</Alert>}
      </Form>
    </div>
  );
};

export default TrafficReport;
