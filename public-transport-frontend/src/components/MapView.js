import { useState, useMemo, useEffect, useRef } from 'react';
import AsyncSelect from 'react-select/async';
import axios from 'axios';
import dayjs from 'dayjs';
import debounce from 'lodash.debounce';

/*────────── Goong JS (Mapbox-GL) ──────────*/
import goongjs from '@goongmaps/goong-js';
import '@goongmaps/goong-js/dist/goong-js.css';

/*────────── Polyline decoder ──────────*/
import polyline from '@mapbox/polyline';
import polylineMapbox from "@mapbox/polyline";

/*────────── Backend API ──────────*/
import Apis, { endpoints } from '../configs/Apis';

/*────────── Keys & constants ──────────*/
const GOONG_PLACES_KEY = 'Q4bkZ3oKTpdRmZBsFwpezaOcYmXyuYuZA4WIeRLW';
const GOONG_MAP_KEY    = 'vbiUfQEWCEZAIgIaxD1hMpCkhIDmqiXUOPILcrYa';
const GOONG_STYLE_URL  = 'https://tiles.goong.io/assets/goong_map_web.json';
const MAX_WAYPOINTS    = 25; // Goong Directions API limit

/*====================================================================*/
export default function RouteOptimizeQuickTest() {
  const [start,  setStart]  = useState(null);   // { lat, lng, label }
  const [end,    setEnd]    = useState(null);
  const [routes, setRoutes] = useState([]);     // array ItineraryDTO





  /*────────────────── 1. Autocomplete (debounced) ──────────────────*/
  const fetchOptions = async (inputValue) => {
    if (!inputValue) return [];
    const { data } = await axios.get('https://rsapi.goong.io/Place/AutoComplete', {
      params: { api_key: GOONG_PLACES_KEY, input: inputValue }
    });
    return data.predictions.map((p) => ({ label: p.description, value: p.place_id }));
  };

  const debouncedLoadOptions = useMemo(() => debounce(fetchOptions, 1000), []);
  useEffect(() => () => debouncedLoadOptions.cancel(), [debouncedLoadOptions]);

  const fetchLatLng = async (place_id) => {
    const { data } = await axios.get('https://rsapi.goong.io/Place/Detail', {
      params: { api_key: GOONG_PLACES_KEY, place_id }
    });
    return data.result.geometry.location; // { lat, lng }
  };

  const handlePick = (setter) => async (opt) => {
    if (!opt) return;
    const { lat, lng } = await fetchLatLng(opt.value);
    setter({ lat, lng, label: opt.label });
  };

  /*────────────────── 2. Call backend ──────────────────*/
  const handleSearch = async () => {
    if (!start || !end) return alert('⚠️  Chọn cả điểm đi và điểm đến!');

    const payload = {
      start: { lat: start.lat, lng: start.lng },
      end:   { lat: end.lat,   lng: end.lng   },
      travelDate: dayjs().format('YYYY-MM-DD'),
      earliestDepart: dayjs().format('HH:mm:ss'),
      sortBy: 'TIME'
    };

    try {
      const res = await Apis.post(endpoints.optimizeRoute, payload);
      setRoutes(res.data);
      console.log('[Optimize] →', res.data);
    } catch (err) {
      console.error(err);
      alert('Lỗi gọi API tối ưu!');
    }
  };

  /*────────────────── 3. Derived data ──────────────────*/
  const best = useMemo(() => (routes?.length ? routes[0] : null), [routes]);

  /*────────────────── 4. UI ──────────────────*/
  return (
    <div style={{ maxWidth: 640, margin: 'auto', padding: 16 }}>
      <h2 style={{ fontWeight: 600, marginBottom: 12 }}>Tìm lộ trình bus</h2>

      {/* Autocomplete */}
      <div style={{ display: 'grid', gap: 12 }}>
        <AsyncSelect cacheOptions loadOptions={debouncedLoadOptions} onChange={handlePick(setStart)} placeholder="Điểm đi…" />
        <AsyncSelect cacheOptions loadOptions={debouncedLoadOptions} onChange={handlePick(setEnd)}   placeholder="Điểm đến…" />
      </div>

      <button
        onClick={handleSearch}
        style={{ marginTop: 16, padding: '8px 18px', background: '#4f46e5', color: '#fff', borderRadius: 6 }}>
        Tìm lộ trình
      </button>

      {/* Map + Thông tin */}
      {best && (
        <div style={{ marginTop: 24 }}>
          <MapSection itinerary={best} />
          <LegTable itinerary={best} />
        </div>
      )}
    </div>
  );
}

/*──────────────────── Map section ───────────────────*/
function MapSection({ itinerary }) {
  const containerRef = useRef(null);
  const mapRef       = useRef(null);

  // Helper: build Goong Directions URL with optional waypoints
  const buildDirectionURL = (points) => {
    const origin      = `${points[0][1]},${points[0][0]}`; // lat,lng
    const destination = `${points[points.length - 1][1]},${points[points.length - 1][0]}`;

    const mid = points.slice(1, -1);
    // Giới hạn 23 waypoint => 25 điểm tổng
    let selected = mid;
    if (mid.length > MAX_WAYPOINTS - 2) {
      const step = Math.ceil(mid.length / (MAX_WAYPOINTS - 2));
      selected = mid.filter((_, idx) => idx % step === 0);
    }
    const waypointStr = selected.map(([lng, lat]) => `${lat},${lng}`).join('|');

    // const params = new URLSearchParams({
    //   api_key: GOONG_MAP_KEY,
    //   origin,
    //   destination,
    //   vehicle: 'car',
    // });
    if (waypointStr) params.append('waypoints', waypointStr);

    return `https://rsapi.goong.io/direction?origin=${origin}&destination=${destination}&vehicle=car&api_key=Q4bkZ3oKTpdRmZBsFwpezaOcYmXyuYuZA4WIeRLW`;
  };

  useEffect(() => {
    if (!containerRef.current) return;

    const fetchLegPolyline = async (leg) => {
      // 1. Chuẩn hoá toạ độ từ BE => [lng,lat]
      let pts = [];
      if (typeof leg.polyline === 'string') {
        pts = polyline.decode(leg.polyline).map(([lat, lng]) => [lng, lat]);
      } else if (Array.isArray(leg.polyline)) {
        pts = leg.polyline.map(([lat, lng]) => [lng, lat]);
      }
      if (pts.length < 2) return pts; // không đủ => bỏ

      // 2. Gọi Goong lấy đường ô‑tô cho toàn bộ pts thông qua waypoints
      try {
        const url = buildDirectionURL(pts);
        const { data } = await axios.get(url);
        const polyline  = polylineMapbox.decode(data.routes[0].overview_polyline.points);



        return polyline.map(([lat, lng]) => [lng, lat]);
      } catch (e) {
        console.warn('Direction fallback', e);
        return pts; // fallback đường thẳng
      }
    };

    const render = async () => {
      // Fetch & merge tất cả leg
      const routeCoords = [];
      for (const leg of itinerary.legs) {
        const seg = await fetchLegPolyline(leg);
        if (!seg.length) continue;
        if (routeCoords.length) seg.shift(); // tránh lặp điểm giao
        routeCoords.push(...seg);
      }
      if (!routeCoords.length) return;

      /*──── Vẽ map ────*/
      goongjs.accessToken = GOONG_MAP_KEY;
      if (mapRef.current) mapRef.current.remove();

      const map = new goongjs.Map({
        container: containerRef.current,
        style: GOONG_STYLE_URL,
        center: routeCoords[0],
        zoom: 13
      });
      mapRef.current = map;

      map.on('load', () => {
        map.addSource('route', {
          type: 'geojson',
          data: {
            type: 'Feature',
            geometry: { type: 'LineString', coordinates: routeCoords }
          }
        });
        map.addLayer({
          id: 'route-line',
          type: 'line',
          source: 'route',
          paint: { 'line-color': '#2563eb', 'line-width': 5, 'line-opacity': 0.9 }
        });

        new goongjs.Marker({ color: '#10b981' })
          .setLngLat(routeCoords[0])
          .setPopup(new goongjs.Popup().setText('Điểm đi'))
          .addTo(map);

        new goongjs.Marker({ color: '#ef4444' })
          .setLngLat(routeCoords.at(-1))
          .setPopup(new goongjs.Popup().setText('Điểm đến'))
          .addTo(map);

        const bounds = routeCoords.reduce(
          (b, c) => b.extend(c),
          new goongjs.LngLatBounds(routeCoords[0], routeCoords[0])
        );
        map.fitBounds(bounds, { padding: 40 });
      });
    };

    render();
    return () => { if (mapRef.current) mapRef.current.remove(); };
  }, [itinerary]);

  return <div ref={containerRef} style={{ height: 350, borderRadius: 8 }} />;
}

/*──────────────────── Table section ───────────────────*/
function LegTable({ itinerary }) {
  return (
    <table style={{ width: '100%', marginTop: 16, fontSize: 14, borderCollapse: 'collapse' }}>
      <thead>
        <tr style={{ background: '#f3f4f6' }}>
          <th style={{ padding: 6 }}>#</th>
          <th style={{ padding: 6, textAlign: 'left' }}>Chặng</th>
          <th style={{ padding: 6 }}>Thời gian</th>
          <th style={{ padding: 6 }}>Km</th>
        </tr>
      </thead>
      <tbody>
        {itinerary.legs.map((leg, i) => (
          <tr key={i} style={{ borderBottom: '1px solid #e5e7eb' }}>
            <td style={{ padding: 6 }}>{leg.mode === 'BUS' ? '🚌' : '🚶'}</td>
            <td style={{ padding: 6 }}>
              {leg.startStop} → {leg.endStop}
              {leg.mode === 'BUS' && (
                <span style={{ marginLeft: 4, color: '#3b82f6', fontSize: 12 }}>({leg.routeName})</span>
              )}
            </td>
            <td style={{ padding: 6 }}>{leg.departure} – {leg.arrival}</td>
            <td style={{ padding: 6, textAlign: 'right' }}>{leg.distanceKm.toFixed(1)}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}
