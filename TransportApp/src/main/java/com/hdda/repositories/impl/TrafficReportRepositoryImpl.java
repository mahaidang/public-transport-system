/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hdda.repositories.impl;

import com.hdda.pojo.TrafficReport;
import com.hdda.repositories.TrafficReportRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author mahai
 */
@Transactional
@Repository
public class TrafficReportRepositoryImpl implements TrafficReportRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public TrafficReport save(TrafficReport r) {
        Session s = this.factory.getObject().getCurrentSession();
        if (r.getId() == null) {
            s.persist(r);
        } else {
            s.merge(r);
        }

        return r;
    }

    @Override
    public List<TrafficReport> getAllReports() {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<TrafficReport> cq = cb.createQuery(TrafficReport.class);
        Root<TrafficReport> root = cq.from(TrafficReport.class);
        cq.select(root).orderBy(cb.desc(root.get("createdAt")));
        return s.createQuery(cq).getResultList();
    }

    @Override
    public TrafficReport getReportById(Long id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(TrafficReport.class, id);
    }
}
