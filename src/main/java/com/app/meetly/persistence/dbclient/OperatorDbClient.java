package com.app.meetly.persistence.dbclient;

import com.app.meetly.models.entity.Operator;
import com.app.meetly.models.request.CreateOperatorRequest;
import com.app.meetly.persistence.repository.OperatorRepository;
import com.app.meetly.service.exception.InvalidArgumentException;
import com.app.meetly.service.exception.OperatorNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OperatorDbClient {

    private final OperatorRepository operatorRepository;

    public Operator createOperator(CreateOperatorRequest createOperatorRequest) {
        Operator operator = Operator.builder().
                name(createOperatorRequest.getName()).build();

        return operatorRepository.save(operator);
    }

    public Operator getOperatorByName(String name) throws Exception {
        Optional<Operator> operator = operatorRepository.findOperatorByName(name);
        if(operator.isEmpty()) {
            throw new OperatorNotFoundException();
        }

        return operator.get();
    }

}
