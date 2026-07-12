package com.hospital.management;

import com.hospital.management.domain.entity.*;
import com.hospital.management.domain.enums.*;
import com.hospital.management.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final ClinicRepository clinicRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final StaffRepository staffRepository;
    private final BloodInventoryRepository bloodInventoryRepository;
    private final TestimonialRepository testimonialRepository;
    private final FaqRepository faqRepository;
    private final NewsletterSubscriptionRepository newsletterSubscriptionRepository;
    private final HospitalGalleryRepository hospitalGalleryRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final TenantBrandingRepository tenantBrandingRepository;
    private final PermissionRepository permissionRepository;
    private final HospitalBranchRepository hospitalBranchRepository;
    private final DrugInteractionRepository drugInteractionRepository;
    private final StaffAttendanceRepository staffAttendanceRepository;
    private final InsuranceClaimRepository insuranceClaimRepository;
    private final PatientFeedbackRepository patientFeedbackRepository;
    private final MedicalImageRepository medicalImageRepository;
    private final OperationTheatreRepository operationTheatreRepository;
    private final SurgeryScheduleRepository surgeryScheduleRepository;
    private final OrganTransplantRegistryRepository organTransplantRegistryRepository;
    private final ConsentRecordRepository consentRecordRepository;
    private final PatientAccessLogRepository patientAccessLogRepository;
    private final CanteenMenuItemRepository canteenMenuItemRepository;
    private final ParkingSlotRepository parkingSlotRepository;
    private final AmbulanceRepository ambulanceRepository;
    private final PasswordEncoder passwordEncoder;
    private UUID defaultClinicId;

    @Override
    @Transactional
    public void run(String... args) {
        seedClinic();
        seedAdmin();
        seedDepartments();
        seedDoctors();
        seedPatients();
        seedBloodInventory();
        seedWebsiteContent();
        seedSaasAndAdminData();
        seedDrugInteractions();
        seedFeatureData();
        seedOperationsData();
        log.info("Data initialization completed");
    }

    private void seedClinic() {
        Clinic clinic = clinicRepository.findByCode("MAIN").orElseGet(() ->
                clinicRepository.save(Clinic.builder()
                        .code("MAIN")
                        .name("City Care Main Clinic")
                        .address("Anna Nagar, Chennai")
                        .city("Chennai")
                        .phone("+914400000001")
                        .latitude(13.0827)
                        .longitude(80.2707)
                        .active(true)
                        .build()));
        if (clinic.getLatitude() == null) {
            clinic.setLatitude(13.0827);
            clinic.setLongitude(80.2707);
            clinicRepository.save(clinic);
        }
        defaultClinicId = clinic.getId();
    }

    private void seedAdmin() {
        if (userRepository.existsByEmail("admin@hospital.com")) {
            return;
        }
        User admin = User.builder()
                .clinicId(defaultClinicId)
                .email("admin@hospital.com")
                .passwordHash(passwordEncoder.encode("Admin@123"))
                .role(UserRole.ADMIN)
                .enabled(true)
                .build();
        userRepository.save(admin);
        log.info("Seeded admin user: admin@hospital.com");
    }

    private void seedDepartments() {
        if (departmentRepository.count() > 0) {
            return;
        }
        List<Department> departments = List.of(
                Department.builder().clinicId(defaultClinicId).name("Cardiology").description("Heart and cardiovascular care").active(true).build(),
                Department.builder().clinicId(defaultClinicId).name("Neurology").description("Brain and nervous system").active(true).build(),
                Department.builder().clinicId(defaultClinicId).name("Orthopedics").description("Bone and joint care").active(true).build(),
                Department.builder().clinicId(defaultClinicId).name("General Medicine").description("Primary healthcare").active(true).build(),
                Department.builder().clinicId(defaultClinicId).name("Emergency").description("24/7 emergency care").active(true).build()
        );
        departmentRepository.saveAll(departments);
        log.info("Seeded {} departments", departments.size());
    }

    private void seedDoctors() {
        if (doctorRepository.count() > 0) {
            return;
        }
        Department cardiology = departmentRepository.findByName("Cardiology").orElse(null);
        Department neurology = departmentRepository.findByName("Neurology").orElse(null);

        createDoctor("dr.sharma@hospital.com", "Rajesh", "Sharma", "Cardiology",
                cardiology != null ? cardiology.getId() : null, "Interventional Cardiology",
                new BigDecimal("800.00"), 15);
        createDoctor("dr.patel@hospital.com", "Priya", "Patel", "Neurology",
                neurology != null ? neurology.getId() : null, "Neurology",
                new BigDecimal("750.00"), 12);
        createDoctor("dr.kumar@hospital.com", "Arun", "Kumar", "General Medicine",
                null, "General Physician", new BigDecimal("500.00"), 8);
        log.info("Seeded sample doctors");
    }

    private void createDoctor(String email, String firstName, String lastName, String specialization,
                              java.util.UUID departmentId, String qualification, BigDecimal fee, int experience) {
        if (userRepository.existsByEmail(email)) {
            return;
        }
        User user = User.builder()
                .clinicId(defaultClinicId)
                .email(email)
                .passwordHash(passwordEncoder.encode("Doctor@123"))
                .role(UserRole.DOCTOR)
                .enabled(true)
                .build();
        user = userRepository.save(user);

        Doctor doctor = Doctor.builder()
                .clinicId(defaultClinicId)
                .userId(user.getId())
                .departmentId(departmentId)
                .firstName(firstName)
                .lastName(lastName)
                .specialization(specialization)
                .qualification(qualification)
                .experienceYears(experience)
                .gender(Gender.MALE)
                .consultationFee(fee)
                .available(true)
                .build();
        doctorRepository.save(doctor);
    }

    private void seedPatients() {
        if (patientRepository.count() > 0) {
            return;
        }
        createPatient("patient1@hospital.com", "Anita", "Desai", "+919876543201",
                LocalDate.of(1990, 5, 15), Gender.FEMALE, BloodGroup.A_POSITIVE);
        createPatient("patient2@hospital.com", "Ravi", "Menon", "+919876543202",
                LocalDate.of(1985, 8, 22), Gender.MALE, BloodGroup.O_POSITIVE);
        createPatient("john.doe@email.com", "John", "Doe", "+919876543203",
                LocalDate.of(1995, 3, 10), Gender.MALE, BloodGroup.B_POSITIVE);
        log.info("Seeded sample patients");
    }

    private void createPatient(String email, String firstName, String lastName, String phone,
                               LocalDate dob, Gender gender, BloodGroup bloodGroup) {
        if (userRepository.existsByEmail(email)) {
            return;
        }
        User user = User.builder()
                .clinicId(defaultClinicId)
                .email(email)
                .passwordHash(passwordEncoder.encode("Patient@123"))
                .role(UserRole.PATIENT)
                .enabled(true)
                .build();
        user = userRepository.save(user);

        Patient patient = Patient.builder()
                .clinicId(defaultClinicId)
                .userId(user.getId())
                .firstName(firstName)
                .lastName(lastName)
                .phone(phone)
                .dateOfBirth(dob)
                .gender(gender)
                .bloodGroup(bloodGroup)
                .address("Chennai, Tamil Nadu")
                .emergencyContact("+919999999999")
                .build();
        patientRepository.save(patient);
    }

    private void seedBloodInventory() {
        if (bloodInventoryRepository.count() > 0) {
            return;
        }
        for (BloodGroup group : BloodGroup.values()) {
            BloodInventory inventory = BloodInventory.builder()
                    .bloodGroup(group)
                    .unitsAvailable(50)
                    .build();
            bloodInventoryRepository.save(inventory);
        }
        log.info("Seeded blood inventory");
    }

    private void seedWebsiteContent() {
        if (testimonialRepository.count() == 0) {
            testimonialRepository.save(Testimonial.builder()
                    .patientName("Anita Desai")
                    .message("Excellent emergency and follow-up care.")
                    .rating(5)
                    .approved(true)
                    .build());
        }
        if (faqRepository.count() == 0) {
            faqRepository.save(Faq.builder()
                    .question("How to book an appointment?")
                    .answer("Use patient app or call reception.")
                    .sortOrder(1)
                    .active(true)
                    .build());
        }
        if (newsletterSubscriptionRepository.count() == 0) {
            newsletterSubscriptionRepository.save(NewsletterSubscription.builder()
                    .email("news@hospital.com")
                    .active(true)
                    .build());
        }
        if (hospitalGalleryRepository.count() == 0) {
            hospitalGalleryRepository.save(HospitalGallery.builder()
                    .title("ICU Virtual Tour")
                    .description("360 degree ICU unit")
                    .imageUrl("https://example.com/gallery/icu.jpg")
                    .active(true)
                    .build());
        }
    }

    private void seedSaasAndAdminData() {
        if (subscriptionPlanRepository.count() == 0) {
            subscriptionPlanRepository.saveAll(List.of(
                    SubscriptionPlan.builder().code("BASIC").name("Basic").monthlyPrice(new BigDecimal("9999")).maxUsers(25).build(),
                    SubscriptionPlan.builder().code("PRO").name("Pro").monthlyPrice(new BigDecimal("24999")).maxUsers(100).build()
            ));
        }
        if (tenantBrandingRepository.count() == 0) {
            tenantBrandingRepository.save(TenantBranding.builder()
                    .clinicId(defaultClinicId)
                    .hospitalName("City Care Hospital")
                    .logoUrl("https://example.com/logo.png")
                    .primaryColor("#0055AA")
                    .secondaryColor("#00AA88")
                    .build());
        }
        if (permissionRepository.count() == 0) {
            permissionRepository.saveAll(List.of(
                    Permission.builder().code("APPOINTMENT_MANAGE").name("Manage Appointments").description("Create and update appointments").build(),
                    Permission.builder().code("BILLING_VIEW").name("View Billing").description("View billing data").build()
            ));
        }
        if (hospitalBranchRepository.count() == 0) {
            hospitalBranchRepository.save(HospitalBranch.builder()
                    .name("Main Branch")
                    .city("Chennai")
                    .address("Anna Nagar, Chennai")
                    .contactNumber("+914400000001")
                    .active(true)
                    .build());
        }
    }

    private void seedDrugInteractions() {
        if (drugInteractionRepository.count() > 0) {
            return;
        }
        drugInteractionRepository.save(DrugInteraction.builder()
                .drugA("Aspirin")
                .drugB("Warfarin")
                .severity("HIGH")
                .advisory("Risk of bleeding increases; monitor INR closely.")
                .build());
    }

    private void seedFeatureData() {
        List<Staff> staffList = staffRepository.findAll();
        List<Patient> patientList = patientRepository.findAll();
        List<Doctor> doctorList = doctorRepository.findAll();

        if (!staffList.isEmpty() && staffAttendanceRepository.count() == 0) {
            staffAttendanceRepository.save(StaffAttendance.builder()
                    .clinicId(defaultClinicId)
                    .staffId(staffList.get(0).getId())
                    .biometricId("BIO-1001")
                    .checkIn(Instant.now().minusSeconds(8 * 3600))
                    .checkOut(Instant.now().minusSeconds(3600))
                    .method(AttendanceMethod.FINGERPRINT)
                    .build());
        }

        if (!patientList.isEmpty() && insuranceClaimRepository.count() == 0) {
            insuranceClaimRepository.save(InsuranceClaim.builder()
                    .clinicId(defaultClinicId)
                    .patientId(patientList.get(0).getId())
                    .insuranceProvider("Star Health")
                    .policyNumber("POL-2026-100")
                    .claimAmount(new BigDecimal("12000"))
                    .status(InsuranceClaimStatus.SUBMITTED)
                    .build());
        }

        if (!patientList.isEmpty() && !doctorList.isEmpty() && patientFeedbackRepository.count() == 0) {
            patientFeedbackRepository.save(PatientFeedback.builder()
                    .clinicId(defaultClinicId)
                    .patientId(patientList.get(0).getId())
                    .doctorId(doctorList.get(0).getId())
                    .rating(5)
                    .review("Very supportive consultation.")
                    .build());
        }

        if (!patientList.isEmpty() && medicalImageRepository.count() == 0) {
            medicalImageRepository.save(MedicalImage.builder()
                    .clinicId(defaultClinicId)
                    .patientId(patientList.get(0).getId())
                    .imageUrl("https://example.com/images/xray-1001.png")
                    .imageType("X_RAY")
                    .notes("Baseline chest X-ray")
                    .build());
        }

        if (operationTheatreRepository.count() == 0) {
            OperationTheatre theatre = operationTheatreRepository.save(OperationTheatre.builder()
                    .clinicId(defaultClinicId)
                    .theatreCode("OT-1")
                    .name("Main Operation Theatre")
                    .floorNumber(1)
                    .active(true)
                    .build());
            if (!patientList.isEmpty() && !doctorList.isEmpty()) {
                surgeryScheduleRepository.save(SurgerySchedule.builder()
                        .clinicId(defaultClinicId)
                        .patientId(patientList.get(0).getId())
                        .doctorId(doctorList.get(0).getId())
                        .operationTheatreId(theatre.getId())
                        .surgeryName("Appendectomy")
                        .surgeryDate(LocalDate.now().plusDays(2))
                        .startTime(LocalTime.of(10, 0))
                        .endTime(LocalTime.of(11, 30))
                        .status(SurgeryStatus.SCHEDULED)
                        .notes("Pre-op tests completed")
                        .build());
            }
        }

        if (organTransplantRegistryRepository.count() == 0) {
            organTransplantRegistryRepository.save(OrganTransplantRegistry.builder()
                    .clinicId(defaultClinicId)
                    .donorName("K. Rajan")
                    .recipientName("M. Devi")
                    .organType("Kidney")
                    .status(TransplantStatus.REGISTERED)
                    .notes("Crossmatch pending")
                    .build());
        }

        if (!patientList.isEmpty() && consentRecordRepository.count() == 0) {
            consentRecordRepository.save(ConsentRecord.builder()
                    .clinicId(defaultClinicId)
                    .patientId(patientList.get(0).getId())
                    .consentType("DATA_SHARING")
                    .granted(true)
                    .metadataJson("{\"source\":\"initializer\"}")
                    .build());
        }

        if (!patientList.isEmpty() && !userRepository.findAll().isEmpty() && patientAccessLogRepository.count() == 0) {
            patientAccessLogRepository.save(PatientAccessLog.builder()
                    .clinicId(defaultClinicId)
                    .patientId(patientList.get(0).getId())
                    .viewerUserId(userRepository.findAll().get(0).getId())
                    .viewerRole("ADMIN")
                    .accessedAt(Instant.now())
                    .action("VIEW_MEDICAL_RECORD")
                    .build());
        }
    }

    private void seedOperationsData() {
        if (canteenMenuItemRepository.count() == 0) {
            canteenMenuItemRepository.saveAll(List.of(
                    CanteenMenuItem.builder().clinicId(defaultClinicId).name("Idli Sambar").description("Soft idlis with sambar").price(new BigDecimal("60")).category("Breakfast").veg(true).available(true).build(),
                    CanteenMenuItem.builder().clinicId(defaultClinicId).name("Veg Thali").description("Rice, dal, sabzi, roti").price(new BigDecimal("120")).category("Lunch").veg(true).available(true).build(),
                    CanteenMenuItem.builder().clinicId(defaultClinicId).name("Chicken Soup").description("Light protein soup").price(new BigDecimal("90")).category("Dinner").veg(false).available(true).build(),
                    CanteenMenuItem.builder().clinicId(defaultClinicId).name("Fruit Bowl").description("Seasonal fresh fruits").price(new BigDecimal("80")).category("Snacks").veg(true).available(true).build()
            ));
        }

        if (parkingSlotRepository.count() == 0) {
            parkingSlotRepository.saveAll(List.of(
                    ParkingSlot.builder().clinicId(defaultClinicId).slotNumber("P-A01").floorLevel("Ground").slotType(ParkingSlotType.VISITOR).status(ParkingSlotStatus.AVAILABLE).build(),
                    ParkingSlot.builder().clinicId(defaultClinicId).slotNumber("P-A02").floorLevel("Ground").slotType(ParkingSlotType.VISITOR).status(ParkingSlotStatus.AVAILABLE).build(),
                    ParkingSlot.builder().clinicId(defaultClinicId).slotNumber("P-B01").floorLevel("Basement").slotType(ParkingSlotType.STAFF).status(ParkingSlotStatus.AVAILABLE).build(),
                    ParkingSlot.builder().clinicId(defaultClinicId).slotNumber("P-D01").floorLevel("Ground").slotType(ParkingSlotType.DISABLED).status(ParkingSlotStatus.AVAILABLE).build()
            ));
        }

        if (ambulanceRepository.count() == 0) {
            ambulanceRepository.saveAll(List.of(
                    Ambulance.builder().vehicleNumber("TN-01-AMB-001").driverName("Ramesh Kumar").driverPhone("+919876543210").location("Anna Salai").latitude(13.0604).longitude(80.2496).destinationLat(13.0827).destinationLng(80.2707).available(true).build(),
                    Ambulance.builder().vehicleNumber("TN-01-AMB-002").driverName("Suresh Patel").driverPhone("+919876543211").location("T Nagar").latitude(13.0418).longitude(80.2341).destinationLat(13.0827).destinationLng(80.2707).available(true).build()
            ));
        }
    }
}
