/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hdda.services.impl;

import com.hdda.pojo.Favorite;
import com.hdda.repositories.FavoriteRepository;
import com.hdda.services.FavoriteService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author mahai
 */
@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepo;

    @Override
    public void save(Favorite f) {
        favoriteRepo.save(f);
    }

    @Override
    public List<Favorite> getFavoritesByCurrentUser() {
        return favoriteRepo.getFavoritesByCurrentUser();
    }

    @Override
    public void delete(Long id) {
        favoriteRepo.delete(id);
    }
}
