/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hdda.repositories.impl;

import com.hdda.pojo.Favorite;
import com.hdda.pojo.User;
import com.hdda.repositories.FavoriteRepository;
import com.hdda.repositories.UserRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author mahai
 */
@Repository
@Transactional
public class FavoriteRepositoryImpl implements FavoriteRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Autowired
    private UserRepository userRepo;

    @Override
    public void save(Favorite f) {
        Session s = factory.getObject().getCurrentSession();
        f.setUser(this.userRepo.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName())); // update current user
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<Favorite> cq = cb.createQuery(Favorite.class);
        Root<Favorite> root = cq.from(Favorite.class);
        cq.select(root).where(cb.and(
                cb.equal(root.get("user").get("id"), f.getUser().getId()),
                cb.equal(root.get("route").get("id"), f.getRoute().getId())
        ));

        List<Favorite> existed = s.createQuery(cq).getResultList();

        if (existed.isEmpty()) {
            s.persist(f); // chỉ lưu nếu chưa tồn tại
        }
    }

    @Override
    public List<Favorite> getFavoritesByCurrentUser() {
        Session s = factory.getObject().getCurrentSession();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.getUserByUsername(username);

        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Favorite> q = b.createQuery(Favorite.class);
        Root<Favorite> root = q.from(Favorite.class);
        Predicate byUser = b.equal(root.get("user"), user);
        q.select(root).where(byUser);

        return s.createQuery(q).getResultList();
    }

    @Override
    public void delete(Long id) {
        Session s = factory.getObject().getCurrentSession();
        Favorite f = s.get(Favorite.class, id);
        if (f != null) {
            s.remove(f);
        }
    }
}
