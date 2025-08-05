///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.hdda.utils;
//
///**
// *
// * @author mahai
// */
//public class GeoUtils {
//
//    private static final int EARTH_RADIUS_KM = 6371;
//    private static final double WALK_SPEED_KMH = 5.0;
//
//    /**
//     * Tính khoảng cách giữa hai điểm bằng công thức Haversine (trả về km).
//     */
//    public static double haversineKm(double lat1, double lon1, double lat2, double lon2) {
//        double dLat = Math.toRadians(lat2 - lat1);
//        double dLon = Math.toRadians(lon2 - lon1);
//        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
//                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
//                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
//        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//        return EARTH_RADIUS_KM * c;
//    }
//
//    /**
//     * Tính thời gian đi bộ (phút) từ quãng đường (km), với vận tốc trung bình 5
//     * km/h.
//     */
//    public static long walkTimeMin(double distKm) {
//        return Math.round(distKm / WALK_SPEED_KMH * 60);
//    }
//}
