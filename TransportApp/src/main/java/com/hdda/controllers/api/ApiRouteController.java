/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hdda.controllers.api;

import com.hdda.pojo.Route;
import com.hdda.services.RouteService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@CrossOrigin
public class ApiRouteController {

    @Autowired
    private RouteService routeService;

    @GetMapping("/routes")
    public ResponseEntity<List<Route>> list(@RequestParam Map<String, String> params) {
        return new ResponseEntity<>(this.routeService.getRoutes(params), HttpStatus.OK);
    }

    @GetMapping("/routes/{id}")
    public ResponseEntity<?> getRouteDetail(@PathVariable("id") Long id) {
        return ResponseEntity.ok(routeService.getRouteDetail(id));
    }

}
