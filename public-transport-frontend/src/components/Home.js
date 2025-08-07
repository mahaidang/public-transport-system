import MapUI from './MapUI';
const Home = () => {
    return (
         <div style={{ height: '100vh', width: '100vw', position: 'relative', overflow: 'hidden' }}>
            <MapUI center={[106.68, 10.77]} zoom={14} />
        </div>
    );
};

export default Home;
