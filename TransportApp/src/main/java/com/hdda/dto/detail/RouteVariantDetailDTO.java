/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hdda.dto.detail;

import com.hdda.dto.base.ScheduleTripBaseDTO;
import java.util.List;
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
    private String name;
    private int seq;
    private double distanceKm;
    private int timeMinute;
    private List<ScheduleTripBaseDTO> scheduleTrips;

}
