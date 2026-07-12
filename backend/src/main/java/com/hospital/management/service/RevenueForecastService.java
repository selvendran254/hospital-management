package com.hospital.management.service;

import com.hospital.management.dto.response.ExtensionResponses;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class RevenueForecastService {

    public ExtensionResponses.RevenueForecastResponse forecast(BigDecimal lastMonthRevenue, BigDecimal growthRate, int months) {
        List<ExtensionResponses.RevenueForecastPoint> points = new ArrayList<>();
        BigDecimal current = lastMonthRevenue;
        YearMonth month = YearMonth.now();
        for (int i = 0; i < months; i++) {
            current = current.add(current.multiply(growthRate));
            points.add(ExtensionResponses.RevenueForecastPoint.builder()
                    .month(month.plusMonths(i + 1).toString())
                    .amount(current)
                    .build());
        }
        return ExtensionResponses.RevenueForecastResponse.builder().forecast(points).build();
    }
}
