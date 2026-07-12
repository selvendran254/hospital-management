package com.hospital.management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDashboardResponse {

    private long todayAppointments;
    private long pendingAppointments;
    private long completedAppointments;
    private long totalPatients;
    private long upcomingLeaves;
}
