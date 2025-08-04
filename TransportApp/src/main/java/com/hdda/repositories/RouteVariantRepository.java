/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hdda.repositories;

import com.hdda.pojo.RouteVariant;
import java.util.List;
import java.util.Map;

/**
 *
 * @author mahai
 */
public interface RouteVariantRepository {

    List<RouteVariant> getRouteVariants(Map<String, String> params);

    RouteVariant getRouteVariantById(Long id);

    RouteVariant addOrUpdateRouteVariant(RouteVariant r);

    void deleteRouteVariant(Long id);

    List<RouteVariant> byStartAndEnd(Long startStopId, Long endStopId);

    List<RouteVariant> startingAt(Long startStopId);

    List<RouteVariant> endingAt(Long endStopId);
}
