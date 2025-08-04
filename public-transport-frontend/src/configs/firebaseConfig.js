// Import các chức năng cần thiết từ Firebase SDK
import { initializeApp } from "firebase/app";
import { getAnalytics } from "firebase/analytics";
import { getAuth, GoogleAuthProvider } from "firebase/auth";

// Cấu hình Firebase (từ Firebase Console)
const firebaseConfig = {
  apiKey: "AIzaSyDqgISt8TijzA41Ze_qSTrAqTgcsHCm1Ms",
  authDomain: "traffic-b0217.firebaseapp.com",
  projectId: "traffic-b0217",
  storageBucket: "traffic-b0217.firebasestorage.app",
  messagingSenderId: "490130152752",
  appId: "1:490130152752:web:c1feed76ccd86c3c563cf5",
  measurementId: "G-EZ5C6HJQ5L"
};

// Khởi tạo Firebase
const app = initializeApp(firebaseConfig);
const analytics = getAnalytics(app);

// Khởi tạo Firebase Authentication và Provider cho Google
const auth = getAuth(app);
const provider = new GoogleAuthProvider();

// Export để dùng ở component Login
export { auth, provider };
