/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hdda.services;

import com.hdda.dto.detail.ItineraryDTO;
import com.hdda.dto.request.OptimizeRouteRequestDTO;
import java.util.List;

/**
 *
 * @author mahai
 */
public interface RouteOptimizeService {

    List<ItineraryDTO> optimize(OptimizeRouteRequestDTO req);

}
