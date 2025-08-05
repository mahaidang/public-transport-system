/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hdda.services;

import com.hdda.dto.detail.RouteDetailDTO;
import com.hdda.pojo.Route;
import com.hdda.pojo.RouteVariant;
import java.util.List;
import java.util.Map;

/**
 *
 * @author mahai
 */
public interface RouteService {
    
    List<Route> getRoutes(Map<String, String> params);
    Route getRouteById(Long id);
    Route addOrUpdateRoute(Route r);
    void deleteRoute(Long id);   
    long countRoutes(Map<String, String> params);
    List<RouteVariant> getVariantsByRouteId(Long routeId);
    RouteDetailDTO getRouteDetail(Long id);
    
}
