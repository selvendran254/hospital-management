package com.hospital.management.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public final class ExtensionRequests {
    private ExtensionRequests() {
    }

    @Data
    public static class PaymentRequest {
        @NotNull
        private UUID billId;
        @NotNull
        @DecimalMin("1.0")
        private BigDecimal amount;
        @NotBlank
        private String currency;
    }

    @Data
    public static class ContactSupportRequest {
        @NotBlank
        private String name;
        @Email
        @NotBlank
        private String email;
        @NotBlank
        private String message;
        private Boolean chatRequest;
    }

    @Data
    public static class TestimonialRequest {
        @NotBlank
        private String patientName;
        @NotBlank
        private String message;
        @Min(1)
        @Max(5)
        private Integer rating;
        private Boolean approved;
    }

    @Data
    public static class FaqRequest {
        @NotBlank
        private String question;
        @NotBlank
        private String answer;
        private Integer sortOrder;
        private Boolean active;
    }

    @Data
    public static class NewsletterSubscriptionRequest {
        @Email
        @NotBlank
        private String email;
        private Boolean active;
    }

    @Data
    public static class HospitalGalleryRequest {
        @NotBlank
        private String imageUrl;
        @NotBlank
        private String title;
        private String description;
        private Boolean active;
    }

    @Data
    public static class QueueTokenRequest {
        @NotNull
        private UUID doctorId;
        @NotNull
        private UUID patientId;
        @NotNull
        private LocalDate queueDate;
    }

    @Data
    public static class FamilyMemberRequest {
        @NotNull
        private UUID patientId;
        @NotBlank
        private String name;
        @NotBlank
        private String relationship;
        private String phone;
        private LocalDate dateOfBirth;
    }

    @Data
    public static class RecurringAppointmentRequest {
        @NotNull
        private UUID patientId;
        @NotNull
        private UUID doctorId;
        @NotBlank
        private String frequency;
        @NotNull
        private LocalDate startDate;
        private LocalDate endDate;
        @NotNull
        private LocalTime startTime;
        @NotNull
        private LocalTime endTime;
    }

    @Data
    public static class HealthTrackerRequest {
        @NotNull
        private UUID patientId;
        @NotNull
        private LocalDate recordDate;
        private Integer systolicBp;
        private Integer diastolicBp;
        private BigDecimal sugarLevel;
        private BigDecimal weight;
    }

    @Data
    public static class MedicineReminderRequest {
        @NotNull
        private UUID patientId;
        @NotBlank
        private String medicineName;
        @NotBlank
        private String dosage;
        @NotNull
        private LocalTime reminderTime;
        private Boolean active;
    }

    @Data
    public static class InsurancePolicyRequest {
        @NotNull
        private UUID patientId;
        @NotBlank
        private String providerName;
        @NotBlank
        private String policyNumber;
        private LocalDate validTill;
        private Boolean active;
    }

    @Data
    public static class VaccinationRecordRequest {
        @NotNull
        private UUID patientId;
        @NotBlank
        private String vaccineName;
        private Integer doseNumber;
        private LocalDate vaccinationDate;
        private LocalDate nextDueDate;
    }

    @Data
    public static class PrescriptionTemplateRequest {
        @NotNull
        private UUID doctorId;
        @NotBlank
        private String name;
        @NotBlank
        private String content;
    }

    @Data
    public static class TelemedicineSessionRequest {
        @NotNull
        private UUID appointmentId;
        @NotBlank
        private String meetingUrl;
    }

    @Data
    public static class DoctorNoteRequest {
        @NotNull
        private UUID doctorId;
        @NotNull
        private UUID patientId;
        @NotBlank
        private String noteText;
        private String voiceNoteUrl;
    }

    @Data
    public static class ReferralRequest {
        @NotNull
        private UUID fromDoctorId;
        @NotNull
        private UUID toDoctorId;
        @NotNull
        private UUID patientId;
        @NotBlank
        private String reason;
        @NotNull
        private LocalDate referralDate;
    }

    @Data
    public static class PayrollRecordRequest {
        @NotNull
        private UUID staffId;
        @NotNull
        private LocalDate payDate;
        @NotNull
        private BigDecimal basicSalary;
        private BigDecimal bonus;
        private BigDecimal deductions;
    }

    @Data
    public static class HospitalBranchRequest {
        @NotBlank
        private String name;
        @NotBlank
        private String city;
        private String address;
        private String contactNumber;
        private Boolean active;
    }

    @Data
    public static class PermissionRequest {
        @NotBlank
        private String code;
        @NotBlank
        private String name;
        private String description;
    }

    @Data
    public static class AmbulanceTrackingRequest {
        @NotNull
        private UUID ambulanceId;
        @NotNull
        private Double latitude;
        @NotNull
        private Double longitude;
        private String statusMessage;
    }

    @Data
    public static class EmergencyAlertRequest {
        private UUID patientId;
        @NotBlank
        private String message;
        private String severity;
    }

    @Data
    public static class TraumaAlertRequest {
        @NotBlank
        private String location;
        @NotBlank
        private String details;
        private String priority;
    }

    @Data
    public static class DrugInteractionRequest {
        @NotBlank
        private String drugA;
        @NotBlank
        private String drugB;
        @NotBlank
        private String advisory;
        private String severity;
    }

    @Data
    public static class MedicineOrderRequest {
        @NotNull
        private UUID patientId;
        @NotNull
        private UUID medicineId;
        @NotNull
        @Min(1)
        private Integer quantity;
        @NotNull
        @DecimalMin("0.1")
        private BigDecimal totalAmount;
    }

    @Data
    public static class SubscriptionPlanRequest {
        @NotBlank
        private String code;
        @NotBlank
        private String name;
        @NotNull
        private BigDecimal monthlyPrice;
        @NotNull
        @Min(1)
        private Integer maxUsers;
    }

    @Data
    public static class TenantBrandingRequest {
        @NotBlank
        private String hospitalName;
        private String logoUrl;
        private String primaryColor;
        private String secondaryColor;
    }

    @Data
    public static class OnboardingStepRequest {
        @NotNull
        private UUID tenantId;
        @NotBlank
        private String stepKey;
        private Boolean completed;
    }
}
