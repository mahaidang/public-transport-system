import React, { useEffect, useState } from 'react';
import { Card, ListGroup, Badge, Button, Spinner } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { authApis, endpoints } from '../configs/Apis';

const FavoriteRoutes = () => {
  const [favorites, setFavorites] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  const loadFavorites = async () => {
    try {
      const res = await authApis().get(endpoints.favorites);
      setFavorites(res.data);
    } catch (err) {
      console.error("Lỗi khi tải yêu thích:", err);
      setFavorites([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadFavorites();
  }, []);

  const handleNavigate = (routeId) => {
    navigate(`/routes/${routeId}`);
  };

  const handleDelete = async (favoriteId) => {
    if (window.confirm("Bạn có chắc muốn xoá tuyến này khỏi yêu thích?")) {
      try {
        await authApis().delete(`${endpoints.favorites}/${favoriteId}`);
        // Xoá khỏi UI
        setFavorites(favorites.filter((f) => f.id !== favoriteId));
      } catch (err) {
        console.error("Lỗi khi xoá yêu thích:", err);
        alert("Xoá thất bại!");
      }
    }
  };

  return (
    <Card className="p-4 shadow-sm mt-4">
      <h4 className="mb-4">Tuyến đường yêu thích</h4>

      {loading ? (
        <div className="text-center">
          <Spinner animation="border" variant="primary" />
        </div>
      ) : favorites.length === 0 ? (
        <p>Chưa có tuyến đường yêu thích nào.</p>
      ) : (
        <ListGroup>
          {favorites.map((f) => (
            <ListGroup.Item
              key={f.id}
              className="d-flex justify-content-between align-items-center"
            >
              <div
                style={{ cursor: "pointer" }}
                onClick={() => handleNavigate(f.route.id)}
              >
                <strong>{f.route.name}</strong> ({f.route.code})<br />
                <small>{f.route.description}</small><br />
                <Badge bg={f.route.isActive ? "success" : "secondary"}>
                  {f.route.isActive ? "Đang hoạt động" : "Tạm dừng"}
                </Badge>
              </div>

              <div className="d-flex flex-column gap-2 align-items-end">
                <Button
                  variant="outline-success"
                  size="sm"
                  onClick={() => handleNavigate(f.route.id)}
                >
                  Xem chi tiết
                </Button>

                <Button
                  variant="outline-danger"
                  size="sm"
                  onClick={() => handleDelete(f.id)}
                >
                  ❌ Xoá
                </Button>
              </div>
            </ListGroup.Item>
          ))}
        </ListGroup>
      )}
    </Card>
  );
};

export default FavoriteRoutes;
