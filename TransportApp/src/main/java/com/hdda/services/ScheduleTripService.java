/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hdda.services;

import com.hdda.dto.base.ScheduleTripBaseDTO;
import com.hdda.dto.request.ScheduleTripRequestDTO;
import java.util.List;

/**
 *
 * @author mahai
 */
public interface ScheduleTripService {
    List<ScheduleTripBaseDTO>          getTripsBySchedule(Long scheduleId);
    ScheduleTripRequestDTO      getTripDTO(Long id);
    void                        addOrUpdate(ScheduleTripRequestDTO d);
    void                        delete(Long id);
}

