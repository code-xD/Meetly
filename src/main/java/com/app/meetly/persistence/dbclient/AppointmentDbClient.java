package com.app.meetly.persistence.dbclient;

import com.app.meetly.models.entity.Appointment;
import com.app.meetly.models.request.CreateAppointmentRequest;
import com.app.meetly.persistence.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class AppointmentDbClient {

    private final AppointmentRepository appointmentRepository;


    public Appointment createAppointment(CreateAppointmentRequest appointmentRequest)  {
        Appointment apt = Appointment.builder().
                operatorId(appointmentRequest.getOperatorID()).
                startTime(appointmentRequest.getStartTime()).
                endTime(appointmentRequest.getEndTime()).
                code(appointmentRequest.getAppointmentCode()).
                isActive(true).
                build();

        return appointmentRepository.save(apt);
    }

    public Appointment deactivateAppointment(Appointment appointment)  {
        appointment.setIsActive(false);
        return appointmentRepository.save(appointment);
    }

}
