/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hdda.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import com.hdda.enums.SortField;
import com.hdda.enums.SortOrder;
import lombok.Data;

/**
 *
 * @author mahai
 */
@Data
public class OptimizeRouteRequestDTO {

    private Coordinate start;
    private Coordinate end;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate travelDate = LocalDate.now();

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime earliestDepart = LocalTime.now();
    private SortField sortBy = SortField.TIME;     // TIME, DISTANCE, TRANSFERS
    private SortOrder order = SortOrder.ASC;      // ASC, DESC

    @Data
    public static class Coordinate {

        private double lat;
        private double lng;
    }
}
