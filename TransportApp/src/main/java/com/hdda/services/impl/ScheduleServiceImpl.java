/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hdda.services.impl;

import com.hdda.dto.base.ScheduleBaseDTO;
import com.hdda.dto.request.ScheduleRequestDTO;
import com.hdda.pojo.Schedule;
import com.hdda.repositories.RouteVariantRepository;
import com.hdda.repositories.ScheduleRepository;
import com.hdda.services.ScheduleService;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author mahai
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired private ScheduleRepository      scheduleRepo;
    @Autowired private RouteVariantRepository  variantRepo;

    @Override
    public List<ScheduleBaseDTO> getSchedules(Map<String, String> params) {
        List<Schedule> entities = scheduleRepo.getSchedules(params);
        return entities.stream().map(s -> mapToBDTO(s)).collect(Collectors.toList());
    }

    @Override
    public Schedule getScheduleById(Long id) {
        return scheduleRepo.getScheduleById(id);
    }

    @Override
    public ScheduleRequestDTO addOrUpdateSchedule(ScheduleRequestDTO d) {
        Schedule s = new Schedule();
        s.setId(d.getId());
        s.setDayGroup(d.getDayGroup());
        s.setEffectiveFrom(d.getEffectiveFrom());
        s.setEffectiveTo(d.getEffectiveTo());
        s.setNote(d.getNote());
        s.setVariant(variantRepo.getRouteVariantById(d.getVariantId()));

        Schedule saved = scheduleRepo.addOrUpdateSchedule(s);

        /* Map ngược lại DTO trả ra */
        ScheduleRequestDTO res = new ScheduleRequestDTO();
        res.setId(saved.getId());
        res.setDayGroup(saved.getDayGroup());
        res.setEffectiveFrom(saved.getEffectiveFrom());
        res.setEffectiveTo(saved.getEffectiveTo());
        res.setNote(saved.getNote());
        res.setVariantId(saved.getVariant().getId());

        return res;
    }

    @Override
    public void deleteSchedule(Long id) {
        scheduleRepo.deleteSchedule(id);
    }

    @Override
    public long countSchedules(Map<String, String> params) {
        return scheduleRepo.countSchedules(params);
    }
    
    private ScheduleBaseDTO mapToBDTO(Schedule s) {
        ScheduleBaseDTO dto = new ScheduleBaseDTO();
        dto.setId(s.getId());
        dto.setName(s.getVariant().getStartStop().getName() + " - " + s.getVariant().getEndStop().getName());
        dto.setDayGroup(s.getDayGroup());
        dto.setEffectiveFrom(s.getEffectiveFrom());
        dto.setEffectiveTo(s.getEffectiveFrom());
        return dto;
    }

}