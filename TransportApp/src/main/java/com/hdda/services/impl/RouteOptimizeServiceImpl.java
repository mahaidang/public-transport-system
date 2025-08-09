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
import com.hdda.services.external.GoongService;
//import com.hdda.utils.GeoUtils;
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

    @Autowired
    private GoongService goongService;

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

    private Optional<ItineraryDTO> buildItinerary(List<RouteVariant> legs,
            Stop start, Stop end,
            OptimizeRouteRequestDTO req) {

        /* ▸ 1. Toạ độ người dùng chọn trên bản đồ */
        double originLat = req.getStart().getLat();
        double originLng = req.getStart().getLng();
        double destLat = req.getEnd().getLat();
        double destLng = req.getEnd().getLng();

        /* ▸ 2. Khai báo biến tích luỹ */
        List<ItineraryLegDTO> legDTOs = new ArrayList<>();
        LocalTime cursor = req.getEarliestDepart();
        double totalDist = 0;
        long totalMin = 0;
        Stop current = start;

        /* ▸ 3. Duyệt từng RouteVariant (BUS leg) */
        for (RouteVariant rv : legs) {

            /* ---------- WALK: người dùng → trạm BUS đầu ---------- */
            double walkKm = goongService.getWalkingDistanceKm(
                    originLat, originLng,
                    rv.getStartStop().getLatitude(), rv.getStartStop().getLongitude());
            long walkMin = Math.round(walkKm * 12);          // tuỳ ý đổi sang duration từ Goong
            LocalTime walkArr = cursor.plusMinutes(walkMin);

            legDTOs.add(ItineraryLegDTO.builder()
                    .mode(TravelMode.WALK)
                    .startStop("Điểm xuất phát")
                    .endStop(rv.getStartStop().getName())
                    .departure(cursor)
                    .arrival(walkArr)
                    .distanceKm(walkKm)
                    .polyline(List.of(
                            new double[]{originLat, originLng},
                            new double[]{rv.getStartStop().getLatitude(),
                                rv.getStartStop().getLongitude()}
                    ))
                    .build());

            totalDist += walkKm;
            totalMin += walkMin;
            cursor = walkArr;          // cập nhật thời gian hiện tại
            current = rv.getStartStop();

            /* ---------- BUS leg ---------- */
            Optional<LocalDateTime> ldt = tripRepo.nextDeparture(
                    rv.getSchedules().get(0).getId(),
                    req.getTravelDate(),
                    cursor);

            if (ldt.isEmpty()) {
                return Optional.empty();
            }

            LocalTime dep = ldt.get().toLocalTime();
            long waitMin = Duration.between(cursor, dep).toMinutes();
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
                            new double[]{rv.getStartStop().getLatitude(),
                                rv.getStartStop().getLongitude()},
                            new double[]{rv.getEndStop().getLatitude(),
                                rv.getEndStop().getLongitude()}
                    ))
                    .build());

            totalDist += rv.getDistanceKm();
            totalMin += waitMin + rv.getTimeMinute();
            cursor = arr;
            current = rv.getEndStop();

            /* Sau BUS, lần lặp kế tiếp (nếu có) sẽ tính WALK chuyển tuyến */
            originLat = current.getLatitude();   // cập nhật điểm xuất phát cho WALK kế tiếp
            originLng = current.getLongitude();
        }

        /* ---------- WALK cuối: trạm BUS cuối → điểm đến thật ---------- */
        double lastWalkKm = goongService.getWalkingDistanceKm(
                current.getLatitude(), current.getLongitude(),
                destLat, destLng);
        long lastWalkMin = Math.round(lastWalkKm * 12);
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
                        new double[]{destLat, destLng}
                ))
                .build());

        totalDist += lastWalkKm;
        totalMin += lastWalkMin;

        /* ---------- Trả về kết quả ---------- */
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
