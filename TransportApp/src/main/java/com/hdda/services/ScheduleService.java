/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hdda.services;

import com.hdda.dto.base.ScheduleBaseDTO;
import com.hdda.dto.request.ScheduleRequestDTO;
import com.hdda.pojo.Schedule;
import java.util.List;
import java.util.Map;

/**
 *
 * @author mahai
 */
public interface ScheduleService {

    List<ScheduleBaseDTO> getSchedules(Map<String, String> params);

    Schedule getScheduleById(Long id);

    ScheduleRequestDTO addOrUpdateSchedule(ScheduleRequestDTO dto);

    void deleteSchedule(Long id);

    long countSchedules(Map<String, String> params);
}
