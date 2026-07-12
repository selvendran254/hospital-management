package com.hospital.management.service;

import com.hospital.management.domain.entity.*;
import com.hospital.management.dto.request.ExtensionRequests;
import com.hospital.management.dto.response.ExtensionResponses;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ExtensionCrudService {

    private final TestimonialRepository testimonialRepository;
    private final FaqRepository faqRepository;
    private final NewsletterSubscriptionRepository newsletterSubscriptionRepository;
    private final HospitalGalleryRepository hospitalGalleryRepository;
    private final QueueTokenRepository queueTokenRepository;
    private final FamilyMemberRepository familyMemberRepository;
    private final RecurringAppointmentRepository recurringAppointmentRepository;
    private final DoctorAvailabilityRepository doctorAvailabilityRepository;
    private final HealthTrackerRecordRepository healthTrackerRecordRepository;
    private final MedicineReminderRepository medicineReminderRepository;
    private final InsurancePolicyRepository insurancePolicyRepository;
    private final VaccinationRecordRepository vaccinationRecordRepository;
    private final PrescriptionTemplateRepository prescriptionTemplateRepository;
    private final TelemedicineSessionRepository telemedicineSessionRepository;
    private final DoctorNoteRepository doctorNoteRepository;
    private final ReferralRepository referralRepository;
    private final PayrollRecordRepository payrollRecordRepository;
    private final HospitalBranchRepository hospitalBranchRepository;
    private final PermissionRepository permissionRepository;
    private final AmbulanceTrackingRepository ambulanceTrackingRepository;
    private final EmergencyAlertRepository emergencyAlertRepository;
    private final TraumaAlertRepository traumaAlertRepository;
    private final DrugInteractionRepository drugInteractionRepository;
    private final MedicineOrderRepository medicineOrderRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final TenantBrandingRepository tenantBrandingRepository;
    private final OnboardingStepRepository onboardingStepRepository;
    private final AppointmentRepository appointmentRepository;
    private final ClinicContextService clinicContextService;

    @Transactional
    public Testimonial createTestimonial(ExtensionRequests.TestimonialRequest request) {
        Testimonial entity = Testimonial.builder()
                .patientName(request.getPatientName())
                .message(request.getMessage())
                .rating(request.getRating() == null ? 5 : request.getRating())
                .approved(request.getApproved() == null || request.getApproved())
                .build();
        return testimonialRepository.save(entity);
    }

    public List<Testimonial> getTestimonials() {
        return testimonialRepository.findAll();
    }

    @Transactional
    public Testimonial updateTestimonial(UUID id, ExtensionRequests.TestimonialRequest request) {
        Testimonial t = testimonialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Testimonial not found: " + id));
        t.setPatientName(request.getPatientName());
        t.setMessage(request.getMessage());
        t.setRating(request.getRating() == null ? 5 : request.getRating());
        if (request.getApproved() != null) {
            t.setApproved(request.getApproved());
        }
        return testimonialRepository.save(t);
    }

    @Transactional
    public void deleteTestimonial(UUID id) {
        testimonialRepository.deleteById(id);
    }

    @Transactional
    public Faq createFaq(ExtensionRequests.FaqRequest request) {
        Faq faq = Faq.builder()
                .question(request.getQuestion())
                .answer(request.getAnswer())
                .sortOrder(request.getSortOrder() == null ? 0 : request.getSortOrder())
                .active(request.getActive() == null || request.getActive())
                .build();
        return faqRepository.save(faq);
    }

    public List<Faq> getFaqs() {
        return faqRepository.findAll();
    }

    @Transactional
    public Faq updateFaq(UUID id, ExtensionRequests.FaqRequest request) {
        Faq faq = faqRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FAQ not found: " + id));
        faq.setQuestion(request.getQuestion());
        faq.setAnswer(request.getAnswer());
        if (request.getSortOrder() != null) faq.setSortOrder(request.getSortOrder());
        if (request.getActive() != null) faq.setActive(request.getActive());
        return faqRepository.save(faq);
    }

    @Transactional
    public void deleteFaq(UUID id) {
        faqRepository.deleteById(id);
    }

    @Transactional
    public NewsletterSubscription createNewsletterSubscription(ExtensionRequests.NewsletterSubscriptionRequest request) {
        NewsletterSubscription entity = newsletterSubscriptionRepository.findByEmailIgnoreCase(request.getEmail())
                .orElse(NewsletterSubscription.builder().email(request.getEmail()).build());
        entity.setActive(request.getActive() == null || request.getActive());
        return newsletterSubscriptionRepository.save(entity);
    }

    public List<NewsletterSubscription> getNewsletterSubscriptions() {
        return newsletterSubscriptionRepository.findAll();
    }

    @Transactional
    public void deleteNewsletterSubscription(UUID id) {
        newsletterSubscriptionRepository.deleteById(id);
    }

    @Transactional
    public HospitalGallery createGallery(ExtensionRequests.HospitalGalleryRequest request) {
        return hospitalGalleryRepository.save(HospitalGallery.builder()
                .imageUrl(request.getImageUrl())
                .title(request.getTitle())
                .description(request.getDescription())
                .active(request.getActive() == null || request.getActive())
                .build());
    }

    public List<HospitalGallery> getGallery() {
        return hospitalGalleryRepository.findAll();
    }

    @Transactional
    public QueueToken issueQueueToken(ExtensionRequests.QueueTokenRequest request) {
        int next = queueTokenRepository.findByDoctorIdAndQueueDateOrderByTokenNumber(
                request.getDoctorId(), request.getQueueDate()).size() + 1;
        return queueTokenRepository.save(QueueToken.builder()
                .doctorId(request.getDoctorId())
                .patientId(request.getPatientId())
                .queueDate(request.getQueueDate())
                .tokenNumber(next)
                .status("WAITING")
                .build());
    }

    public List<QueueToken> getQueue(UUID doctorId, LocalDate date) {
        return queueTokenRepository.findByDoctorIdAndQueueDateOrderByTokenNumber(doctorId, date);
    }

    @Transactional
    public FamilyMember createFamilyMember(ExtensionRequests.FamilyMemberRequest request) {
        return familyMemberRepository.save(FamilyMember.builder()
                .patientId(request.getPatientId())
                .name(request.getName())
                .relationship(request.getRelationship())
                .phone(request.getPhone())
                .dateOfBirth(request.getDateOfBirth())
                .build());
    }

    public List<FamilyMember> getFamilyMembers(UUID patientId) {
        return familyMemberRepository.findByPatientId(patientId);
    }

    @Transactional
    public RecurringAppointment createRecurringAppointment(ExtensionRequests.RecurringAppointmentRequest request) {
        return recurringAppointmentRepository.save(RecurringAppointment.builder()
                .patientId(request.getPatientId())
                .doctorId(request.getDoctorId())
                .frequency(request.getFrequency())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .active(true)
                .build());
    }

    public List<RecurringAppointment> getRecurringAppointments(UUID doctorId) {
        return recurringAppointmentRepository.findByDoctorIdAndActiveTrue(doctorId);
    }

    public ExtensionResponses.DoctorAvailabilityCalendarResponse getAvailability(UUID doctorId, LocalDate date) {
        int dayOfWeek = date.getDayOfWeek().getValue();
        List<DoctorAvailability> ranges = doctorAvailabilityRepository.findByDoctorId(doctorId).stream()
                .filter(d -> d.getDayOfWeek() == dayOfWeek)
                .toList();

        Set<String> booked = new HashSet<>();
        appointmentRepository.findByDoctorIdAndAppointmentDate(doctorId, date)
                .forEach(a -> booked.add(a.getStartTime() + "-" + a.getEndTime()));

        List<ExtensionResponses.DoctorAvailabilitySlot> slots = new ArrayList<>();
        for (DoctorAvailability range : ranges) {
            LocalTime cursor = range.getStartTime();
            while (cursor.plusMinutes(range.getSlotDurationMins()).compareTo(range.getEndTime()) <= 0) {
                LocalTime end = cursor.plusMinutes(range.getSlotDurationMins());
                String key = cursor + "-" + end;
                slots.add(ExtensionResponses.DoctorAvailabilitySlot.builder()
                        .startTime(cursor)
                        .endTime(end)
                        .available(!booked.contains(key))
                        .build());
                cursor = end;
            }
        }
        return ExtensionResponses.DoctorAvailabilityCalendarResponse.builder()
                .doctorId(doctorId)
                .date(date)
                .slots(slots)
                .build();
    }

    @Transactional
    public HealthTrackerRecord createHealthTracker(ExtensionRequests.HealthTrackerRequest request) {
        return healthTrackerRecordRepository.save(HealthTrackerRecord.builder()
                .patientId(request.getPatientId())
                .recordDate(request.getRecordDate())
                .systolicBp(request.getSystolicBp())
                .diastolicBp(request.getDiastolicBp())
                .sugarLevel(request.getSugarLevel())
                .weight(request.getWeight())
                .build());
    }

    public List<HealthTrackerRecord> getHealthTracker(UUID patientId) {
        return healthTrackerRecordRepository.findByPatientIdOrderByRecordDateDesc(patientId);
    }

    @Transactional
    public MedicineReminder createMedicineReminder(ExtensionRequests.MedicineReminderRequest request) {
        return medicineReminderRepository.save(MedicineReminder.builder()
                .patientId(request.getPatientId())
                .medicineName(request.getMedicineName())
                .dosage(request.getDosage())
                .reminderTime(request.getReminderTime())
                .active(request.getActive() == null || request.getActive())
                .build());
    }

    @Transactional
    public InsurancePolicy createInsurancePolicy(ExtensionRequests.InsurancePolicyRequest request) {
        return insurancePolicyRepository.save(InsurancePolicy.builder()
                .patientId(request.getPatientId())
                .providerName(request.getProviderName())
                .policyNumber(request.getPolicyNumber())
                .validTill(request.getValidTill())
                .active(request.getActive() == null || request.getActive())
                .build());
    }

    @Transactional
    public VaccinationRecord createVaccinationRecord(ExtensionRequests.VaccinationRecordRequest request) {
        return vaccinationRecordRepository.save(VaccinationRecord.builder()
                .patientId(request.getPatientId())
                .vaccineName(request.getVaccineName())
                .doseNumber(request.getDoseNumber())
                .vaccinationDate(request.getVaccinationDate())
                .nextDueDate(request.getNextDueDate())
                .build());
    }

    @Transactional
    public PrescriptionTemplate createPrescriptionTemplate(ExtensionRequests.PrescriptionTemplateRequest request) {
        return prescriptionTemplateRepository.save(PrescriptionTemplate.builder()
                .doctorId(request.getDoctorId())
                .name(request.getName())
                .content(request.getContent())
                .build());
    }

    @Transactional
    public TelemedicineSession createTelemedicineSession(ExtensionRequests.TelemedicineSessionRequest request) {
        return telemedicineSessionRepository.save(TelemedicineSession.builder()
                .appointmentId(request.getAppointmentId())
                .meetingUrl(request.getMeetingUrl())
                .build());
    }

    @Transactional
    public DoctorNote createDoctorNote(ExtensionRequests.DoctorNoteRequest request) {
        return doctorNoteRepository.save(DoctorNote.builder()
                .doctorId(request.getDoctorId())
                .patientId(request.getPatientId())
                .noteText(request.getNoteText())
                .voiceNoteUrl(request.getVoiceNoteUrl())
                .build());
    }

    @Transactional
    public Referral createReferral(ExtensionRequests.ReferralRequest request) {
        return referralRepository.save(Referral.builder()
                .fromDoctorId(request.getFromDoctorId())
                .toDoctorId(request.getToDoctorId())
                .patientId(request.getPatientId())
                .reason(request.getReason())
                .referralDate(request.getReferralDate())
                .build());
    }

    @Transactional
    public PayrollRecord createPayroll(ExtensionRequests.PayrollRecordRequest request) {
        return payrollRecordRepository.save(PayrollRecord.builder()
                .staffId(request.getStaffId())
                .payDate(request.getPayDate())
                .basicSalary(request.getBasicSalary())
                .bonus(request.getBonus() == null ? BigDecimal.ZERO : request.getBonus())
                .deductions(request.getDeductions() == null ? BigDecimal.ZERO : request.getDeductions())
                .build());
    }

    @Transactional
    public HospitalBranch createBranch(ExtensionRequests.HospitalBranchRequest request) {
        return hospitalBranchRepository.save(HospitalBranch.builder()
                .name(request.getName())
                .city(request.getCity())
                .address(request.getAddress())
                .contactNumber(request.getContactNumber())
                .active(request.getActive() == null || request.getActive())
                .build());
    }

    @Transactional
    public Permission createPermission(ExtensionRequests.PermissionRequest request) {
        return permissionRepository.save(Permission.builder()
                .code(request.getCode())
                .name(request.getName())
                .description(request.getDescription())
                .build());
    }

    @Transactional
    public AmbulanceTracking createAmbulanceTracking(ExtensionRequests.AmbulanceTrackingRequest request) {
        return ambulanceTrackingRepository.save(AmbulanceTracking.builder()
                .ambulanceId(request.getAmbulanceId())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .statusMessage(request.getStatusMessage())
                .build());
    }

    @Transactional
    public EmergencyAlert createEmergencyAlert(ExtensionRequests.EmergencyAlertRequest request) {
        return emergencyAlertRepository.save(EmergencyAlert.builder()
                .patientId(request.getPatientId())
                .message(request.getMessage())
                .severity(request.getSeverity() == null ? "HIGH" : request.getSeverity())
                .resolved(false)
                .build());
    }

    @Transactional
    public TraumaAlert createTraumaAlert(ExtensionRequests.TraumaAlertRequest request) {
        return traumaAlertRepository.save(TraumaAlert.builder()
                .location(request.getLocation())
                .details(request.getDetails())
                .priority(request.getPriority() == null ? "CRITICAL" : request.getPriority())
                .build());
    }

    @Transactional
    public DrugInteraction createDrugInteraction(ExtensionRequests.DrugInteractionRequest request) {
        return drugInteractionRepository.save(DrugInteraction.builder()
                .drugA(request.getDrugA())
                .drugB(request.getDrugB())
                .advisory(request.getAdvisory())
                .severity(request.getSeverity() == null ? "MEDIUM" : request.getSeverity())
                .build());
    }

    @Transactional
    public MedicineOrder createMedicineOrder(ExtensionRequests.MedicineOrderRequest request) {
        return medicineOrderRepository.save(MedicineOrder.builder()
                .patientId(request.getPatientId())
                .medicineId(request.getMedicineId())
                .quantity(request.getQuantity())
                .totalAmount(request.getTotalAmount())
                .status("PLACED")
                .build());
    }

    @Transactional
    public SubscriptionPlan createSubscriptionPlan(ExtensionRequests.SubscriptionPlanRequest request) {
        return subscriptionPlanRepository.save(SubscriptionPlan.builder()
                .code(request.getCode())
                .name(request.getName())
                .monthlyPrice(request.getMonthlyPrice())
                .maxUsers(request.getMaxUsers())
                .build());
    }

    @Transactional
    public TenantBranding createTenantBranding(ExtensionRequests.TenantBrandingRequest request) {
        return tenantBrandingRepository.save(TenantBranding.builder()
                .clinicId(clinicContextService.resolveClinicId())
                .hospitalName(request.getHospitalName())
                .logoUrl(request.getLogoUrl())
                .primaryColor(request.getPrimaryColor())
                .secondaryColor(request.getSecondaryColor())
                .build());
    }

    @Transactional
    public OnboardingStep createOnboardingStep(ExtensionRequests.OnboardingStepRequest request) {
        OnboardingStep step = OnboardingStep.builder()
                .tenantId(request.getTenantId())
                .stepKey(request.getStepKey())
                .completed(request.getCompleted() != null && request.getCompleted())
                .build();
        if (Boolean.TRUE.equals(step.getCompleted())) {
            step.setCompletedAt(java.time.Instant.now());
        }
        return onboardingStepRepository.save(step);
    }
}
