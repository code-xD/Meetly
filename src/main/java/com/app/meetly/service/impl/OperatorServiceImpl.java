package com.app.meetly.service.impl;

import com.app.meetly.models.entity.Operator;
import com.app.meetly.models.request.CreateOperatorRequest;
import com.app.meetly.models.response.OperatorDetailResponse;
import com.app.meetly.persistence.dbclient.OperatorDbClient;
import com.app.meetly.persistence.repository.OperatorRepository;
import com.app.meetly.service.AppointmentService;
import com.app.meetly.service.OperatorService;
import com.app.meetly.service.exception.InvalidArgumentException;
import com.app.meetly.service.exception.OperatorNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OperatorServiceImpl implements OperatorService {

    private final OperatorDbClient operatorDbClient;

    private final OperatorRepository operatorRepository;

    private final AppointmentService appointmentService;

    @Override
    public Operator createOperator(CreateOperatorRequest operatorRequest) throws Exception {
        if (Objects.isNull(operatorRequest.getName()) || operatorRequest.getName().isEmpty()) {
            throw new InvalidArgumentException("operator name is not valid");
        }
        return operatorDbClient.createOperator(operatorRequest);
    }

    @Override
    public OperatorDetailResponse getOperator(Long id, Date startDate, Date endDate) throws Exception {
        Optional<Operator> operator = operatorRepository.findById(id);

        if (operator.isEmpty()) {
            throw new OperatorNotFoundException();
        }

        if(endDate != null) {
            endDate = Date.from(endDate.toInstant().plus(24, ChronoUnit.HOURS));
        }


        OperatorDetailResponse response = new OperatorDetailResponse();
        response.setOperator(operator.get());

        if( startDate != null || endDate != null) {
            response.setAppointmentSlots(
                    appointmentService.getAppointmentsForOperator(
                            id,
                            startDate == null ? null : startDate.toInstant(),
                            endDate == null ? null : endDate.toInstant()
                    )
            );
        }

        return response;
    }



}
