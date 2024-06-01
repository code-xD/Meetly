package com.app.meetly.service.impl;

import com.app.meetly.constants.Constants;
import com.app.meetly.models.entity.Appointment;
import com.app.meetly.models.entity.Operator;
import com.app.meetly.models.request.CancelAppointmentRequest;
import com.app.meetly.models.request.CreateAppointmentRequest;
import com.app.meetly.models.request.RescheduleAppointmentRequest;
import com.app.meetly.models.response.AppointmentSlotResponse;
import com.app.meetly.models.response.FreeSlotResponse;
import com.app.meetly.persistence.dbclient.AppointmentDbClient;
import com.app.meetly.persistence.dbclient.OperatorDbClient;
import com.app.meetly.persistence.repository.AppointmentRepository;
import com.app.meetly.persistence.repository.OperatorRepository;
import com.app.meetly.service.AppointmentService;
import com.app.meetly.service.exception.InvalidArgumentException;
import com.app.meetly.service.exception.OperatorNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final OperatorDbClient operatorDbClient;

    private final OperatorRepository operatorRepository;

    private final AppointmentDbClient appointmentDbClient;

    private final AppointmentRepository appointmentRepository;

    private static final ZoneId zone = ZoneId.of("Asia/Kolkata");


    //Source: https://www.geeksforgeeks.org/generate-random-string-of-given-size-in-java/
    private String createAppointmentCode() {

        // choose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789";

        int n = Constants.APPOINTMENT_CODE_LENGTH;

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(Constants.APPOINTMENT_CODE_LENGTH);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

    private String createAppointmentCode(String code) {
        return Constants.RESCHEDULE_APPOINTMENT_PREFIX + code;
    }

    private boolean isSlotAvailable(Instant startTime, Instant endTime, Long operatorID) {
        Optional<Long> appointmentID =
                appointmentRepository.getAvailableAppointmentByOperatorAndTimeSlot(operatorID, startTime, endTime);

        return appointmentID.isEmpty();
    }

    private Long findAvailableOperator(Instant startTime, Instant endTime) throws Exception{
        List<Long> occupiedOperatorID = appointmentRepository.getUnAvailableOperators(startTime, endTime);

        if(occupiedOperatorID.isEmpty()) {
            Optional<Operator> operator = operatorRepository.findFirstByOrderById();
            if(operator.isEmpty()) {
                throw new OperatorNotFoundException();
            }

            return operator.get().getId();
        }

        Optional<Long> operator = operatorRepository.findAvailableOperatorByIDs(occupiedOperatorID);

        if(operator.isEmpty()) {
            throw new InvalidArgumentException("no operator available in the given time slot");
        }

        return operator.get();
    }

    private void validateAndBuildAppointment(CreateAppointmentRequest appointment) throws Exception {
        if (appointment.getHour() < 0 || appointment.getHour() > 23) {
            throw new InvalidArgumentException("hour should be in 0-23 range.");
        }

        appointment.setStartTime(
                appointment.
                        getDate().
                        toInstant().
                        plus(appointment.getHour(), ChronoUnit.HOURS).
                        plus(appointment.getDate().getTimezoneOffset(), ChronoUnit.MINUTES)
        );

        if(appointment.getStartTime().isBefore(Instant.now())) {
            throw new InvalidArgumentException("start time cannot be less than current time");
        }

        appointment.setEndTime(appointment.getStartTime().plus(Constants.APPOINTMENT_DURATION_IN_HOURS, ChronoUnit.HOURS));

        if(!appointment.getOperatorName().isBlank()){
            Operator operator = operatorDbClient.getOperatorByName(appointment.getOperatorName());
            appointment.setOperatorID(operator.getId());

            if(!isSlotAvailable(appointment.getStartTime(), appointment.getEndTime(), appointment.getOperatorID())) {
                throw new InvalidArgumentException("operator is not free in the given time slot");
            }
        } else {
            Long operatorID = findAvailableOperator(appointment.getStartTime(), appointment.getEndTime());
            appointment.setOperatorID(operatorID);
        }

    }


    private ArrayList<FreeSlotResponse> getFreeSlots(List<Appointment> activeAppointments, Instant startTime, Instant endTime) {
        ArrayList<FreeSlotResponse> freeSlots = new ArrayList<>();

        Instant cursorStartTime = startTime;
        for(Appointment appointment: activeAppointments) {
            if(cursorStartTime == null ||
                    cursorStartTime.isBefore(appointment.getStartTime())) {
                freeSlots.add(
                        FreeSlotResponse.builder().
                                startTime(cursorStartTime).
                                endTime(appointment.getStartTime()).
                                build()
                );
                cursorStartTime = appointment.getEndTime();
                continue;
            }

            // cursor ahead of current appointment
            if(cursorStartTime.equals(appointment.getEndTime())  ||
                    cursorStartTime.isAfter(appointment.getEndTime())) {
                continue;
            }

            // overlapping cursor
            if(cursorStartTime.equals(appointment.getStartTime()) ||
                    cursorStartTime.isAfter(appointment.getStartTime())) {
                cursorStartTime = appointment.getEndTime();
            }
        }

        if(endTime == null || cursorStartTime.isBefore(endTime)) {
            freeSlots.add(
                    FreeSlotResponse.builder().
                            startTime(cursorStartTime).
                            endTime(endTime).
                            build()
            );
        }

        return freeSlots;
    }

    @Override
    public AppointmentSlotResponse getAppointmentsForOperator(Long operatorID, Instant startTime, Instant endTime) {
        List<Appointment> activeAppointments = appointmentRepository.
                findAppointmentsByTimeRangeAndOperator(operatorID, startTime, endTime);

        AppointmentSlotResponse response = new AppointmentSlotResponse();

        response.setOngoingSlots(activeAppointments);
        response.setFreeSlots(getFreeSlots(activeAppointments, startTime, endTime));

        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Appointment createAppointment(CreateAppointmentRequest createAppointmentRequest) throws Exception {
        createAppointmentRequest.setAppointmentCode(createAppointmentCode());

        validateAndBuildAppointment(createAppointmentRequest);

        return appointmentDbClient.createAppointment(createAppointmentRequest);
    }


    // We can reschedule older appointments but creation of newer appointments should follow createAppointmentValidations
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Appointment rescheduleAppointment(RescheduleAppointmentRequest appointmentRequest) throws Exception {
        CancelAppointmentRequest cancelRequest = new CancelAppointmentRequest(appointmentRequest.getCode());
        Appointment appointment = cancelAppointment(cancelRequest);

        Optional<Operator> operator = operatorRepository.findById(appointment.getOperatorId());
        if(operator.isEmpty()) {
            throw new OperatorNotFoundException();
        }


        CreateAppointmentRequest createAppointmentRequest = CreateAppointmentRequest.builder().
                appointmentCode(createAppointmentCode(appointment.getCode())).
                date(appointmentRequest.getDate()).
                hour(appointmentRequest.getHour()).
                operatorName(operator.get().getName()).
                build();

        validateAndBuildAppointment(createAppointmentRequest);

        return appointmentDbClient.createAppointment(createAppointmentRequest);
    }

    // We can cancel older appointment shouldn't be an issue as it is done in google calendar as well.
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Appointment cancelAppointment(CancelAppointmentRequest appointmentRequest) throws Exception{
        Optional<Appointment> appointment = appointmentRepository.getAppointmentByCodeAndIsActiveTrue(appointmentRequest.getCode());
        if(appointment.isEmpty()) {
            throw new InvalidArgumentException("appointment does exist or is inactive");
        }

        return appointmentDbClient.deactivateAppointment(appointment.get());
    }
}
