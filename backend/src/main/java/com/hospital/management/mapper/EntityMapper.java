package com.hospital.management.mapper;

import com.hospital.management.domain.entity.*;
import com.hospital.management.dto.response.*;
import com.hospital.management.repository.DepartmentRepository;
import com.hospital.management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EntityMapper {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .enabled(user.getEnabled())
                .profileImageUrl(user.getProfileImageUrl())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public DepartmentResponse toDepartmentResponse(Department department) {
        return DepartmentResponse.builder()
                .id(department.getId())
                .name(department.getName())
                .description(department.getDescription())
                .headDoctorId(department.getHeadDoctorId())
                .imageUrl(department.getImageUrl())
                .active(department.getActive())
                .createdAt(department.getCreatedAt())
                .build();
    }

    public DoctorResponse toDoctorResponse(Doctor doctor) {
        String email = userRepository.findById(doctor.getUserId()).map(User::getEmail).orElse(null);
        String deptName = doctor.getDepartmentId() != null
                ? departmentRepository.findById(doctor.getDepartmentId()).map(Department::getName).orElse(null)
                : null;
        return DoctorResponse.builder()
                .id(doctor.getId())
                .userId(doctor.getUserId())
                .email(email)
                .departmentId(doctor.getDepartmentId())
                .departmentName(deptName)
                .firstName(doctor.getFirstName())
                .lastName(doctor.getLastName())
                .fullName(doctor.getFirstName() + " " + doctor.getLastName())
                .specialization(doctor.getSpecialization())
                .qualification(doctor.getQualification())
                .experienceYears(doctor.getExperienceYears())
                .gender(doctor.getGender())
                .phone(doctor.getPhone())
                .bio(doctor.getBio())
                .consultationFee(doctor.getConsultationFee())
                .available(doctor.getAvailable())
                .createdAt(doctor.getCreatedAt())
                .build();
    }

    public PatientResponse toPatientResponse(Patient patient) {
        String email = userRepository.findById(patient.getUserId()).map(User::getEmail).orElse(null);
        return PatientResponse.builder()
                .id(patient.getId())
                .userId(patient.getUserId())
                .email(email)
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .fullName(patient.getFirstName() + " " + patient.getLastName())
                .phone(patient.getPhone())
                .dateOfBirth(patient.getDateOfBirth())
                .gender(patient.getGender())
                .bloodGroup(patient.getBloodGroup())
                .address(patient.getAddress())
                .emergencyContact(patient.getEmergencyContact())
                .medicalHistory(patient.getMedicalHistory())
                .createdAt(patient.getCreatedAt())
                .build();
    }

    public StaffResponse toStaffResponse(Staff staff) {
        String email = userRepository.findById(staff.getUserId()).map(User::getEmail).orElse(null);
        String deptName = staff.getDepartmentId() != null
                ? departmentRepository.findById(staff.getDepartmentId()).map(Department::getName).orElse(null)
                : null;
        return StaffResponse.builder()
                .id(staff.getId())
                .userId(staff.getUserId())
                .email(email)
                .firstName(staff.getFirstName())
                .lastName(staff.getLastName())
                .fullName(staff.getFirstName() + " " + staff.getLastName())
                .role(staff.getRole())
                .departmentId(staff.getDepartmentId())
                .departmentName(deptName)
                .phone(staff.getPhone())
                .hireDate(staff.getHireDate())
                .active(staff.getActive())
                .build();
    }

    public DoctorAvailabilityResponse toDoctorAvailabilityResponse(DoctorAvailability availability) {
        return DoctorAvailabilityResponse.builder()
                .id(availability.getId())
                .doctorId(availability.getDoctorId())
                .dayOfWeek(availability.getDayOfWeek())
                .startTime(availability.getStartTime())
                .endTime(availability.getEndTime())
                .slotDurationMins(availability.getSlotDurationMins())
                .build();
    }

    public DoctorLeaveResponse toDoctorLeaveResponse(DoctorLeave leave) {
        return DoctorLeaveResponse.builder()
                .id(leave.getId())
                .doctorId(leave.getDoctorId())
                .startDate(leave.getStartDate())
                .endDate(leave.getEndDate())
                .reason(leave.getReason())
                .build();
    }

    public AppointmentResponse toAppointmentResponse(Appointment appointment) {
        String patientName = appointment.getPatient() != null
                ? appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName()
                : null;
        String doctorName = appointment.getDoctor() != null
                ? appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName()
                : null;
        return AppointmentResponse.builder()
                .id(appointment.getId())
                .patientId(appointment.getPatientId())
                .patientName(patientName)
                .doctorId(appointment.getDoctorId())
                .doctorName(doctorName)
                .appointmentDate(appointment.getAppointmentDate())
                .startTime(appointment.getStartTime())
                .endTime(appointment.getEndTime())
                .status(appointment.getStatus())
                .reason(appointment.getReason())
                .notes(appointment.getNotes())
                .isVideo(appointment.getIsVideo())
                .meetingUrl(appointment.getMeetingUrl())
                .createdAt(appointment.getCreatedAt())
                .build();
    }

    public PrescriptionResponse toPrescriptionResponse(Prescription prescription) {
        List<PrescriptionMedicineResponse> medicines = prescription.getMedicines() != null
                ? prescription.getMedicines().stream().map(this::toPrescriptionMedicineResponse).collect(Collectors.toList())
                : Collections.emptyList();
        String patientName = prescription.getPatient() != null
                ? prescription.getPatient().getFirstName() + " " + prescription.getPatient().getLastName()
                : null;
        String doctorName = prescription.getDoctor() != null
                ? prescription.getDoctor().getFirstName() + " " + prescription.getDoctor().getLastName()
                : null;
        return PrescriptionResponse.builder()
                .id(prescription.getId())
                .appointmentId(prescription.getAppointmentId())
                .patientId(prescription.getPatientId())
                .patientName(patientName)
                .doctorId(prescription.getDoctorId())
                .doctorName(doctorName)
                .diagnosis(prescription.getDiagnosis())
                .instructions(prescription.getInstructions())
                .pdfUrl(prescription.getPdfUrl())
                .medicines(medicines)
                .createdAt(prescription.getCreatedAt())
                .build();
    }

    public PrescriptionMedicineResponse toPrescriptionMedicineResponse(PrescriptionMedicine medicine) {
        return PrescriptionMedicineResponse.builder()
                .id(medicine.getId())
                .medicineName(medicine.getMedicineName())
                .dosage(medicine.getDosage())
                .frequency(medicine.getFrequency())
                .duration(medicine.getDuration())
                .build();
    }

    public LabTestResponse toLabTestResponse(LabTest labTest) {
        return LabTestResponse.builder()
                .id(labTest.getId())
                .name(labTest.getName())
                .code(labTest.getCode())
                .barcode(labTest.getBarcode())
                .description(labTest.getDescription())
                .price(labTest.getPrice())
                .departmentId(labTest.getDepartmentId())
                .active(labTest.getActive())
                .build();
    }

    public LabReportResponse toLabReportResponse(LabReport report) {
        String patientName = report.getPatient() != null
                ? report.getPatient().getFirstName() + " " + report.getPatient().getLastName()
                : null;
        String testName = report.getLabTest() != null ? report.getLabTest().getName() : null;
        return LabReportResponse.builder()
                .id(report.getId())
                .patientId(report.getPatientId())
                .patientName(patientName)
                .labTestId(report.getLabTestId())
                .labTestName(testName)
                .appointmentId(report.getAppointmentId())
                .resultSummary(report.getResultSummary())
                .reportFileUrl(report.getReportFileUrl())
                .completed(report.getCompleted())
                .completedAt(report.getCompletedAt())
                .createdAt(report.getCreatedAt())
                .build();
    }

    public SupplierResponse toSupplierResponse(Supplier supplier) {
        return SupplierResponse.builder()
                .id(supplier.getId())
                .name(supplier.getName())
                .contactPerson(supplier.getContactPerson())
                .phone(supplier.getPhone())
                .email(supplier.getEmail())
                .address(supplier.getAddress())
                .build();
    }

    public MedicineResponse toMedicineResponse(Medicine medicine) {
        String supplierName = medicine.getSupplier() != null ? medicine.getSupplier().getName() : null;
        boolean lowStock = medicine.getStockQuantity() <= medicine.getReorderLevel();
        return MedicineResponse.builder()
                .id(medicine.getId())
                .name(medicine.getName())
                .genericName(medicine.getGenericName())
                .manufacturer(medicine.getManufacturer())
                .category(medicine.getCategory())
                .unitPrice(medicine.getUnitPrice())
                .stockQuantity(medicine.getStockQuantity())
                .reorderLevel(medicine.getReorderLevel())
                .expiryDate(medicine.getExpiryDate())
                .barcode(medicine.getBarcode())
                .supplierId(medicine.getSupplierId())
                .supplierName(supplierName)
                .active(medicine.getActive())
                .lowStock(lowStock)
                .build();
    }

    public MedicinePurchaseResponse toMedicinePurchaseResponse(MedicinePurchase purchase) {
        String medicineName = purchase.getMedicine() != null ? purchase.getMedicine().getName() : null;
        String supplierName = purchase.getSupplier() != null ? purchase.getSupplier().getName() : null;
        return MedicinePurchaseResponse.builder()
                .id(purchase.getId())
                .medicineId(purchase.getMedicineId())
                .medicineName(medicineName)
                .supplierId(purchase.getSupplierId())
                .supplierName(supplierName)
                .quantity(purchase.getQuantity())
                .unitPrice(purchase.getUnitPrice())
                .purchaseDate(purchase.getPurchaseDate())
                .invoiceNumber(purchase.getInvoiceNumber())
                .build();
    }

    public MedicineSaleResponse toMedicineSaleResponse(MedicineSale sale) {
        String medicineName = sale.getMedicine() != null ? sale.getMedicine().getName() : null;
        String patientName = sale.getPatient() != null
                ? sale.getPatient().getFirstName() + " " + sale.getPatient().getLastName()
                : null;
        return MedicineSaleResponse.builder()
                .id(sale.getId())
                .medicineId(sale.getMedicineId())
                .medicineName(medicineName)
                .patientId(sale.getPatientId())
                .patientName(patientName)
                .quantity(sale.getQuantity())
                .unitPrice(sale.getUnitPrice())
                .totalAmount(sale.getTotalAmount())
                .saleDate(sale.getSaleDate())
                .soldBy(sale.getSoldBy())
                .build();
    }

    public BloodInventoryResponse toBloodInventoryResponse(BloodInventory inventory) {
        return BloodInventoryResponse.builder()
                .id(inventory.getId())
                .bloodGroup(inventory.getBloodGroup())
                .unitsAvailable(inventory.getUnitsAvailable())
                .updatedAt(inventory.getUpdatedAt())
                .build();
    }

    public BloodDonorResponse toBloodDonorResponse(BloodDonor donor) {
        return BloodDonorResponse.builder()
                .id(donor.getId())
                .name(donor.getName())
                .bloodGroup(donor.getBloodGroup())
                .phone(donor.getPhone())
                .email(donor.getEmail())
                .lastDonationDate(donor.getLastDonationDate())
                .address(donor.getAddress())
                .build();
    }

    public BloodRequestResponse toBloodRequestResponse(BloodRequest request) {
        String patientName = request.getPatient() != null
                ? request.getPatient().getFirstName() + " " + request.getPatient().getLastName()
                : null;
        return BloodRequestResponse.builder()
                .id(request.getId())
                .patientId(request.getPatientId())
                .patientName(patientName)
                .bloodGroup(request.getBloodGroup())
                .unitsRequired(request.getUnitsRequired())
                .urgency(request.getUrgency())
                .status(request.getStatus())
                .requestedAt(request.getRequestedAt())
                .fulfilledAt(request.getFulfilledAt())
                .build();
    }

    public RoomResponse toRoomResponse(Room room) {
        return RoomResponse.builder()
                .id(room.getId())
                .roomNumber(room.getRoomNumber())
                .roomType(room.getRoomType())
                .floor(room.getFloor())
                .departmentId(room.getDepartmentId())
                .dailyRate(room.getDailyRate())
                .active(room.getActive())
                .build();
    }

    public BedResponse toBedResponse(Bed bed) {
        String roomNumber = bed.getRoom() != null ? bed.getRoom().getRoomNumber() : null;
        return BedResponse.builder()
                .id(bed.getId())
                .roomId(bed.getRoomId())
                .roomNumber(roomNumber)
                .bedNumber(bed.getBedNumber())
                .status(bed.getStatus())
                .build();
    }

    public AdmissionResponse toAdmissionResponse(Admission admission) {
        String patientName = admission.getPatient() != null
                ? admission.getPatient().getFirstName() + " " + admission.getPatient().getLastName()
                : null;
        String bedNumber = admission.getBed() != null ? admission.getBed().getBedNumber() : null;
        String roomNumber = admission.getBed() != null && admission.getBed().getRoom() != null
                ? admission.getBed().getRoom().getRoomNumber()
                : null;
        return AdmissionResponse.builder()
                .id(admission.getId())
                .patientId(admission.getPatientId())
                .patientName(patientName)
                .bedId(admission.getBedId())
                .bedNumber(bedNumber)
                .roomNumber(roomNumber)
                .admittedBy(admission.getAdmittedBy())
                .admissionDate(admission.getAdmissionDate())
                .dischargeDate(admission.getDischargeDate())
                .status(admission.getStatus())
                .diagnosis(admission.getDiagnosis())
                .notes(admission.getNotes())
                .build();
    }

    public BillResponse toBillResponse(Bill bill) {
        String patientName = bill.getPatient() != null
                ? bill.getPatient().getFirstName() + " " + bill.getPatient().getLastName()
                : null;
        BigDecimal balance = bill.getTotalAmount().subtract(bill.getPaidAmount());
        return BillResponse.builder()
                .id(bill.getId())
                .patientId(bill.getPatientId())
                .patientName(patientName)
                .appointmentId(bill.getAppointmentId())
                .admissionId(bill.getAdmissionId())
                .totalAmount(bill.getTotalAmount())
                .paidAmount(bill.getPaidAmount())
                .balanceAmount(balance)
                .gstNumber(bill.getGstNumber())
                .cgst(bill.getCgst())
                .sgst(bill.getSgst())
                .status(bill.getStatus())
                .description(bill.getDescription())
                .createdAt(bill.getCreatedAt())
                .build();
    }

    public PaymentResponse toPaymentResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .billId(payment.getBillId())
                .amount(payment.getAmount())
                .method(payment.getMethod())
                .transactionRef(payment.getTransactionRef())
                .paidAt(payment.getPaidAt())
                .build();
    }

    public NotificationResponse toNotificationResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .userId(notification.getUserId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .type(notification.getType())
                .read(notification.getRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }

    public BlogPostResponse toBlogPostResponse(BlogPost post) {
        String authorName = post.getAuthor() != null ? post.getAuthor().getEmail() : null;
        return BlogPostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .slug(post.getSlug())
                .excerpt(post.getExcerpt())
                .content(post.getContent())
                .authorId(post.getAuthorId())
                .authorName(authorName)
                .imageUrl(post.getImageUrl())
                .published(post.getPublished())
                .publishedAt(post.getPublishedAt())
                .createdAt(post.getCreatedAt())
                .build();
    }

    public HealthPackageResponse toHealthPackageResponse(HealthPackage healthPackage) {
        return HealthPackageResponse.builder()
                .id(healthPackage.getId())
                .name(healthPackage.getName())
                .description(healthPackage.getDescription())
                .price(healthPackage.getPrice())
                .testsIncluded(healthPackage.getTestsIncluded())
                .active(healthPackage.getActive())
                .build();
    }

    public HospitalServiceResponse toHospitalServiceResponse(HospitalService service) {
        String deptName = service.getDepartment() != null ? service.getDepartment().getName() : null;
        return HospitalServiceResponse.builder()
                .id(service.getId())
                .name(service.getName())
                .description(service.getDescription())
                .icon(service.getIcon())
                .departmentId(service.getDepartmentId())
                .departmentName(deptName)
                .active(service.getActive())
                .build();
    }

    public AmbulanceResponse toAmbulanceResponse(Ambulance ambulance) {
        return AmbulanceResponse.builder()
                .id(ambulance.getId())
                .vehicleNumber(ambulance.getVehicleNumber())
                .driverName(ambulance.getDriverName())
                .driverPhone(ambulance.getDriverPhone())
                .available(ambulance.getAvailable())
                .location(ambulance.getLocation())
                .build();
    }

    public CareerResponse toCareerResponse(Career career) {
        String deptName = career.getDepartment() != null ? career.getDepartment().getName() : null;
        return CareerResponse.builder()
                .id(career.getId())
                .title(career.getTitle())
                .departmentId(career.getDepartmentId())
                .departmentName(deptName)
                .description(career.getDescription())
                .requirements(career.getRequirements())
                .location(career.getLocation())
                .active(career.getActive())
                .postedAt(career.getPostedAt())
                .build();
    }
}
