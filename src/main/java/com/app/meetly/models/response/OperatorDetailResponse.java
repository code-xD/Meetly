package com.app.meetly.models.response;

import com.app.meetly.models.entity.Operator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperatorDetailResponse {
    Operator operator;
    AppointmentSlotResponse appointmentSlots;
}
