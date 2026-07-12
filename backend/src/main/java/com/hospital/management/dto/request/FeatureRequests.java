package com.hospital.management.dto.request;

import com.hospital.management.domain.enums.AttendanceMethod;
import com.hospital.management.domain.enums.InsuranceClaimStatus;
import com.hospital.management.domain.enums.MediaCategory;
import com.hospital.management.domain.enums.SurgeryStatus;
import com.hospital.management.domain.enums.TransplantStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public final class FeatureRequests {
    private FeatureRequests() {
    }

    @Data
    public static class VerifyPaymentRequest {
        @NotBlank
        private String orderId;
        @NotBlank
        private String paymentId;
        @NotBlank
        private String signature;
    }

    @Data
    public static class SmsRequest {
        @NotBlank
        private String to;
        @NotBlank
        private String message;
        private Boolean whatsapp;
    }

    @Data
    public static class AttendanceRequest {
        @NotNull
        private UUID staffId;
        private String biometricId;
        @NotNull
        private Instant checkIn;
        private Instant checkOut;
        @NotNull
        private AttendanceMethod method;
    }

    @Data
    public static class InsuranceClaimRequest {
        @NotNull
        private UUID patientId;
        private UUID billId;
        @NotBlank
        private String insuranceProvider;
        @NotBlank
        private String policyNumber;
        @NotNull
        @DecimalMin("1.0")
        private BigDecimal claimAmount;
    }

    @Data
    public static class InsuranceClaimStatusRequest {
        @NotNull
        private InsuranceClaimStatus status;
        private String reviewNotes;
    }

    @Data
    public static class PatientFeedbackRequest {
        @NotNull
        private UUID patientId;
        private UUID doctorId;
        @NotNull
        @Min(1)
        @Max(5)
        private Integer rating;
        @Size(max = 2000)
        private String review;
    }

    @Data
    public static class MediaUploadMetaRequest {
        @NotNull
        private MediaCategory category;
    }

    @Data
    public static class MedicalImageRequest {
        @NotNull
        private UUID patientId;
        @NotBlank
        private String imageUrl;
        @NotBlank
        private String imageType;
        private String notes;
    }

    @Data
    public static class LabResultRequest {
        @NotBlank
        private String patientName;
        @NotEmpty
        private List<String> observations;
        @NotEmpty
        private List<String> abnormalValues;
    }

    @Data
    public static class OperationTheatreRequest {
        @NotBlank
        private String theatreCode;
        @NotBlank
        private String name;
        private Integer floorNumber;
    }

    @Data
    public static class SurgeryScheduleRequest {
        @NotNull
        private UUID patientId;
        @NotNull
        private UUID doctorId;
        @NotNull
        private UUID operationTheatreId;
        @NotBlank
        private String surgeryName;
        @NotNull
        private LocalDate surgeryDate;
        @NotNull
        private LocalTime startTime;
        @NotNull
        private LocalTime endTime;
        private SurgeryStatus status;
        private String notes;
    }

    @Data
    public static class TransplantRegistryRequest {
        @NotBlank
        private String donorName;
        @NotBlank
        private String recipientName;
        @NotBlank
        private String organType;
        private TransplantStatus status;
        private String notes;
    }

    @Data
    public static class VideoRoomRequest {
        @NotNull
        private UUID appointmentId;
        @NotBlank
        private String participantName;
    }

    @Data
    public static class TwoFactorVerifyRequest {
        @NotBlank
        private String otp;
    }

    @Data
    public static class ConsentRecordRequest {
        @NotNull
        private UUID patientId;
        @NotBlank
        private String consentType;
        @NotNull
        private Boolean granted;
        private String metadataJson;
    }

    @Data
    public static class PatientAccessLogRequest {
        @NotNull
        private UUID patientId;
        @NotNull
        private UUID viewerUserId;
        private String viewerRole;
        private String action;
    }
}
