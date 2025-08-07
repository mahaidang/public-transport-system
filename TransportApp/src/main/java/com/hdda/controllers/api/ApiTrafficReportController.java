/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hdda.controllers.api;

import com.hdda.dto.request.TrafficReportRequestDTO;
import com.hdda.pojo.TrafficReport;
import com.hdda.services.TrafficReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author mahai
 */
@RestController
@RequestMapping("/api/secure/reports")
@CrossOrigin
public class ApiTrafficReportController {

    @Autowired
    private TrafficReportService trafficReportService;

    @PostMapping
    public ResponseEntity<?> createReport(@RequestBody TrafficReportRequestDTO req) {
        TrafficReport saved = trafficReportService.saveReport(req);
        return ResponseEntity.ok("Đã gửi báo cáo thành công! ID: " + saved.getId());
    }
}