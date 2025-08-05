/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hdda.dto.detail;

/**
 *
 * @author mahai
 */
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItineraryDTO {
    private List<ItineraryLegDTO> legs;         // 1 hoặc 2 chặng
    private List<String>          transferStops;// tên các trạm transfer
    private int                   transfers;    // số lần chuyển = legs.size()-1
    private double                totalDistanceKm;
    private long                  totalMinutes; // tổng thời gian (đi + chờ)
}
