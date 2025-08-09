/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hdda.dto.detail;

import com.hdda.enums.TravelMode;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 *
 * @author mahai
 */
@Data
@Builder
public class ItineraryLegDTO {
    private TravelMode mode;       // WALK hoặc BUS
    private String      routeName; // chỉ dùng khi BUS
    private String      startStop; // tên trạm hoặc “Walking start”
    private String      endStop;   // tên trạm hoặc “Walking end”
    private LocalTime   departure;
    private LocalTime   arrival;
    private double      distanceKm;
    private List<double[]> polyline;
}