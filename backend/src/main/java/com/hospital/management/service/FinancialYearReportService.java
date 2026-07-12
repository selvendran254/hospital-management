package com.hospital.management.service;

import com.hospital.management.domain.entity.Bill;
import com.hospital.management.dto.response.FeatureResponses;
import com.hospital.management.repository.BillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FinancialYearReportService {

    private final BillRepository billRepository;
    private final ClinicContextService clinicContextService;

    public FeatureResponses.FinancialReportResponse generate(int financialYearStart) {
        UUID clinicId = clinicContextService.resolveClinicId();
        BigDecimal revenue = billRepository.findByClinicId(clinicId, Pageable.unpaged()).stream()
                .filter(this::hasPaidData)
                .filter(b -> inFinancialYear(b, financialYearStart))
                .map(Bill::getPaidAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal expense = revenue.multiply(new BigDecimal("0.62"));
        return FeatureResponses.FinancialReportResponse.builder()
                .financialYear(financialYearStart)
                .revenue(revenue)
                .expense(expense)
                .net(revenue.subtract(expense))
                .build();
    }

    private boolean hasPaidData(Bill bill) {
        return bill.getPaidAmount() != null;
    }

    private boolean inFinancialYear(Bill bill, int startYear) {
        int year = bill.getCreatedAt().atZone(ZoneOffset.UTC).getYear();
        int month = bill.getCreatedAt().atZone(ZoneOffset.UTC).getMonthValue();
        int fyYear = month >= 4 ? year : year - 1;
        return fyYear == startYear;
    }
}
