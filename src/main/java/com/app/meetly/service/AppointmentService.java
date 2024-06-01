package com.app.meetly.service;

import com.app.meetly.models.entity.Appointment;
import com.app.meetly.models.request.CancelAppointmentRequest;
import com.app.meetly.models.request.CreateAppointmentRequest;
import com.app.meetly.models.request.RescheduleAppointmentRequest;
import com.app.meetly.models.response.AppointmentSlotResponse;

import java.time.Instant;

public interface AppointmentService {

    AppointmentSlotResponse getAppointmentsForOperator(Long operatorID, Instant startTime, Instant endTime);

    Appointment createAppointment(CreateAppointmentRequest createAppointmentRequest) throws Exception;

    Appointment rescheduleAppointment(RescheduleAppointmentRequest appointmentRequest) throws Exception;

    Appointment cancelAppointment(CancelAppointmentRequest appointmentRequest) throws Exception;

}
