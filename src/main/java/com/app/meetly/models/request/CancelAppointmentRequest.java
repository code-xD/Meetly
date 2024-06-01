package com.app.meetly.models.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancelAppointmentRequest {

    @NotBlank(message = "code should be present")
    String code;
}
