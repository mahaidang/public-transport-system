/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hdda.repositories;

import com.hdda.pojo.User;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author mahai
 */
public interface UserRepository {

    User getUserByUsername(String username);

    User addUser(User u);

    public boolean authenticate(String username, String password);
}
