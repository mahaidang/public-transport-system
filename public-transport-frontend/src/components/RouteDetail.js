import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import Apis, { endpoints } from '../configs/Apis';
import { Card, Badge, ListGroup, Spinner, Alert } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import dayjs from "dayjs";



const RouteDetail = () => {
    const { id } = useParams(); // Lấy route id từ URL
    const [route, setRoute] = useState(null);
    const [loading, setLoading] = useState(true);
    const [err, setErr] = useState(null);
    const navigate = useNavigate();
    const today = dayjs().format("YYYY-MM-DD");
    


    useEffect(() => {
        const loadRoute = async () => {
            try {
                const res = await Apis.get(`${endpoints.routes}/${id}`);
                setRoute(res.data);
            } catch (e) {
                setErr("Không thể tải dữ liệu tuyến.");
                console.error(e);
            } finally {
                setLoading(false);
            }
        };

        loadRoute();
    }, [id]);

    if (loading) return <Spinner animation="border" variant="primary" />;
    if (err) return <Alert variant="danger">{err}</Alert>;
    if (!route) return <Alert variant="warning">Không tìm thấy dữ liệu tuyến.</Alert>;

    return (
        <Card className="p-4 shadow-sm">
            <h4>
                {route.name} ({route.code}){" "}
                <Badge bg={route.isActive ? "success" : "secondary"}>
                    {route.isActive ? "Đang hoạt động" : "Tạm dừng"}
                </Badge>
            </h4>
            <p className="text-muted">{route.description}</p>

            <hr />

            <h5>Các chặng:</h5>
            {route.variants?.length > 0 ? (
                <ListGroup className="mb-3">
                    {route.variants.map((v) => (
                        <ListGroup.Item
                            key={v.id}
                            action
                            style={{ cursor: "pointer" }}
                            onClick={() => navigate(`/variants/${v.id}?date=${today}`)}
                        >
                            <strong>#{v.seq}</strong>: {v.name}
                        </ListGroup.Item>
                    ))}
                </ListGroup>
            ) : (
                <p>Không có chặng nào.</p>
            )}

        </Card>
    );
};

export default RouteDetail;
