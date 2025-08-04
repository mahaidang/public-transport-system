/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hdda.services.impl;

import com.hdda.pojo.Route;
import com.hdda.pojo.RouteVariant;
import com.hdda.repositories.RouteRepository;
import com.hdda.services.RouteService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author mahai
 */
@Service
public class RouteServiceImpl implements RouteService {

    @Autowired
    private RouteRepository routeRepo;

    @Override
    public Route addOrUpdateRoute(Route r) {
        return this.routeRepo.addOrUpdateRoute(r);
    }

    @Override
    public List<Route> getRoutes(Map<String, String> params) {
        return this.routeRepo.getRoutes(params);
    }

    @Override
    public Route getRouteById(Long id) {
        return this.routeRepo.getRouteById(id);
    }

    @Override
    public void deleteRoute(Long id) {
        this.routeRepo.deleteRoute(id);
    }

    @Override
    public long countRoutes(Map<String, String> params) {
        return routeRepo.countRoutes(params);
    }

    @Override
    public List<RouteVariant> getVariantsByRouteId(Long routeId) {
        return this.routeRepo.getVariantsByRouteId(routeId);
    }
}
