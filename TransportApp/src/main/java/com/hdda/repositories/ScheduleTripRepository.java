/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hdda.repositories;

import com.hdda.pojo.ScheduleTrip;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author mahai
 */
public interface ScheduleTripRepository {

    List<ScheduleTrip> getTripsBySchedule(Long scheduleId);

    ScheduleTrip getTripById(Long id);

    ScheduleTrip addOrUpdateTrip(ScheduleTrip t);

    void deleteTrip(Long id);

    Optional<LocalDateTime> nextDeparture(Long scheduleId, LocalDate date, LocalTime after);

    List<ScheduleTrip> findByRouteVariantAndDate(Long variantId, LocalDate date);

}
