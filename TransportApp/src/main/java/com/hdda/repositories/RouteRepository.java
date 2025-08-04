/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hdda.repositories;

import com.hdda.pojo.Route;
import com.hdda.pojo.RouteVariant;
import java.util.List;
import java.util.Map;

/**
 *
 * @author mahai
 */
public interface RouteRepository {
    List<Route> getRoutes(Map<String, String> params);
    Route getRouteById(Long id);
    Route addOrUpdateRoute(Route r);
    void deleteRoute(Long id);
    List<RouteVariant> getVariants(Long id);
    long countRoutes(Map<String, String> params);
    List<RouteVariant> getVariantsByRouteId(Long routeId);

}
