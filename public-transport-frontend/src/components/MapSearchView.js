import React, { useState } from "react";
import axios from "axios";
import Apis, { endpoints } from "../configs/Apis";
import MapView from "./MapView";

/* ---------- geocode OSM (Nominatim) ---------- */
const geocode = async (text) => {
  const url = `https://nominatim.openstreetmap.org/search?q=${encodeURIComponent(
    text
  )}&format=json&limit=1`;
  const res = await axios.get(url);
  if (!res.data.length) throw new Error("Không tìm thấy: " + text);
  return { lat: +res.data[0].lat, lng: +res.data[0].lon };
};

/* ---------- OSRM ---------- */
const osrm = async (a, b) => {
  const url = `https://router.project-osrm.org/route/v1/driving/${a.lng},${a.lat};${b.lng},${b.lat}?overview=full&geometries=geojson`;
  const { data } = await axios.get(url);
  return data.routes[0].geometry.coordinates.map(([lng, lat]) => [lat, lng]);
};

/* ---------- build req ---------- */
const buildReq = ({ start, end, sortBy }) => {
  const now = new Date();
  return {
    start,
    end,
    travelDate: now.toISOString().slice(0, 10),
    earliestDepart: now.toTimeString().slice(0, 8),
    sortBy: sortBy || "TIME",
    order: "ASC",
  };
};

/* ---------- component ---------- */
export default function MapSearchView() {
  const [fromTxt, setFromTxt] = useState("");
  const [toTxt, setToTxt] = useState("");
  const [sort, setSort] = useState("");
  const [itinerary, setItinerary] = useState(null);
  const [loading, setLoading] = useState(false);
  const [err, setErr] = useState("");

  const handleSearch = async () => {
    try {
      setErr("");
      setLoading(true);

      // 1. geocode
      const start = await geocode(fromTxt);
      const end = await geocode(toTxt);

      // 2. gọi BE /routes/optimize
      const req = buildReq({ start, end, sortBy: sort });
      const { data } = await Apis.post(endpoints.optimizeRoute, req);
      if (!data.length) throw new Error("Không có lộ trình.");

      // 3. OSRM cho từng leg
      const raw = data[0];
      const legs = await Promise.all(
        raw.legs.map(async (leg) => {
          const a = { lat: leg.polyline[0][0], lng: leg.polyline[0][1] };
          const b = {
            lat: leg.polyline.at(-1)[0],
            lng: leg.polyline.at(-1)[1],
          };
          const poly = await osrm(a, b);
          return { ...leg, polyline: poly };
        })
      );

      setItinerary({ ...raw, legs });
    } catch (e) {
      setErr(e.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="space-y-4">
      {/* form */}
      <div className="flex gap-2">
        <input
          className="border p-2 rounded grow"
          placeholder="Điểm đi"
          value={fromTxt}
          onChange={(e) => setFromTxt(e.target.value)}
        />
        <input
          className="border p-2 rounded grow"
          placeholder="Điểm đến"
          value={toTxt}
          onChange={(e) => setToTxt(e.target.value)}
        />
        <select
          className="border p-2 rounded"
          value={sort}
          onChange={(e) => setSort(e.target.value)}
        >
          <option value="">TIME (mặc định)</option>
          <option value="DISTANCE">DISTANCE</option>
          <option value="TRANSFERS">TRANSFERS</option>
        </select>
        <button
          className="bg-blue-600 text-white px-4 rounded"
          onClick={handleSearch}
          disabled={loading}
        >
          {loading ? "..." : "Tìm"}
        </button>
      </div>
      {err && <div className="text-red-600">{err}</div>}

      {/* hiển thị map */}
      <MapView itinerary={itinerary} />
    </div>
  );
}
