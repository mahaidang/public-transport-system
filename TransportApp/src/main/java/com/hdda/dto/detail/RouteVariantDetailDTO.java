/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hdda.dto.detail;

import com.hdda.dto.light.RouteLightDTO;
import com.hdda.dto.light.StopLightDTO;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author mahai
 */
@Getter
@Setter
public class RouteVariantDetailDTO {

    private Long id;

    private double distanceKm;

    private int timeMinute;

    private int seq;

    private StopLightDTO startStop;

    private StopLightDTO endStop;

    private RouteLightDTO routeL;
}
