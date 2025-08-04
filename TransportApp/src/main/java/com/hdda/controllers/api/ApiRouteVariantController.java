/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hdda.controllers.api;


import com.hdda.dto.base.RouteVariantBaseDTO;
import com.hdda.dto.detail.RouteVariantDetailDTO;
import com.hdda.dto.request.RouteVariantRequestDTO;
import com.hdda.services.RouteVariantService;
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
public class ApiRouteVariantController {

    @Autowired
    private RouteVariantService routeService;

    @PostMapping("/variants")
    public ResponseEntity<RouteVariantRequestDTO> create(@RequestBody RouteVariantRequestDTO r) {
        RouteVariantRequestDTO newRoute = this.routeService.addOrUpdateRouteVariant(r);
        return new ResponseEntity<>(newRoute, HttpStatus.CREATED);
    }

    @GetMapping("/variants")
    public ResponseEntity<List<RouteVariantBaseDTO>> list(@RequestParam Map<String, String> params) {
        return new ResponseEntity<>(this.routeService.getRouteVariants(params), HttpStatus.OK);
    }

//    @GetMapping("/variants/{routeId}")
//    public ResponseEntity<RouteVariantDetailDTO> retrieve(@PathVariable(value = "routeId") Long id) {
//        return new ResponseEntity<>(this.routeService.getRouteVariantById(id), HttpStatus.OK);
//    }

    @DeleteMapping("/variants/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        this.routeService.deleteRouteVariant(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/variants/{id}")
    public ResponseEntity<RouteVariantRequestDTO> update(@PathVariable("id") Long id, @RequestBody RouteVariantRequestDTO r) {
        r.setId(id);
        RouteVariantRequestDTO updated = this.routeService.addOrUpdateRouteVariant(r);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }
}

