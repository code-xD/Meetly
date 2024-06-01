package com.app.meetly.models.response;


import com.app.meetly.models.entity.Appointment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentSlotResponse {
    List<Appointment> ongoingSlots;
    List<FreeSlotResponse> freeSlots;
}
