package com.app.meetly.controller;

import com.app.meetly.models.entity.Appointment;
import com.app.meetly.models.entity.Operator;
import com.app.meetly.models.request.CancelAppointmentRequest;
import com.app.meetly.models.request.CreateAppointmentRequest;
import com.app.meetly.models.request.CreateOperatorRequest;
import com.app.meetly.models.request.RescheduleAppointmentRequest;
import com.app.meetly.service.AppointmentService;
import com.app.meetly.service.OperatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/appointment")
@Slf4j
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping("/create")
    public ResponseEntity<?> createAppointment(@RequestBody CreateAppointmentRequest appointmentRequest) throws Exception {
        Appointment appointment = appointmentService.createAppointment(appointmentRequest);
        return ResponseEntity.ok(appointment);
    }

    @PostMapping("/reschedule")
    public ResponseEntity<?> rescheduleAppointment(@RequestBody RescheduleAppointmentRequest appointmentRequest) throws Exception {
        Appointment appointment = appointmentService.rescheduleAppointment(appointmentRequest);
        return ResponseEntity.ok(appointment);
    }

    @PostMapping("/cancel")
    public ResponseEntity<?> cancelAppointment(@RequestBody CancelAppointmentRequest appointmentRequest) throws Exception {
        Appointment appointment = appointmentService.cancelAppointment(appointmentRequest);
        return ResponseEntity.ok(appointment);
    }

}
