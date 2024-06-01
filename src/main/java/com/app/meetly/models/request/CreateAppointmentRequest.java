package com.app.meetly.models.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAppointmentRequest {

    String operatorName = "";

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    Date date;

    Integer hour;

    @JsonIgnore
    Instant startTime;

    @JsonIgnore
    Instant endTime;

    @JsonIgnore
    String appointmentCode;

    @JsonIgnore
    Long operatorID;
}
