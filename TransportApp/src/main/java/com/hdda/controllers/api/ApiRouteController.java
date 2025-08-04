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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

//    @PostMapping("/routes")
//    public ResponseEntity<Route> create(@RequestBody Route r) {
//        Route newRoute = this.routeService.addOrUpdateRoute(r);
//        return new ResponseEntity<>(newRoute, HttpStatus.CREATED);
//    }

    @GetMapping("/routes")
    public ResponseEntity<List<Route>> list(@RequestParam Map<String, String> params) {
        return new ResponseEntity<>(this.routeService.getRoutes(params), HttpStatus.OK);
    }

    @GetMapping("/routes/{routeId}")
    public ResponseEntity<Route> retrieve(@PathVariable(value = "routeId") Long id) {
        return new ResponseEntity<>(this.routeService.getRouteById(id), HttpStatus.OK);
    }

//    @DeleteMapping("/routes/{id}")
//    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
//        this.routeService.deleteRoute(id);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
//
//    @PutMapping("/routes/{id}")
//    public ResponseEntity<Route> update(@PathVariable("id") Long id, @RequestBody Route r) {
//        r.setId(id);
//        Route updated = this.routeService.addOrUpdateRoute(r);
//        return new ResponseEntity<>(updated, HttpStatus.OK);
//    }
//
//    @GetMapping("/routes/{routeId}/variants")
//    public ResponseEntity<Route> getVariants(@PathVariable(value = "routeId") Long id) {
//        return new ResponseEntity<>(this.routeService.getRouteById(id), HttpStatus.OK);
//    }
}
