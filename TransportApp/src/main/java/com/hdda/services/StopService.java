/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hdda.services;

import com.hdda.pojo.Stop;
import java.util.List;
import java.util.Map;

/**
 *
 * @author mahai
 */
public interface StopService {

    List<Stop> getStops(Map<String, String> params);

    Stop getStopById(Long id);

    Stop addOrUpdateStop(Stop r);

    void deleteStop(Long id);

    long countStops(Map<String, String> params);

}
