import { onValue, ref } from "firebase/database";
import { useEffect, useState } from "react";
import { db } from '../configs/firebaseConfig';

export default function useTrafficReports() {
  const [reports, setReports] = useState([]);

  useEffect(() => {
    const rRef = ref(db, "reports");
    const unsub = onValue(rRef, (snap) => {
      const raw = snap.val() || {};
      setReports(Object.values(raw));
    });
    return () => unsub();
  }, []);

  return reports;
}
