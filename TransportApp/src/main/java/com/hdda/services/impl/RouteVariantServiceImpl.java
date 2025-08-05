/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hdda.services.impl;

import com.hdda.dto.base.RouteVariantBaseDTO;
import com.hdda.dto.base.ScheduleTripBaseDTO;
import com.hdda.dto.detail.RouteVariantDetailDTO;
import com.hdda.dto.request.RouteVariantRequestDTO;
import com.hdda.pojo.RouteVariant;
import com.hdda.pojo.ScheduleTrip;
import com.hdda.repositories.RouteRepository;
import com.hdda.repositories.RouteVariantRepository;
import com.hdda.repositories.ScheduleTripRepository;
import com.hdda.repositories.StopRepository;
import com.hdda.services.RouteVariantService;
import jakarta.ws.rs.NotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author mahai
 */
@Service
public class RouteVariantServiceImpl implements RouteVariantService {

    @Autowired
    private RouteVariantRepository routeVariantRepo;

    @Autowired
    private RouteRepository routeRepo;

    @Autowired
    private StopRepository stopRepo;

    @Autowired
    private ScheduleTripRepository tripRepo;

    @Override
    public List<RouteVariantBaseDTO> getRouteVariants(Map<String, String> params) {
        List<RouteVariant> entities = this.routeVariantRepo.getRouteVariants(params);
        return entities.stream().map(rv -> mapToBDTO(rv)).collect(Collectors.toList());
    }

    @Override
    public RouteVariant getRouteVariantById(Long id) {
        RouteVariant rv = this.routeVariantRepo.getRouteVariantById(id);
        return rv;
    }

    @Override
    public RouteVariantRequestDTO addOrUpdateRouteVariant(RouteVariantRequestDTO r) {
        RouteVariant rv = new RouteVariant();
        rv.setId(r.getId());
        rv.setDistanceKm(r.getDistanceKm());
        rv.setTimeMinute(r.getTimeMinute());
        rv.setSeq(r.getSeq());
        rv.setRoute(routeRepo.getRouteById(r.getRouteId()));
        rv.setStartStop(stopRepo.getStopById(r.getStartId()));
        rv.setEndStop(stopRepo.getStopById(r.getEndId()));
        RouteVariant saved = routeVariantRepo.addOrUpdateRouteVariant(rv);
        return mapToRDTO(saved);
    }

    @Override
    public void deleteRouteVariant(Long id) {
        this.routeVariantRepo.deleteRouteVariant(id);
    }

    @Override
    public RouteVariantDetailDTO getRouteVariantDetail(Long id, LocalDate date) {
        RouteVariant variant = routeVariantRepo.getDetailVariant(id);

        List<ScheduleTrip> trips = tripRepo.findByRouteVariantAndDate(id, date);

        List<ScheduleTripBaseDTO> tripDTOs = trips.stream()
                .map(t -> {
                    LocalDateTime depart = t.getDepartTime();
                    LocalDateTime arrive = t.getArriveTime();

                    return new ScheduleTripBaseDTO(null, null, depart, arrive);
                }).toList();

        String name = variant.getStartStop().getName() + " → " + variant.getEndStop().getName();

        RouteVariantDetailDTO dto = new RouteVariantDetailDTO();
        dto.setId(variant.getId());
        dto.setName(name);
        dto.setSeq(variant.getSeq());
        dto.setDistanceKm(variant.getDistanceKm());
        dto.setTimeMinute(variant.getTimeMinute());
        dto.setScheduleTrips(tripDTOs);

        return dto;
    }

    private RouteVariantBaseDTO mapToBDTO(RouteVariant rv) {
        RouteVariantBaseDTO dto = new RouteVariantBaseDTO();
        dto.setId(rv.getId());
        dto.setSeq(rv.getSeq());
        dto.setName(rv.getStartStop().getName() + " - " + rv.getEndStop().getName());

        return dto;
    }

    private RouteVariantRequestDTO mapToRDTO(RouteVariant rv) {
        RouteVariantRequestDTO dto = new RouteVariantRequestDTO();
        dto.setId(rv.getId());
        dto.setDistanceKm(rv.getDistanceKm());
        dto.setTimeMinute(rv.getTimeMinute());
        dto.setSeq(rv.getSeq());
        dto.setRouteId(rv.getRoute().getId());
        dto.setStartId(rv.getStartStop().getId());
        dto.setEndId(rv.getEndStop().getId());
        return dto;
    }

//    private RouteVariantDetailDTO mapToDDTO(RouteVariant rv) {
//        RouteVariantDetailDTO dto = new RouteVariantDetailDTO();
//        dto.setId(rv.getId());
//        dto.setDistanceKm(rv.getDistanceKm());
//        dto.setTimeMinute(rv.getTimeMinute());
//        dto.setSeq(rv.getSeq());
//        dto.setRouteL(new RouteLightDTO(rv.getRoute().getId(), rv.getRoute().getCode(), rv.getRoute().getName()));
//        dto.setStartStop(new StopLightDTO(rv.getStartStop().getId(), rv.getStartStop().getCode(), rv.getStartStop().getName()));
//        dto.setEndStop(new StopLightDTO(rv.getEndStop().getId(), rv.getEndStop().getCode(), rv.getEndStop().getName()));
//        return dto;
//    }
}
