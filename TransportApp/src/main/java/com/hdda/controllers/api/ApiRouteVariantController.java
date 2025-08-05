/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hdda.controllers.api;


import com.hdda.dto.detail.RouteVariantDetailDTO;
import com.hdda.services.RouteVariantService;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author mahai
 */
@RestController
@RequestMapping("/api")
public class ApiRouteVariantController {

    @Autowired
    private RouteVariantService routeVariantService;

    /**
     * API: Lấy chi tiết 1 chặng theo ID và ngày
     * Ví dụ: GET /api/variants/7?date=2025-08-07
     */
    @GetMapping("/variants/{id}")
    public ResponseEntity<RouteVariantDetailDTO> getRouteVariantDetail(
            @PathVariable("id") Long id,
            @RequestParam(value = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        if (date == null)
            date = LocalDate.now();

        RouteVariantDetailDTO dto = routeVariantService.getRouteVariantDetail(id, date);
        return ResponseEntity.ok(dto);
    }
}

