/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hdda.repositories.impl;

import com.hdda.pojo.Stop;
import com.hdda.repositories.StopRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author mahai
 */
@Repository
@Transactional
public class StopRepositoryImpl implements StopRepository {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Stop> getStops(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Stop> q = b.createQuery(Stop.class);
        Root root = q.from(Stop.class);
        q.select(root);

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.like(root.get("name"), String.format("%%%s%%", kw)));
            }

            q.where(predicates.toArray(Predicate[]::new));
            Set<String> allowedFields = Set.of("id");

            String orderBy = params.get("orderBy");
            if (orderBy != null && !orderBy.isEmpty() && allowedFields.contains(orderBy)) {
                q.orderBy(b.asc(root.get(orderBy)));
            }
        }

        Query query = s.createQuery(q);

        if (params != null && params.containsKey("page")) {
            int page = Integer.parseInt(params.get("page"));
            int start = (page - 1) * PAGE_SIZE;

            query.setMaxResults(PAGE_SIZE);
            query.setFirstResult(start);
        }

        return query.getResultList();
    }

    @Override
    public Stop getStopById(Long id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Stop.class, id);
    }

    @Override
    public Stop addOrUpdateStop(Stop stop) {
        Session s = this.factory.getObject().getCurrentSession();
        if (stop.getId() == null) {
            s.persist(stop);
        } else {
            s.merge(stop);
        }
        return stop;
    }

    @Override
    public void deleteStop(Long id) {
        Session s = this.factory.getObject().getCurrentSession();
        s.remove(this.getStopById(id));
    }

    @Override
    public long countStops(Map<String, String> params) {
        Session s = factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Long> q = b.createQuery(Long.class);
        Root<Stop> root = q.from(Stop.class);

        List<Predicate> predicates = new ArrayList<>();
        String kw = params.get("kw");
        if (kw != null && !kw.isEmpty()) {
            predicates.add(b.like(root.get("name"), "%" + kw + "%"));
        }

        q.select(b.count(root));
        if (!predicates.isEmpty()) {
            q.where(predicates.toArray(Predicate[]::new));
        }

        return s.createQuery(q).getSingleResult();
    }

    @Override
    public Stop findNearest(double lat, double lng) {
        Session s = factory.getObject().getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<Stop> cq = cb.createQuery(Stop.class);
        Root<Stop> root = cq.from(Stop.class);

        // Dùng ST_Distance_Sphere nếu MySQL8, thay thế Haversine truyền thống
        Expression<Double> distanceExpr = cb.function(
                "st_distance_sphere",
                Double.class,
                cb.function("point", Object.class,
                        root.get("longitude"), root.get("latitude")
                ),
                cb.function("point", Object.class,
                        cb.literal(lng), cb.literal(lat)
                )
        );

        cq.select(root).orderBy(cb.asc(distanceExpr));

        return s.createQuery(cq)
                .setMaxResults(1)
                .uniqueResult();
    }
}
