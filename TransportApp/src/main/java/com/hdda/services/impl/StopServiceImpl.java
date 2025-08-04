/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hdda.services.impl;

import com.hdda.pojo.Stop;
import com.hdda.repositories.StopRepository;
import com.hdda.services.StopService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author mahai
 */
@Service
public class StopServiceImpl implements StopService{
    @Autowired
    private StopRepository stopRepository;

    @Override
    public List<Stop> getStops(Map<String, String> params) {
        return stopRepository.getStops(params);
    }

    @Override
    public Stop getStopById(Long id) {
        return stopRepository.getStopById(id);
    }

    @Override
    public Stop addOrUpdateStop(Stop stop) {
        return stopRepository.addOrUpdateStop(stop);
    }

    @Override
    public void deleteStop(Long id) {
        stopRepository.deleteStop(id);
    }

    @Override
    public long countStops(Map<String, String> params) {
        return stopRepository.countStops(params);
    }
}
