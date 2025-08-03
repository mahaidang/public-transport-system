import React, { useState } from 'react';
import { Form, Button, Card, Row, Col } from 'react-bootstrap';
import { MapContainer, TileLayer, Marker, Polyline, useMap, Popup } from 'react-leaflet';
import L from 'leaflet';
import { FaMapMarkerAlt, FaLocationArrow } from 'react-icons/fa';
import locationIconImg from '../assets/location_8690883.png'; // icon tùy chỉnh

// Khởi tạo icon tùy chỉnh
const currentLocationIcon = new L.Icon({
    iconUrl: locationIconImg,
    iconSize: [40, 40],
    iconAnchor: [20, 40],
    popupAnchor: [0, -40]
});

const FitBounds = ({ route }) => {
    const map = useMap();
    React.useEffect(() => {
        if (route.length > 0) {
            const bounds = L.latLngBounds(route);
            map.fitBounds(bounds, { padding: [50, 50] });
        }
    }, [route, map]);
    return null;
};

const CurrentLocationMarker = ({ setFromCoord, setFromAddress }) => {
    const map = useMap();

    React.useEffect(() => {
        if (!navigator.geolocation) return;

        navigator.geolocation.getCurrentPosition(
            async (pos) => {
                const lat = pos.coords.latitude;
                const lon = pos.coords.longitude;
                const url = `/nominatim/reverse?format=json&lat=${lat}&lon=${lon}`;

                try {
                    const res = await fetch(url);
                    const data = await res.json();
                    const address = data.display_name || `${lat}, ${lon}`;
                    setFromCoord([lat, lon]);
                    setFromAddress(address);
                    map.setView([lat, lon], 14);
                } catch (err) {
                    console.error("Reverse geocoding failed", err);
                }
            },
            (err) => {
                console.warn("Không lấy được vị trí người dùng", err);
            }
        );
    }, [map, setFromCoord, setFromAddress]);

    return null;
};

const MapView = () => {
    const [fromAddress, setFromAddress] = useState('');
    const [toAddress, setToAddress] = useState('');
    const [fromSuggestions, setFromSuggestions] = useState([]);
    const [toSuggestions, setToSuggestions] = useState([]);
    const [fromCoord, setFromCoord] = useState(null);
    const [toCoord, setToCoord] = useState(null);
    const [route, setRoute] = useState([]);
    const [duration, setDuration] = useState(null);
    const [distance, setDistance] = useState(null);

    const fetchSuggestions = async (text, setOptions) => {
        if (!text || text.length < 3) return setOptions([]);
        const url = `/nominatim/search?format=json&q=${encodeURIComponent(text)}&addressdetails=1&limit=5`;
        try {
            const res = await fetch(url);
            const data = await res.json();
            const options = data.map(item => item.display_name);
            setOptions(options);
        } catch (err) {
            console.error('Autocomplete fetch failed:', err);
            setOptions([]);
        }
    };

    const geocodeAddress = async (address) => {
        const url = `/nominatim/search?format=json&q=${encodeURIComponent(address)}`;
        try {
            const res = await fetch(url);
            const data = await res.json();
            if (data.length > 0) {
                return [parseFloat(data[0].lat), parseFloat(data[0].lon)];
            }
        } catch (err) {
            console.error("Geocoding failed:", err);
        }
        return null;
    };

    const getRoute = async (from, to) => {
        const res = await fetch(`https://router.project-osrm.org/route/v1/driving/${from[1]},${from[0]};${to[1]},${to[0]}?overview=full&geometries=geojson`);
        const data = await res.json();
        if (data.routes && data.routes.length > 0) {
            const routeData = data.routes[0];
            const coords = routeData.geometry.coordinates.map(coord => [coord[1], coord[0]]);
            setRoute(coords);
            setDuration(routeData.duration);
            setDistance(routeData.distance);
        }
    };

    const handleFindRoute = async () => {
        const from = await geocodeAddress(fromAddress);
        const to = await geocodeAddress(toAddress);
        setFromCoord(from);
        setToCoord(to);
        setDuration(null);
        setDistance(null);
        if (from && to) await getRoute(from, to);
    };

    return (
        <div className="position-relative vh-100 w-100">
            <Card className="position-absolute top-0 start-50 translate-middle-x mt-3 w-100" style={{ maxWidth: "900px", zIndex: 999 }}>
                <Card.Body>
                    <Form>
                        <Row className="align-items-center g-2">
                            <Col md={5} className="position-relative">
                                <FaMapMarkerAlt className="position-absolute top-50 start-0 translate-middle-y ms-2 text-secondary" />
                                <Form.Control
                                    className="ps-4"
                                    list="fromSuggestions"
                                    type="text"
                                    placeholder="Từ địa chỉ..."
                                    value={fromAddress}
                                    onChange={(e) => {
                                        setFromAddress(e.target.value);
                                        fetchSuggestions(e.target.value, setFromSuggestions);
                                    }}
                                />
                                <datalist id="fromSuggestions">
                                    {fromSuggestions.map((item, idx) => (
                                        <option key={idx} value={item} />
                                    ))}
                                </datalist>
                                <Button
                                    variant="outline-secondary"
                                    className="position-absolute top-50 end-0 translate-middle-y me-1 py-1 px-2"
                                    onClick={() => {
                                        navigator.geolocation.getCurrentPosition(
                                            async (pos) => {
                                                const { latitude, longitude } = pos.coords;
                                                const url = `/nominatim/reverse?format=json&lat=${latitude}&lon=${longitude}`;
                                                const res = await fetch(url);
                                                const data = await res.json();
                                                const address = data.display_name || `${latitude}, ${longitude}`;
                                                setFromCoord([latitude, longitude]);
                                                setFromAddress(address);
                                            },
                                            (err) => {
                                                console.warn("Không lấy được vị trí người dùng", err);
                                            }
                                        );
                                    }}
                                >
                                    <FaLocationArrow />
                                </Button>
                            </Col>

                            <Col md={5} className="position-relative">
                                <FaMapMarkerAlt className="position-absolute top-50 start-0 translate-middle-y ms-2 text-secondary" />
                                <Form.Control
                                    className="ps-4"
                                    list="toSuggestions"
                                    type="text"
                                    placeholder="Đến địa chỉ..."
                                    value={toAddress}
                                    onChange={(e) => {
                                        setToAddress(e.target.value);
                                        fetchSuggestions(e.target.value, setToSuggestions);
                                    }}
                                />
                                <datalist id="toSuggestions">
                                    {toSuggestions.map((item, idx) => (
                                        <option key={idx} value={item} />
                                    ))}
                                </datalist>
                            </Col>

                            <Col md={2}>
                                <Button variant="primary" onClick={handleFindRoute} className="w-100">
                                    Tìm đường
                                </Button>
                            </Col>
                        </Row>

                        {duration && distance && (
                            <Row className="mt-3">
                                <Col>
                                    <div className="fw-bold text-success">
                                        🕒 {(duration / 60).toFixed(1)} phút — 📏 {(distance / 1000).toFixed(2)} km
                                    </div>
                                </Col>
                            </Row>
                        )}
                    </Form>
                </Card.Body>
            </Card>

            <MapContainer
                center={[10.762622, 106.660172]}
                zoom={13}
                className="w-100 h-100"
            >
                <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />

                <CurrentLocationMarker setFromCoord={setFromCoord} setFromAddress={setFromAddress} />

                {fromCoord && (
                    <Marker position={fromCoord} icon={currentLocationIcon}>
                        <Popup>Vị trí của bạn</Popup>
                    </Marker>
                )}
                {toCoord && <Marker position={toCoord} />}
                {route.length > 0 && (
                    <>
                        <Polyline positions={route} color="blue" />
                        <FitBounds route={route} />
                    </>
                )}
            </MapContainer>
        </div>
    );
};

export default MapView;
