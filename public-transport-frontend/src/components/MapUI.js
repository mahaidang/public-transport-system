import React, { useEffect, useRef } from 'react';
import goongjs from '@goongmaps/goong-js';
import '@goongmaps/goong-js/dist/goong-js.css';
import ENV from '../configs/env';
import locationIcon from '../assets/location_8690883.png'; // Đảm bảo đúng path

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

    // Thêm điều khiển zoom
    map.addControl(new goongjs.NavigationControl(), 'bottom-right');

    // Tạo nút tìm vị trí hiện tại
    const locateBtn = document.createElement('button');
    locateBtn.innerHTML = '📍';
    locateBtn.title = 'Tìm vị trí hiện tại';
    locateBtn.style.cssText = `
      position: absolute;
      top: 10px;
      left: 10px;
      background: white;
      border: none;
      border-radius: 4px;
      padding: 8px;
      font-size: 18px;
      cursor: pointer;
      z-index: 1;
    `;

    locateBtn.onclick = () => {
      if (!navigator.geolocation) {
        alert('Trình duyệt không hỗ trợ định vị!');
        return;
      }

      navigator.geolocation.getCurrentPosition((position) => {
        const lng = position.coords.longitude;
        const lat = position.coords.latitude;

        const userLocation = [lng, lat];

        map.flyTo({ center: userLocation, zoom: 15 });

        // Tạo marker với icon người dùng
        const el = document.createElement('img');
        el.src = locationIcon;
        el.style.width = '30px';
        el.style.height = '30px';
        el.style.borderRadius = '50%';

        new goongjs.Marker({ element: el }).setLngLat(userLocation).addTo(map);
      }, () => {
        alert('Không thể lấy vị trí hiện tại!');
      });
    };

    map.getContainer().appendChild(locateBtn);

    return () => {
      if (mapRef.current) mapRef.current.remove();
    };
  }, [center, zoom]);

  return <div ref={containerRef} style={{ height: '100%', width: '100%', borderRadius: 8 }} />;
};

export default MapUI;
