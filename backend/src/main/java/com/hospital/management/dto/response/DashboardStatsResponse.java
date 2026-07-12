package com.hospital.management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsResponse {

    private long totalPatients;
    private long totalDoctors;
    private long totalAppointments;
    private long totalDepartments;
    private long pendingAppointments;
    private long todayAppointments;
    private long occupiedBeds;
    private long availableBeds;
    private long lowStockMedicines;
    private long pendingLabReports;
}
