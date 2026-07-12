package com.hospital.management.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class ExtensionResponses {
    private ExtensionResponses() {
    }

    @Data
    @Builder
    public static class PaymentGatewayResponse {
        private String gatewayOrderId;
        private String status;
        private String message;
    }

    @Data
    @Builder
    public static class ContactSupportResponse {
        private String ticketId;
        private String status;
    }

    @Data
    @Builder
    public static class DoctorAvailabilitySlot {
        private LocalTime startTime;
        private LocalTime endTime;
        private Boolean available;
    }

    @Data
    @Builder
    public static class DoctorAvailabilityCalendarResponse {
        private UUID doctorId;
        private LocalDate date;
        private List<DoctorAvailabilitySlot> slots;
    }

    @Data
    @Builder
    public static class SymptomSuggestionResponse {
        private List<String> suggestions;
        private String triageLevel;
    }

    @Data
    @Builder
    public static class ExportFileResponse {
        private String format;
        private byte[] content;
    }

    @Data
    @Builder
    public static class RevenueForecastPoint {
        private String month;
        private BigDecimal amount;
    }

    @Data
    @Builder
    public static class RevenueForecastResponse {
        private List<RevenueForecastPoint> forecast;
    }

    @Data
    @Builder
    public static class IcuStatusResponse {
        private Integer totalBeds;
        private Integer occupiedBeds;
        private Integer availableBeds;
        private Instant generatedAt;
    }

    @Data
    @Builder
    public static class DrugInteractionCheckResponse {
        private String severity;
        private String advisory;
    }

    @Data
    @Builder
    public static class OwnerAnalyticsResponse {
        private long totalPatients;
        private long totalAppointments;
        private long totalBills;
        private BigDecimal totalRevenue;
        private Map<String, Long> appointmentByStatus;
    }
}
