/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hdda.repositories.impl;

import com.hdda.pojo.Schedule;
import com.hdda.repositories.ScheduleRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
public class ScheduleRepositoryImpl implements ScheduleRepository {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Schedule> getSchedules(Map<String, String> params) {
        Session           s  = factory.getObject().getCurrentSession();
        CriteriaBuilder   cb = s.getCriteriaBuilder();
        CriteriaQuery<Schedule> cq = cb.createQuery(Schedule.class);
        Root<Schedule>    root = cq.from(Schedule.class);
        cq.select(root);

        /* ==== Bộ lọc động ==== */
        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            /* Lọc theo variant */
            String variantId = params.get("variantId");
            if (variantId != null && !variantId.isEmpty())
                predicates.add(cb.equal(root.get("variantId").get("id"), Long.valueOf(variantId)));

            /* Lọc theo từ-khóa ghi chú */
            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty())
                predicates.add(cb.like(root.get("note"), String.format("%%%s%%", kw)));

            /* Lọc theo ngày hiệu lực */
            String from = params.get("from");
            if (from != null && !from.isEmpty())
                predicates.add(cb.greaterThanOrEqualTo(root.get("effectiveFrom"), java.sql.Date.valueOf(from)));
            String to = params.get("to");
            if (to != null && !to.isEmpty())
                predicates.add(cb.lessThanOrEqualTo(root.get("effectiveTo"), java.sql.Date.valueOf(to)));

            cq.where(predicates.toArray(Predicate[]::new));
        }

        Query<Schedule> query = s.createQuery(cq);

        /* Phân trang */
        if (params != null && params.containsKey("page")) {
            int page  = Integer.parseInt(params.get("page"));
            int start = (page - 1) * PAGE_SIZE;
            query.setFirstResult(start).setMaxResults(PAGE_SIZE);
        }
        return query.getResultList();
    }

    @Override
    public Schedule getScheduleById(Long id) {
        return factory.getObject().getCurrentSession().get(Schedule.class, id);
    }

    @Override
    public Schedule addOrUpdateSchedule(Schedule sc) {
        Session s = factory.getObject().getCurrentSession();
        if (sc.getId() == null)
            s.persist(sc);
        else
            s.merge(sc);
        return sc;
    }

    @Override
    public void deleteSchedule(Long id) {
        Session s = factory.getObject().getCurrentSession();
        s.remove(getScheduleById(id));
    }

    @Override
    public long countSchedules(Map<String, String> params) {
        /* Có thể build truy vấn tương tự getSchedules nhưng dùng CB.count */
        return getSchedules(params).size();
    }
}
