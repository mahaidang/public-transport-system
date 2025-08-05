import React, { useEffect, useRef } from 'react';
import goongjs from '@goongmaps/goong-js';
import '@goongmaps/goong-js/dist/goong-js.css';
import ENV from '../configs/env';

const MapUI = ({ center = [106.7, 10.75], zoom = 13 }) => {
  const mapRef = useRef(null);
  const containerRef = useRef(null);

  useEffect(() => {
    if (!containerRef.current) return;

    goongjs.accessToken = ENV.GOONG_MAP_KEY;

    if (mapRef.current) {
      mapRef.current.remove();
    }

    const map = new goongjs.Map({
      container: containerRef.current,
      style: 'https://tiles.goong.io/assets/goong_map_web.json',
      center,
      zoom,
    });

    mapRef.current = map;

    return () => {
      if (mapRef.current) mapRef.current.remove();
    };
  }, [center, zoom]);

  return <div ref={containerRef} style={{ height: '400px', width: '100%', borderRadius: 8 }} />;
};

export default MapUI;
