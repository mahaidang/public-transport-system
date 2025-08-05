import Apis, { endpoints } from '../configs/Apis';
import React, { useEffect, useState } from "react";
import { useParams, useSearchParams } from "react-router-dom";
import { Card, ListGroup, Spinner, Alert, Table, Form } from "react-bootstrap";
import dayjs from "dayjs";

const RouteVariantDetail = () => {
  const { id } = useParams();
  const [searchParams, setSearchParams] = useSearchParams();
  const [travelDate, setTravelDate] = useState(() => {
    return searchParams.get("date") || dayjs().format("YYYY-MM-DD");
  });

  const [variant, setVariant] = useState(null);
  const [loading, setLoading] = useState(true);
  const [err, setErr] = useState(null);

  useEffect(() => {
    const loadVariant = async () => {
      try {
        const res = await Apis.get(`${endpoints.variants}/${id}`, {
          params: { date: travelDate },
        });
        setVariant(res.data);
      } catch (e) {
        console.error(e);
        setErr("Không thể tải dữ liệu chặng.");
      } finally {
        setLoading(false);
      }
    };

    loadVariant();
  }, [id, travelDate]);

  if (loading) return <Spinner animation="border" variant="primary" />;
  if (err) return <Alert variant="danger">{err}</Alert>;
  if (!variant) return <Alert variant="warning">Không có dữ liệu chặng.</Alert>;

  return (
    <Card className="p-4 shadow-sm">
      <h4>{variant.name}</h4>
      <p>
        <strong>Thứ tự:</strong> #{variant.seq} <br />
        <strong>Khoảng cách:</strong> {variant.distanceKm} km<br />
        <strong>Thời gian di chuyển:</strong> {variant.timeMinute} phút
      </p>

      <hr />

      <Form.Group className="mb-3">
        <Form.Label>Chọn ngày:</Form.Label>
        <Form.Control
          type="date"
          value={travelDate}
          onChange={(e) => {
            const newDate = e.target.value;
            setTravelDate(newDate);
            setSearchParams({ date: newDate });
          }}
        />
      </Form.Group>

      <h5 className="mt-4">Lịch chạy ngày {travelDate}:</h5>
      {variant.scheduleTrips?.length > 0 ? (
        <Table bordered>
          <thead className="table-light">
            <tr>
              <th>#</th>
              <th>Thời gian đi</th>
              <th>Thời gian đến</th>
            </tr>
          </thead>
          <tbody>
            {variant.scheduleTrips.map((trip, idx) => (
              <tr key={idx}>
                <td>{idx + 1}</td>
                <td>{dayjs(trip.departTime).format("HH:mm")}</td>
                <td>{dayjs(trip.arriveTime).format("HH:mm")}</td>
              </tr>
            ))}
          </tbody>
        </Table>
      ) : (
        <p>Không có chuyến nào trong ngày này.</p>
      )}
    </Card>
  );
};

export default RouteVariantDetail;
