/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hdda.services;

import com.hdda.dto.base.RouteVariantBaseDTO;
import com.hdda.dto.detail.RouteVariantDetailDTO;
import com.hdda.dto.request.RouteVariantRequestDTO;
import com.hdda.pojo.RouteVariant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 *
 * @author mahai
 */
public interface RouteVariantService {

    List<RouteVariantBaseDTO> getRouteVariants(Map<String, String> params);

    RouteVariant getRouteVariantById(Long id);

    RouteVariantRequestDTO addOrUpdateRouteVariant(RouteVariantRequestDTO r);

    void deleteRouteVariant(Long id);
    
    RouteVariantDetailDTO getRouteVariantDetail(Long id, LocalDate date);

}
