/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hdda.repositories;

import com.hdda.pojo.TrafficReport;
import java.util.List;

/**
 *
 * @author mahai
 */
public interface TrafficReportRepository {

    List<TrafficReport> getAllReports();

    TrafficReport save(TrafficReport r);
    
    TrafficReport getReportById(Long id);

}
