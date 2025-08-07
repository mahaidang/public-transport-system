/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hdda.controllers;

import com.hdda.services.TrafficReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author mahai
 */
@Controller
@RequestMapping("/reports")
public class TrafficReportController {
    @Autowired
    private TrafficReportService trafficReportService;

    @GetMapping
    public String listReports(Model model) {
        model.addAttribute("reports", trafficReportService.getAllReports());
        return "reports/list";
    }

    @GetMapping("/update-status")
    public String updateStatus(@RequestParam("id") Long id,
                               @RequestParam("action") String action) {
        if ("approve".equalsIgnoreCase(action)) {
            trafficReportService.updateReportStatus(id, "APPROVED");
        } else if ("reject".equalsIgnoreCase(action)) {
            trafficReportService.updateReportStatus(id, "REJECTED");
        }
        return "redirect:/reports";
    }
}
