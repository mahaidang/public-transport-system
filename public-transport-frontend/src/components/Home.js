import { Col, Row } from 'react-bootstrap';
import MapView from './MapView';
import MapUI from './MapUI';

const Home = () => {
    return (
        <>
            <Row>
                <Col md={12} className="p-4" style={{ height: '80vh' }}>
                    <MapUI center={[106.68, 10.77]} zoom={14}/>             
                </Col>
            </Row>
        </>
    );
};
export default Home
