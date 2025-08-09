/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hdda.controllers.api;

import com.hdda.pojo.Favorite;
import com.hdda.pojo.User;
import com.hdda.services.FavoriteService;
import com.hdda.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author mahai
 */
@RestController
@RequestMapping("/api/secure/favorites")
@CrossOrigin
public class ApiFavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody Favorite f) {
        favoriteService.save(f);
        return ResponseEntity.ok(Map.of("message", "Đã lưu mục yêu thích"));
    }

    @GetMapping
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(favoriteService.getFavoritesByCurrentUser());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        favoriteService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Đã xóa mục yêu thích"));
    }
}

