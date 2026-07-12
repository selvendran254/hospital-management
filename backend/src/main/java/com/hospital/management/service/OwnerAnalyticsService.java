package com.hospital.management.service;

import com.hospital.management.dto.response.ExtensionResponses;
import com.hospital.management.repository.AppointmentRepository;
import com.hospital.management.repository.BillRepository;
import com.hospital.management.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OwnerAnalyticsService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final BillRepository billRepository;

    public ExtensionResponses.OwnerAnalyticsResponse getExtendedStats() {
        Map<String, Long> byStatus = new HashMap<>();
        appointmentRepository.findAll().forEach(a ->
                byStatus.merge(a.getStatus().name(), 1L, Long::sum));
        BigDecimal revenue = billRepository.findAll().stream()
                .map(b -> b.getPaidAmount() == null ? BigDecimal.ZERO : b.getPaidAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return ExtensionResponses.OwnerAnalyticsResponse.builder()
                .totalPatients(patientRepository.count())
                .totalAppointments(appointmentRepository.count())
                .totalBills(billRepository.count())
                .totalRevenue(revenue)
                .appointmentByStatus(byStatus)
                .build();
    }
}
