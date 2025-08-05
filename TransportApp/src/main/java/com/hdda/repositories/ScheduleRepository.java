package com.hdda.repositories;

import com.hdda.pojo.Schedule;
import java.util.List;
import java.util.Map;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
/**
 *
 * @author mahai
 */
public interface ScheduleRepository {
    List<Schedule> getSchedules(Map<String,String> params);
    Schedule       getScheduleById(Long id);
    Schedule       addOrUpdateSchedule(Schedule s);
    void           deleteSchedule(Long id);
    long           countSchedules(Map<String,String> params);
}
