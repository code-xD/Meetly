package com.app.meetly.service;

import com.app.meetly.models.entity.Operator;
import com.app.meetly.models.request.CreateOperatorRequest;
import com.app.meetly.models.response.OperatorDetailResponse;

import java.util.Date;

public interface OperatorService {

    Operator createOperator(CreateOperatorRequest operatorRequest) throws Exception;

    OperatorDetailResponse getOperator(Long id, Date startDate, Date endDate) throws Exception;
}
