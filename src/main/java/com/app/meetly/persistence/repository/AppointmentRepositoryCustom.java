package com.app.meetly.persistence.repository;

import com.app.meetly.models.entity.Appointment;

import java.time.Instant;
import java.util.List;

public interface AppointmentRepositoryCustom {

    List<Appointment> findAppointmentsByTimeRangeAndOperator(Long operatorID, Instant startTime, Instant endTime);
}
