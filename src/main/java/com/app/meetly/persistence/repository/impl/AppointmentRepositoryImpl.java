package com.app.meetly.persistence.repository.impl;

import com.app.meetly.models.entity.Appointment;
import com.app.meetly.persistence.repository.AppointmentRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.OrderBy;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class AppointmentRepositoryImpl implements AppointmentRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<Appointment> findAppointmentsByTimeRangeAndOperator(Long operatorID, Instant startTime, Instant endTime) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Appointment> cq = cb.createQuery(Appointment.class);

        Root<Appointment> appointment = cq.from(Appointment.class);
        List<Predicate> predicates = new ArrayList<>();


        predicates.add(cb.equal(appointment.get("operatorId"), operatorID));
        predicates.add(cb.equal(appointment.get("isActive"), true));

        if(startTime != null) {
            predicates.add(cb.greaterThanOrEqualTo(appointment.get("startTime"), startTime));
        }

        if (endTime != null) {
            predicates.add(cb.lessThan(appointment.get("startTime"), endTime));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.asc(appointment.get("startTime")));

        return em.createQuery(cq).getResultList();
    }
}
