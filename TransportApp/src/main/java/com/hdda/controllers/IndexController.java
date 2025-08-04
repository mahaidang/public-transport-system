/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hdda.controllers;

import com.hdda.services.RouteService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author mahai
 */
@Controller
@ControllerAdvice
public class IndexController {

    @Autowired
    private RouteService routeService;

    /* Trang chủ hiển thị list Route */
    @GetMapping("/")
    public String listRoutes(Model model) {
        model.addAttribute("routes", routeService.getRoutes(Map.of()));
        return "index";        // templates/index.html
    }
}
