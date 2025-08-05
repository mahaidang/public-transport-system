import React, { useState } from "react";
import { Form, Card, ListGroup, Badge, Button, Row, Col } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import Apis, { endpoints, authApis } from '../configs/Apis';



const SearchStreet = () => {
  const [filters, setFilters] = useState({
    name: "",
    code: "",
    start: "",
    end: "",
  });


  const [results, setResults] = useState([]);
  const navigate = useNavigate();
  const [page, setPage] = useState(1);         // trang hiện tại
  const [hasNext, setHasNext] = useState(false); // kiểm tra còn trang tiếp không


  const handleChange = (e) => {
    setFilters({ ...filters, [e.target.name]: e.target.value });
  };

  const handleSearch = async (pageNum = 1) => {
    try {
      const res = await Apis.get(endpoints.routes, {
        params: {
          name: filters.name,
          code: filters.code,
          start: filters.start,
          end: filters.end,
          page: pageNum,
        },
      });

      setResults(res.data);
      setPage(pageNum);
      setHasNext(res.data.length === 10); // nếu đủ PAGE_SIZE thì có thể còn trang tiếp
    } catch (err) {
      console.error("Lỗi khi tìm tuyến:", err);
      setResults([]);
    }
  };

  const handleFavorite = async (routeId) => {
    try {
      await authApis().post(endpoints.favorites, {
        route: { id: routeId }
      });
      alert("Đã thêm vào yêu thích!");
    } catch (err) {
      console.error("Lỗi khi thêm yêu thích:", err);
      alert("Lỗi khi thêm yêu thích!");
    }
  };


  return (
    <Card className="p-4 shadow-sm">
      <h4 className="mb-4">Tìm kiếm tuyến xe</h4>
      <Form>
        <Row className="mb-3">
          <Col md={6}>
            <Form.Group>
              <Form.Label>Tên tuyến</Form.Label>
              <Form.Control
                type="text"
                name="name"
                value={filters.name}
                onChange={handleChange}
                placeholder="VD: Tuyến 01"
              />
            </Form.Group>
          </Col>
          <Col md={6}>
            <Form.Group>
              <Form.Label>Mã tuyến</Form.Label>
              <Form.Control
                type="text"
                name="code"
                value={filters.code}
                onChange={handleChange}
                placeholder="VD: RT001"
              />
            </Form.Group>
          </Col>
        </Row>

        <Row className="mb-3">
          <Col md={6}>
            <Form.Group>
              <Form.Label>Điểm đi</Form.Label>
              <Form.Control
                type="text"
                name="start"
                value={filters.start}
                onChange={handleChange}
                placeholder="VD: Mỹ Đình"
              />
            </Form.Group>
          </Col>
          <Col md={6}>
            <Form.Group>
              <Form.Label>Điểm đến</Form.Label>
              <Form.Control
                type="text"
                name="end"
                value={filters.end}
                onChange={handleChange}
                placeholder="VD: Ga Hà Nội"
              />
            </Form.Group>
          </Col>
        </Row>

        <div className="d-flex justify-content-end">
          <Button onClick={() => handleSearch(1)} variant="primary">
            Tìm kiếm
          </Button>

        </div>
      </Form>

      <hr />

      <h5>Kết quả:</h5>
      <ListGroup>
        {results.length === 0 ? (
          <ListGroup.Item>Không tìm thấy tuyến phù hợp.</ListGroup.Item>
        ) : (
          results.map((s) => (
            <ListGroup.Item
              key={s.id}
              className="d-flex justify-content-between align-items-center"
            >
              <div
                style={{ cursor: "pointer" }}
                onClick={() => navigate(`/routes/${s.id}`)}
              >
                <strong>{s.name}</strong> ({s.code})<br />
                <small>{s.description}</small><br />
                <Badge bg={s.isActive ? "success" : "secondary"}>
                  {s.isActive ? "Đang hoạt động" : "Tạm dừng"}
                </Badge>
              </div>

              <Button
                size="sm"
                variant="outline-danger"
                onClick={() => handleFavorite(s.id)}
                title="Yêu thích tuyến"
              >
                ❤️
              </Button>
            </ListGroup.Item>

          ))
        )}
      </ListGroup>
      {results.length > 0 && (
        <div className="d-flex justify-content-between mt-3">
          <Button
            disabled={page === 1}
            onClick={() => handleSearch(page - 1)}
            variant="secondary"
          >
            Trang trước
          </Button>

          <span>Trang {page}</span>

          <Button
            disabled={!hasNext}
            onClick={() => handleSearch(page + 1)}
            variant="secondary"
          >
            Trang sau
          </Button>
        </div>
      )}

    </Card>
  );
};

export default SearchStreet;
