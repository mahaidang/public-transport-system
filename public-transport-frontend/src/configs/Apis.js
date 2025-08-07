import axios from "axios";
import cookie from 'react-cookies';

const BASE_URL = 'http://localhost:8080/TransportApp/api';

export const endpoints = {
  optimizeRoute: "/routes/optimize",
  login: "/login",
  profile: "/secure/profile",
  routes: "/routes",
  variants: "/variants",
  favorites: "/secure/favorites",
  reports: "/secure/reports",
};

export const authApis = () => axios.create({
  baseURL: BASE_URL,
  headers: {
    Authorization: `Bearer ${cookie.load('token')}`
  }
});

export default axios.create({
  baseURL: BASE_URL
});
