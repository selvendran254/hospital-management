package com.hospital.management.dto.request;

import com.hospital.management.domain.enums.AppointmentStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentUpdateRequest {

    private LocalDate appointmentDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String reason;
    private AppointmentStatus status;
    private String notes;
    private Boolean isVideo;
    private String meetingUrl;
}
