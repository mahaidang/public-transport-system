/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hdda.services.impl;

import com.hdda.dto.detail.ItineraryDTO;
import com.hdda.dto.detail.ItineraryLegDTO;
import com.hdda.dto.request.OptimizeRouteRequestDTO;
import com.hdda.enums.SortField;
import com.hdda.enums.SortOrder;
import com.hdda.enums.TravelMode;
import com.hdda.pojo.RouteVariant;
import com.hdda.pojo.Stop;
import com.hdda.repositories.RouteVariantRepository;
import com.hdda.repositories.ScheduleTripRepository;
import com.hdda.repositories.StopRepository;
import com.hdda.services.RouteOptimizeService;
import com.hdda.utils.GeoUtils;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author mahai
 */
@Service
public class RouteOptimizeServiceImpl implements RouteOptimizeService {

    @Autowired
    private StopRepository stopRepo;

    @Autowired
    private RouteVariantRepository rvRepo;

    @Autowired
    private ScheduleTripRepository tripRepo;

    @Override
    @Transactional(readOnly = true)
    public List<ItineraryDTO> optimize(OptimizeRouteRequestDTO req) {
        Stop start = stopRepo.findNearest(req.getStart().getLat(), req.getStart().getLng());
        Stop end = stopRepo.findNearest(req.getEnd().getLat(), req.getEnd().getLng());

        List<ItineraryDTO> plans = findDirect(start, end, req);
        if (plans.isEmpty()) {
            plans = findTwoLeg(start, end, req);
        }

        Comparator<ItineraryDTO> cmp;
        if (req.getSortBy() == SortField.DISTANCE) {
            cmp = Comparator.comparing(ItineraryDTO::getTotalDistanceKm);
        } else if (req.getSortBy() == SortField.TRANSFERS) {
            cmp = Comparator.comparing(ItineraryDTO::getTransfers);
        } else {
            cmp = Comparator.comparing(ItineraryDTO::getTotalMinutes);
        }
        if (req.getOrder() == SortOrder.DESC) {
            cmp = cmp.reversed();
        }
        return plans.stream().sorted(cmp).collect(Collectors.toList());
    }

    private List<ItineraryDTO> findDirect(Stop start, Stop end, OptimizeRouteRequestDTO req) {
        List<RouteVariant> variants = rvRepo.byStartAndEnd(start.getId(), end.getId());
        return variants.stream()
                .map(rv -> buildItinerary(List.of(rv), start, end, req))
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    private List<ItineraryDTO> findTwoLeg(Stop start, Stop end, OptimizeRouteRequestDTO req) {
        List<RouteVariant> fromStart = rvRepo.startingAt(start.getId());
        List<RouteVariant> toEnd = rvRepo.endingAt(end.getId());
        List<ItineraryDTO> result = new ArrayList<>();

        for (RouteVariant rv1 : fromStart) {
            for (RouteVariant rv2 : toEnd) {
                if (!rv1.getEndStop().getId().equals(rv2.getStartStop().getId())) {
                    continue;
                }
                buildItinerary(List.of(rv1, rv2), start, end, req).ifPresent(result::add);
            }
        }
        return result;
    }

    private Optional<ItineraryDTO> buildItinerary(List<RouteVariant> legs, Stop start, Stop end, OptimizeRouteRequestDTO req) {
        List<ItineraryLegDTO> legDTOs = new ArrayList<>();
        LocalTime cursor = req.getEarliestDepart();
        double totalDist = 0;
        long totalMin = 0;
        Stop current = start;

        for (RouteVariant rv : legs) {
            // Walk to bus start
            double walkKm = GeoUtils.haversineKm(
                    current.getLatitude(), current.getLongitude(),
                    rv.getStartStop().getLatitude(), rv.getStartStop().getLongitude());
            long walkMin = GeoUtils.walkTimeMin(walkKm);
            LocalTime walkArr = cursor.plusMinutes(walkMin);
            legDTOs.add(ItineraryLegDTO.builder()
                    .mode(TravelMode.WALK)
                    .startStop("Điểm xuất phát")
                    .endStop(rv.getStartStop().getName())
                    .departure(cursor)
                    .arrival(walkArr)
                    .distanceKm(walkKm)
                    .polyline(List.of(
                            new double[]{current.getLatitude(), current.getLongitude()},
                            new double[]{rv.getStartStop().getLatitude(), rv.getStartStop().getLongitude()}
                    )).build());
            totalDist += walkKm;
            totalMin += walkMin;
            cursor = walkArr;
            current = rv.getStartStop();

            // Bus leg
            Optional<LocalDateTime> ldt = tripRepo.nextDeparture(
                    rv.getSchedules().get(0).getId(), req.getTravelDate(), cursor);
            Optional<LocalTime> depOpt = ldt.map(LocalDateTime::toLocalTime);

            if (depOpt.isEmpty()) {
                return Optional.empty();
            }
            LocalTime dep = depOpt.get();
            long waitMin = Duration.between(cursor, dep).toMinutes();
            totalMin += waitMin;
            cursor = dep;
            LocalTime arr = dep.plusMinutes(rv.getTimeMinute());
            legDTOs.add(ItineraryLegDTO.builder()
                    .mode(TravelMode.BUS)
                    .routeName(rv.getRoute().getName())
                    .startStop(rv.getStartStop().getName())
                    .endStop(rv.getEndStop().getName())
                    .departure(dep)
                    .arrival(arr)
                    .distanceKm(rv.getDistanceKm())
                    .polyline(List.of(
                            new double[]{rv.getStartStop().getLatitude(), rv.getStartStop().getLongitude()},
                            new double[]{rv.getEndStop().getLatitude(), rv.getEndStop().getLongitude()}
                    )).build());
            totalDist += rv.getDistanceKm();
            totalMin += rv.getTimeMinute();
            cursor = arr;
            current = rv.getEndStop();
        }

        // Walk from last stop to destination
        double lastWalkKm = GeoUtils.haversineKm(
                current.getLatitude(), current.getLongitude(),
                end.getLatitude(), end.getLongitude());
        long lastWalkMin = GeoUtils.walkTimeMin(lastWalkKm);
        LocalTime finalArr = cursor.plusMinutes(lastWalkMin);
        legDTOs.add(ItineraryLegDTO.builder()
                .mode(TravelMode.WALK)
                .startStop(current.getName())
                .endStop("Điểm đến")
                .departure(cursor)
                .arrival(finalArr)
                .distanceKm(lastWalkKm)
                .polyline(List.of(
                        new double[]{current.getLatitude(), current.getLongitude()},
                        new double[]{end.getLatitude(), end.getLongitude()}
                )).build());
        totalDist += lastWalkKm;
        totalMin += lastWalkMin;

        return Optional.of(ItineraryDTO.builder()
                .legs(legDTOs)
                .transferStops(legs.size() == 2
                        ? List.of(legs.get(0).getEndStop().getName()) : List.of())
                .transfers(legs.size() - 1)
                .totalDistanceKm(totalDist)
                .totalMinutes(totalMin)
                .build());
    }
}
