/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hdda.services;

import com.hdda.pojo.Favorite;
import java.util.List;

/**
 *
 * @author mahai
 */
public interface FavoriteService {

    void save(Favorite f);
    List<Favorite> getFavoritesByCurrentUser();
    void delete(Long id);
}
