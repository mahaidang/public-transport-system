/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hdda.services;

import com.hdda.dto.request.TrafficReportRequestDTO;
import com.hdda.pojo.TrafficReport;
import java.util.List;

/**
 *
 * @author mahai
 */
public interface TrafficReportService {
    List<TrafficReport> getAllReports();
    void updateReportStatus(Long reportId, String status);
    TrafficReport getReportById(Long id);
    TrafficReport saveReport(TrafficReportRequestDTO req);

}
