package com.app.meetly.controller;

import com.app.meetly.models.entity.Operator;
import com.app.meetly.models.request.CreateOperatorRequest;
import com.app.meetly.models.response.OperatorDetailResponse;
import com.app.meetly.service.OperatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/operator")
@Slf4j
@RequiredArgsConstructor
public class OperatorController {

    private final OperatorService operatorService;

    @PostMapping("/create")
    public ResponseEntity<?> createOperator(@RequestBody CreateOperatorRequest createOperatorRequest) throws Exception {
        Operator operator = operatorService.createOperator(createOperatorRequest);
        return ResponseEntity.ok(operator);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getOperator(@PathVariable Long id,
                                         @RequestParam(value="fromDate", required = false)     @DateTimeFormat(pattern="yyyy-MM-dd") Date fromDate,
                                         @RequestParam(value="toDate", required = false)     @DateTimeFormat(pattern="yyyy-MM-dd") Date toDate) throws Exception {
        OperatorDetailResponse operator = operatorService.getOperator(id, fromDate, toDate);
        return ResponseEntity.ok(operator);
    }
}