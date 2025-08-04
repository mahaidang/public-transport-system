import MapView from '../components/MapView';
import { Button, Card, Col, Row } from 'react-bootstrap';
import MapSearchView from './MapSearchView';


const Home = () => {
    return (
        <>
            <Row>
                <Col md={12} className="p-4">
                    <h4>Tìm tuyến đường</h4>
                    <MapSearchView />
                </Col>
            </Row>

            <Row>
                
            </Row>
        </>
    );
};
export default Home
