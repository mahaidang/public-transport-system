import React, { useState } from "react";
import { Form, Card, ListGroup, Badge, Button, Row, Col } from "react-bootstrap";

const SearchStreet = () => {
  const [filters, setFilters] = useState({
    name: "",
    code: "",
    start: "",
    end: "",
  });

  const [results, setResults] = useState([]);

  const streets = [
    {
      id: 1,
      code: "RT001",
      name: "Tuyến 01",
      description: "Mỹ Đình ↔ Ga Hà Nội",
      isActive: true,
    },
    {
      id: 5,
      code: "RT005",
      name: "Tuyến 05",
      description: "Cổng Bách Khoa ↔ Hồ Gươm",
      isActive: false,
    },
  ];

  const handleChange = (e) => {
    setFilters({ ...filters, [e.target.name]: e.target.value });
  };

  const handleSearch = () => {
    const { name, code, start, end } = filters;
    const filtered = streets.filter((s) => {
      const matchesName = name === "" || s.name.toLowerCase().includes(name.toLowerCase());
      const matchesCode = code === "" || s.code.toLowerCase().includes(code.toLowerCase());
      const matchesStart = start === "" || s.description.toLowerCase().includes(start.toLowerCase());
      const matchesEnd = end === "" || s.description.toLowerCase().includes(end.toLowerCase());
      return matchesName && matchesCode && matchesStart && matchesEnd;
    });
    setResults(filtered);
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
          <Button onClick={handleSearch} variant="primary">
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
            <ListGroup.Item key={s.id}>
              <div className="d-flex justify-content-between align-items-center">
                <div>
                  <strong>{s.name}</strong> ({s.code})<br />
                  <small>{s.description}</small>
                </div>
                <Badge bg={s.isActive ? "success" : "secondary"}>
                  {s.isActive ? "Đang hoạt động" : "Tạm dừng"}
                </Badge>
              </div>
            </ListGroup.Item>
          ))
        )}
      </ListGroup>
    </Card>
  );
};

export default SearchStreet;
