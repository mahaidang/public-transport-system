/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hdda.repositories.impl;

import com.hdda.pojo.RouteVariant;
import com.hdda.repositories.RouteVariantRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
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
public class RouteVariantRepositoryImpl implements RouteVariantRepository {

    private static final int PAGE_SIZE = 10;
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<RouteVariant> getRouteVariants(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<RouteVariant> q = b.createQuery(RouteVariant.class);
        Root root = q.from(RouteVariant.class);
        q.select(root);

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.like(root.get("name"), String.format("%%%s%%", kw)));
            }
            

            q.where(predicates.toArray(Predicate[]::new));
            Set<String> allowedFields = Set.of( "id", "seq");

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
    public RouteVariant getRouteVariantById(Long id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(RouteVariant.class, id);
    }

    @Override
    public RouteVariant addOrUpdateRouteVariant(RouteVariant r) {
        Session s = this.factory.getObject().getCurrentSession();
        if (r.getId() == null) {
            s.persist(r);
        } else {
            s.merge(r);
        }
        return r;
    }

    @Override
    public void deleteRouteVariant(Long id) {
        Session s = this.factory.getObject().getCurrentSession();
        s.remove(this.getRouteVariantById(id));
    }

 @Override
    public List<RouteVariant> byStartAndEnd(Long startId, Long endId) {
        // 1. Lấy Hibernate Session
        Session s = this.factory.getObject().getCurrentSession();
        // 2. Tạo CriteriaBuilder và CriteriaQuery cho RouteVariant
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<RouteVariant> cq = cb.createQuery(RouteVariant.class);
        Root<RouteVariant> root = cq.from(RouteVariant.class);

        // 3. Đặt điều kiện WHERE: startStop.id = :startId AND endStop.id = :endId
        Predicate pStart = cb.equal(root.get("startStop").get("id"), startId);
        Predicate pEnd   = cb.equal(root.get("endStop")  .get("id"), endId);

        // 4. Build query: SELECT root WHERE … 
        cq.select(root)
          .where(cb.and(pStart, pEnd));

        // 5. Thực thi và trả kết quả
        return s.createQuery(cq)
                .getResultList();
    }

    @Override
    public List<RouteVariant> startingAt(Long s) {
        // 1. Lấy Hibernate Session
        Session session = factory.getObject().getCurrentSession();
        // 2. Tạo CriteriaBuilder / CriteriaQuery
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<RouteVariant> cq = cb.createQuery(RouteVariant.class);
        Root<RouteVariant> rv = cq.from(RouteVariant.class);

        // 3. WHERE rv.startStop.id = :s
        cq.select(rv)
          .where(cb.equal(rv.get("startStop").get("id"), s));

        // 4. Thực thi query
        return session.createQuery(cq).getResultList();
    }

    @Override
    public List<RouteVariant> endingAt(Long e) {
        Session session = factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<RouteVariant> cq = cb.createQuery(RouteVariant.class);
        Root<RouteVariant> rv = cq.from(RouteVariant.class);

        cq.select(rv)
          .where(cb.equal(rv.get("endStop").get("id"), e));

        return session.createQuery(cq).getResultList();
    }

}
