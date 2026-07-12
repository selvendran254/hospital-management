package com.hospital.management.service;

import com.hospital.management.domain.enums.AppointmentStatus;
import com.hospital.management.domain.enums.BedStatus;
import com.hospital.management.domain.enums.UserRole;
import com.hospital.management.dto.response.DashboardStatsResponse;
import com.hospital.management.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final DepartmentRepository departmentRepository;
    private final BedRepository bedRepository;
    private final MedicineRepository medicineRepository;
    private final LabReportRepository labReportRepository;

    public DashboardStatsResponse getDashboardStats() {
        LocalDate today = LocalDate.now();
        long occupiedBeds = bedRepository.findByStatus(BedStatus.OCCUPIED).size();
        long availableBeds = bedRepository.findByStatus(BedStatus.AVAILABLE).size();
        return DashboardStatsResponse.builder()
                .totalPatients(patientRepository.count())
                .totalDoctors(doctorRepository.count())
                .totalAppointments(appointmentRepository.count())
                .totalDepartments(departmentRepository.count())
                .pendingAppointments(appointmentRepository.findAll().stream()
                        .filter(a -> a.getStatus() == AppointmentStatus.PENDING).count())
                .todayAppointments(appointmentRepository.findAll().stream()
                        .filter(a -> today.equals(a.getAppointmentDate())).count())
                .occupiedBeds(occupiedBeds)
                .availableBeds(availableBeds)
                .lowStockMedicines(medicineRepository.findLowStockMedicines().size())
                .pendingLabReports(labReportRepository.findByCompletedFalse().size())
                .build();
    }
}
