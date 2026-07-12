package com.hospital.management.service;

import com.hospital.management.domain.entity.Appointment;
import com.hospital.management.domain.entity.Bill;
import com.hospital.management.domain.entity.Doctor;
import com.hospital.management.dto.response.FeatureResponses;
import com.hospital.management.repository.AppointmentRepository;
import com.hospital.management.repository.BillRepository;
import com.hospital.management.repository.DoctorRepository;
import com.hospital.management.repository.PatientFeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DoctorPerformanceService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final BillRepository billRepository;
    private final PatientFeedbackRepository patientFeedbackRepository;
    private final ClinicContextService clinicContextService;

    public List<FeatureResponses.DoctorPerformanceRow> rankDoctors() {
        UUID clinicId = clinicContextService.resolveClinicId();
        List<Doctor> doctors = doctorRepository.findByClinicId(clinicId);
        List<Appointment> appointments = appointmentRepository.findByClinicId(clinicId);
        List<Bill> bills = billRepository.findByClinicId(clinicId, Pageable.unpaged()).stream().toList();

        Map<UUID, Long> appointmentCounts = appointments.stream()
                .collect(java.util.stream.Collectors.groupingBy(Appointment::getDoctorId, java.util.stream.Collectors.counting()));
        Map<UUID, BigDecimal> revenueByDoctor = new HashMap<>();
        Map<UUID, UUID> appointmentDoctor = appointments.stream()
                .collect(java.util.stream.Collectors.toMap(Appointment::getId, Appointment::getDoctorId, (a, b) -> a));
        for (Bill bill : bills) {
            if (bill.getAppointmentId() == null) {
                continue;
            }
            UUID doctorId = appointmentDoctor.get(bill.getAppointmentId());
            if (doctorId != null) {
                revenueByDoctor.merge(doctorId, bill.getPaidAmount(), BigDecimal::add);
            }
        }

        List<FeatureResponses.DoctorPerformanceRow> rows = new ArrayList<>();
        for (Doctor doctor : doctors) {
            Double avgRating = patientFeedbackRepository.averageRating(clinicId, doctor.getId());
            rows.add(FeatureResponses.DoctorPerformanceRow.builder()
                    .doctorId(doctor.getId())
                    .doctorName(doctor.getFirstName() + " " + doctor.getLastName())
                    .appointments(appointmentCounts.getOrDefault(doctor.getId(), 0L))
                    .avgRating(avgRating == null ? 0.0 : avgRating)
                    .revenue(revenueByDoctor.getOrDefault(doctor.getId(), BigDecimal.ZERO))
                    .build());
        }

        rows.sort(Comparator
                .comparing(FeatureResponses.DoctorPerformanceRow::getAppointments, Comparator.reverseOrder())
                .thenComparing(FeatureResponses.DoctorPerformanceRow::getAvgRating, Comparator.reverseOrder())
                .thenComparing(FeatureResponses.DoctorPerformanceRow::getRevenue, Comparator.reverseOrder()));
        for (int i = 0; i < rows.size(); i++) {
            rows.get(i).setRank(i + 1);
        }
        return rows;
    }
}
