/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hdda.repositories.impl;

import com.hdda.pojo.Route;
import com.hdda.pojo.RouteVariant;
import com.hdda.pojo.Stop;
import com.hdda.repositories.RouteRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
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
public class RouteRepositoryImpl implements RouteRepository {

    private static final int PAGE_SIZE = 10;
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Route addOrUpdateRoute(Route r) {
        Session s = this.factory.getObject().getCurrentSession();
        if (r.getId() == null) {
            s.persist(r);
        } else {
            s.merge(r);
        }

        return r;
    }

    @Override
    public List<Route> getRoutes(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Route> q = b.createQuery(Route.class);
        Root root = q.from(Route.class);
        q.select(root);

        Join<Route, RouteVariant> variantJoin = null;
        Join<RouteVariant, Stop> startJoin = null;
        Join<RouteVariant, Stop> endJoin = null;

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            String kw = params.get("name");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.like(root.get("name"), String.format("%%%s%%", kw)));
            }

            String code = params.get("code");
            if (code != null && !code.isEmpty()) {
                predicates.add(b.like(root.get("code"), String.format("%%%s%%", code)));
            }

            String start = params.get("start");
            if (start != null && !start.isEmpty()) {
                if (variantJoin == null) {
                    variantJoin = root.join("routeVariantSet", JoinType.LEFT);
                }
                if (startJoin == null) {
                    startJoin = variantJoin.join("startStop", JoinType.LEFT);
                }
                predicates.add(b.like(startJoin.get("name"), "%" + start + "%"));
            }

            String end = params.get("end");
            if (end != null && !end.isEmpty()) {
                if (variantJoin == null) {
                    variantJoin = root.join("routeVariantSet", JoinType.LEFT);
                }
                if (endJoin == null) {
                    endJoin = variantJoin.join("endStop", JoinType.LEFT);
                }
                predicates.add(b.like(endJoin.get("name"), "%" + end + "%"));
            }

            q.where(predicates.toArray(Predicate[]::new));
            Set<String> allowedFields = Set.of("name", "code", "id");

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
    public Route getRouteById(Long id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Route.class, id);
    }

    @Override
    public void deleteRoute(Long id) {
        Session s = this.factory.getObject().getCurrentSession();
        s.remove(this.getRouteById(id));
    }

    @Override
    public List<RouteVariant> getVariants(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public long countRoutes(Map<String, String> params) {
        Session s = factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<Long> q = b.createQuery(Long.class);
        Root<Route> root = q.from(Route.class);

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
    public List<RouteVariant> getVariantsByRouteId(Long routeId) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<RouteVariant> q = b.createQuery(RouteVariant.class);
        Root root = q.from(RouteVariant.class);
        q.select(root);

        q.where(b.equal(root.get("route").get("id"), routeId));
        Query query = s.createQuery(q);
        return query.getResultList();
    }

    @Override
    public Route detailRoute(Long id) {
        Session s = factory.getObject().getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<Route> cq = cb.createQuery(Route.class);
        Root<Route> root = cq.from(Route.class);
        root.fetch("routeVariantSet", JoinType.LEFT);
        cq.select(root).where(cb.equal(root.get("id"), id));
        return s.createQuery(cq).uniqueResult();
    }
}
