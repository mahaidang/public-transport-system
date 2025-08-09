/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hdda.services.impl;

import com.hdda.dto.base.RouteVariantBaseDTO;
import com.hdda.dto.detail.RouteDetailDTO;
import com.hdda.pojo.Route;
import com.hdda.pojo.RouteVariant;
import com.hdda.repositories.RouteRepository;
import com.hdda.services.RouteService;
import jakarta.ws.rs.NotFoundException;
import java.util.Comparator;
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

    @Override
    public RouteDetailDTO getRouteDetail(Long id) {
        Route r = routeRepo.detailRoute(id);

        List<RouteVariantBaseDTO> variants = r.getRouteVariantSet().stream()
                .sorted(Comparator.comparingInt(RouteVariant::getSeq))
                .map(v -> new RouteVariantBaseDTO(
                v.getId(),
                v.getSeq(),
                v.getStartStop().getName() + " - " + v.getEndStop().getName())).toList();

        return new RouteDetailDTO(
                r.getId(),
                r.getCode(),
                r.getName(),
                r.getDescription(),
                r.getIsActive(),
                variants
        );
    }

}
