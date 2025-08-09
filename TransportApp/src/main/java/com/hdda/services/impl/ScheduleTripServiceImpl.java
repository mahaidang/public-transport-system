/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hdda.services.impl;

import com.hdda.dto.base.ScheduleTripBaseDTO;
import com.hdda.dto.request.ScheduleTripRequestDTO;
import com.hdda.pojo.Schedule;
import com.hdda.pojo.ScheduleTrip;
import com.hdda.repositories.ScheduleRepository;
import com.hdda.repositories.ScheduleTripRepository;
import com.hdda.services.ScheduleTripService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author mahai
 */
@Service
public class ScheduleTripServiceImpl implements ScheduleTripService {
    
    @Autowired
    private ScheduleTripRepository tripRepo;
    @Autowired
    private ScheduleRepository scheduleRepo;
    
    @Override
    public List<ScheduleTripBaseDTO> getTripsBySchedule(Long scheduleId) {
        List<ScheduleTrip> entities = tripRepo.getTripsBySchedule(scheduleId);        
        return entities.stream().map(s -> mapToBDTO(s)).collect(Collectors.toList());
    }
    
    @Override
    public ScheduleTripRequestDTO getTripDTO(Long id) {
        ScheduleTrip t = tripRepo.getTripById(id);
        ScheduleTripRequestDTO d = new ScheduleTripRequestDTO();
        d.setId(t.getId());
        d.setDepartTime(t.getDepartTime());
        d.setArriveTime(t.getArriveTime());
        d.setScheduleId(t.getSchedule().getId());
        return d;
    }
    
    @Override
    public void addOrUpdate(ScheduleTripRequestDTO d) {
        ScheduleTrip t = new ScheduleTrip();
        t.setId(d.getId());                       // quan trọng để UPDATE
        t.setDepartTime(d.getDepartTime());       // :contentReference[oaicite:4]{index=4}
        t.setArriveTime(d.getArriveTime());       // :contentReference[oaicite:5]{index=5}
        t.setSchedule(scheduleRepo.getScheduleById(d.getScheduleId()));
        tripRepo.addOrUpdateTrip(t);
    }
    
    @Override
    public void delete(Long id) {
        tripRepo.deleteTrip(id);
    }
    
    private ScheduleTripBaseDTO mapToBDTO(ScheduleTrip s) {
        ScheduleTripBaseDTO dto = new ScheduleTripBaseDTO();
        dto.setId(s.getId());
        dto.setName(s.getSchedule().getVariant().getStartStop().getName() + " - " + s.getSchedule().getVariant().getEndStop().getName());
        dto.setArriveTime(s.getArriveTime());
        dto.setDepartTime(s.getDepartTime());
        return dto;
    }
}
