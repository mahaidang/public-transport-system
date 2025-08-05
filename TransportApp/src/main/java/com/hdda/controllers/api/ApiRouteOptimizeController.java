/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hdda.controllers.api;

import com.hdda.dto.detail.ItineraryDTO;
import com.hdda.dto.request.OptimizeRouteRequestDTO;
import com.hdda.services.RouteOptimizeService;
import java.util.List;
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
@RequestMapping(path = "/api/routes")
@CrossOrigin
public class ApiRouteOptimizeController {

    @Autowired
    private RouteOptimizeService optimizeService;

    @PostMapping("/optimize")
    public ResponseEntity<List<ItineraryDTO>> optimize(@RequestBody OptimizeRouteRequestDTO req) {
        System.out.println("== REQ ==");
        System.out.println(req);
        System.out.println("Start: " + (req.getStart() == null ? "null" : req.getStart().getLat() + "," + req.getStart().getLng()));
        System.out.println("End:   " + (req.getEnd() == null ? "null" : req.getEnd().getLat() + "," + req.getEnd().getLng()));
        return ResponseEntity.ok(optimizeService.optimize(req));
    }
}
