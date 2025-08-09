/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hdda.repositories.impl;

import com.hdda.pojo.ScheduleTrip;
import com.hdda.repositories.ScheduleTripRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
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
public class ScheduleTripRepositoryImpl implements ScheduleTripRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<ScheduleTrip> getTripsBySchedule(Long scheduleId) {
        Session s = factory.getObject().getCurrentSession();
        String hql = "FROM ScheduleTrip WHERE schedule.id = :sid ORDER BY departTime";
        return s.createQuery(hql, ScheduleTrip.class)
                .setParameter("sid", scheduleId)
                .getResultList();
    }

    @Override
    public ScheduleTrip getTripById(Long id) {
        return factory.getObject().getCurrentSession()
                .get(ScheduleTrip.class, id);
    }

    @Override
    public ScheduleTrip addOrUpdateTrip(ScheduleTrip t) {
        Session s = factory.getObject().getCurrentSession();
        if (t.getId() == null) {
            s.persist(t);
        } else {
            s.merge(t);
        }
        return t;
    }

    @Override
    public void deleteTrip(Long id) {
        Session s = factory.getObject().getCurrentSession();
        s.remove(getTripById(id));
    }

    @Override
    @Transactional(readOnly = true)
// trước: public Optional<LocalTime> nextDeparture(...)
    public Optional<LocalDateTime> nextDeparture(Long scheduleId, LocalDate travelDate, LocalTime after) {
        Session s = factory.getObject().getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();

// Tạo ngưỡng ngày-giờ: hôm đó từ sau thời điểm 'after'
        LocalDateTime lowerBound = LocalDateTime.of(travelDate, after);

// 1️⃣ Query cho chuyến ngày travelDate và giờ >= after
        CriteriaQuery<LocalDateTime> cqToday = cb.createQuery(LocalDateTime.class);
        Root<ScheduleTrip> r = cqToday.from(ScheduleTrip.class);

        cqToday.select(r.get("departTime"))
                .where(cb.and(
                        cb.equal(r.get("schedule").get("id"), scheduleId),
                        cb.greaterThanOrEqualTo(r.get("departTime"), lowerBound)
                ))
                .orderBy(cb.asc(r.get("departTime")));

        Optional<LocalDateTime> firstToday = s.createQuery(cqToday)
                .setMaxResults(1)
                .uniqueResultOptional();
        if (firstToday.isPresent()) {
            return firstToday;
        }

        // 2️⃣ Nếu không có chuyến hôm nay thì query chuyến sớm nhất
        CriteriaQuery<LocalDateTime> cqNext = cb.createQuery(LocalDateTime.class);
        Root<ScheduleTrip> r2 = cqNext.from(ScheduleTrip.class);
        cqNext.select(r2.get("departTime"))
                .where(cb.equal(r2.get("schedule").get("id"), scheduleId))
                .orderBy(cb.asc(r2.get("departTime")));

        return s.createQuery(cqNext)
                .setMaxResults(1)
                .uniqueResultOptional();
    }

@Override
    public List<ScheduleTrip> findByRouteVariantAndDate(Long variantId, LocalDate date) {
        Session s = factory.getObject().getCurrentSession();
        CriteriaBuilder cb = s.getCriteriaBuilder();
        CriteriaQuery<ScheduleTrip> cq = cb.createQuery(ScheduleTrip.class);
        Root<ScheduleTrip> root = cq.from(ScheduleTrip.class);

        Join<Object, Object> scheduleJoin = root.join("schedule");

    // So sánh theo DATE(departTime)
    Predicate byVariant = cb.equal(scheduleJoin.get("variant").get("id"), variantId);
    Predicate byDate = cb.equal(cb.function("DATE", LocalDate.class, root.get("departTime")), date);

    cq.where(cb.and(byVariant, byDate));
    cq.orderBy(cb.asc(root.get("departTime")));

    return s.createQuery(cq).getResultList();
    }

}
