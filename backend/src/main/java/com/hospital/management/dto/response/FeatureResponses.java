package com.hospital.management.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class FeatureResponses {
    private FeatureResponses() {
    }

    @Data
    @Builder
    public static class PaymentVerificationResponse {
        private Boolean valid;
        private String message;
    }

    @Data
    @Builder
    public static class MediaUploadResponse {
        private UUID mediaId;
        private String filePath;
        private String publicUrl;
        private String category;
    }

    @Data
    @Builder
    public static class AiSummaryResponse {
        private String summary;
        private String riskLevel;
    }

    @Data
    @Builder
    public static class VideoRoomResponse {
        private String roomUrl;
        private String roomName;
    }

    @Data
    @Builder
    public static class DoctorPerformanceRow {
        private UUID doctorId;
        private String doctorName;
        private Long appointments;
        private Double avgRating;
        private BigDecimal revenue;
        private Integer rank;
    }

    @Data
    @Builder
    public static class PatientDemographicsResponse {
        private Map<String, Long> genderDistribution;
        private Map<String, Long> ageBands;
        private Map<String, Long> locationDistribution;
    }

    @Data
    @Builder
    public static class ComplianceMetadataResponse {
        private String framework;
        private Map<String, Object> metadata;
    }

    @Data
    @Builder
    public static class FinancialReportResponse {
        private int financialYear;
        private BigDecimal revenue;
        private BigDecimal expense;
        private BigDecimal net;
    }

    @Data
    @Builder
    public static class MonthlyStatsResponse {
        private int year;
        private int month;
        private long appointments;
        private long patients;
        private BigDecimal revenue;
        private List<String> highlights;
    }
}
