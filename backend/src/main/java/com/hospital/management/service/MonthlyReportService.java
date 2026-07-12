package com.hospital.management.service;

import com.hospital.management.domain.entity.Appointment;
import com.hospital.management.domain.entity.Bill;
import com.hospital.management.dto.response.FeatureResponses;
import com.hospital.management.repository.AppointmentRepository;
import com.hospital.management.repository.BillRepository;
import com.hospital.management.repository.PatientRepository;
import com.hospital.management.util.SimplePdfBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MonthlyReportService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final BillRepository billRepository;
    private final ClinicContextService clinicContextService;

    public FeatureResponses.MonthlyStatsResponse summarize(int year, int month) {
        UUID clinicId = clinicContextService.resolveClinicId();
        YearMonth ym = YearMonth.of(year, month);
        List<Appointment> appointments = appointmentRepository.findByClinicId(clinicId).stream()
                .filter(a -> YearMonth.from(a.getAppointmentDate()).equals(ym))
                .toList();
        BigDecimal revenue = billRepository.findByClinicId(clinicId, org.springframework.data.domain.Pageable.unpaged())
                .stream()
                .filter(b -> YearMonth.from(b.getCreatedAt().atZone(java.time.ZoneOffset.UTC)).equals(ym))
                .map(Bill::getPaidAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<String> highlights = new ArrayList<>();
        highlights.add("Appointments processed: " + appointments.size());
        highlights.add("Collected revenue: " + revenue);
        highlights.add("Active patient records: " + patientRepository.countByClinicId(clinicId));

        return FeatureResponses.MonthlyStatsResponse.builder()
                .year(year)
                .month(month)
                .appointments(appointments.size())
                .patients(patientRepository.countByClinicId(clinicId))
                .revenue(revenue)
                .highlights(highlights)
                .build();
    }

    public byte[] generatePdf(int year, int month) {
        FeatureResponses.MonthlyStatsResponse stats = summarize(year, month);
        return SimplePdfBuilder.fromLines(List.of(
                "MONTHLY HOSPITAL REPORT",
                "Period: " + stats.getYear() + "-" + String.format("%02d", stats.getMonth()),
                "Appointments: " + stats.getAppointments(),
                "Patients: " + stats.getPatients(),
                "Revenue: " + stats.getRevenue(),
                "Highlights: " + String.join(" | ", stats.getHighlights())
        ));
    }
}
