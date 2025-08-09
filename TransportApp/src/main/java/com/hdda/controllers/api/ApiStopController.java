/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hdda.controllers.api;

import com.hdda.pojo.Stop;
import com.hdda.services.StopService;
import jakarta.validation.Valid;
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
@RequestMapping("/api/stops")
@CrossOrigin
public class ApiStopController {
    @Autowired
    private StopService stopService;

    @GetMapping
    public ResponseEntity<List<Stop>> list(@RequestParam Map<String, String> params) {
        return ResponseEntity.ok(stopService.getStops(params));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Stop> get(@PathVariable("id") Long id) {
        return new ResponseEntity<>(this.stopService.getStopById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Stop> create(@RequestBody @Valid Stop stop) {
        Stop created = stopService.addOrUpdateStop(stop);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Stop> update(@PathVariable("id") Long id, @RequestBody @Valid Stop stop) {
        stop.setId(id);
        return ResponseEntity.ok(stopService.addOrUpdateStop(stop));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        stopService.deleteStop(id);
        return ResponseEntity.noContent().build();
    }
}

