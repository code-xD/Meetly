package com.app.meetly.persistence.repository;

import com.app.meetly.models.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>, AppointmentRepositoryCustom {

    @Query(value = "select id from mtly_appointment apt where apt.operator_id = :operatorId and apt.is_active = true " +
            "and MAX(apt.start_time, :startTime) < MIN(apt.end_time, :endTime) LIMIT 1",
            nativeQuery = true)
    Optional<Long> getAvailableAppointmentByOperatorAndTimeSlot(Long operatorId, Instant startTime, Instant endTime);

    // This query is not production scalable but given the data points we have it should be fine.
    @Query(value = "select distinct(operator_id) from mtly_appointment apt where apt.is_active = true " +
            "and MAX(apt.start_time, :startTime) < MIN(apt.end_time, :endTime)",
            nativeQuery = true)
    List<Long> getUnAvailableOperators(Instant startTime, Instant endTime);

    Optional<Appointment> getAppointmentByCodeAndIsActiveTrue(String code);

}
