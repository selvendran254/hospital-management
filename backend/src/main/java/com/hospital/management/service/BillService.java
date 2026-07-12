package com.hospital.management.service;

import com.hospital.management.domain.entity.Bill;
import com.hospital.management.domain.entity.Patient;
import com.hospital.management.domain.entity.Payment;
import com.hospital.management.domain.entity.User;
import com.hospital.management.domain.enums.BillStatus;
import com.hospital.management.domain.enums.NotificationType;
import com.hospital.management.domain.enums.PaymentMethod;
import com.hospital.management.domain.enums.PaymentStatus;
import com.hospital.management.domain.enums.UserRole;
import com.hospital.management.dto.request.BillRequest;
import com.hospital.management.dto.request.PaymentRequest;
import com.hospital.management.dto.response.BillResponse;
import com.hospital.management.dto.response.PageResponse;
import com.hospital.management.dto.response.PaymentResponse;
import com.hospital.management.exception.BadRequestException;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.mapper.EntityMapper;
import com.hospital.management.repository.BillRepository;
import com.hospital.management.repository.PatientRepository;
import com.hospital.management.repository.PaymentRepository;
import com.hospital.management.util.PageUtils;
import com.hospital.management.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BillService {

    private final BillRepository billRepository;
    private final PaymentRepository paymentRepository;
    private final PatientRepository patientRepository;
    private final NotificationService notificationService;
    private final EntityMapper entityMapper;
    private final SecurityUtils securityUtils;
    private final ClinicContextService clinicContextService;

    public PageResponse<BillResponse> getAll(int page, int size) {
        Pageable pageable = PageUtils.of(page, size, "createdAt", "desc");
        return PageResponse.from(billRepository.findAll(pageable).map(entityMapper::toBillResponse));
    }

    public BillResponse getById(UUID id) {
        return entityMapper.toBillResponse(findBill(id));
    }

    @Transactional
    public BillResponse create(BillRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        Bill bill = Bill.builder()
                .clinicId(resolveClinicId())
                .patientId(request.getPatientId())
                .appointmentId(request.getAppointmentId())
                .admissionId(request.getAdmissionId())
                .totalAmount(request.getTotalAmount())
                .paidAmount(BigDecimal.ZERO)
                .status(BillStatus.PENDING)
                .description(request.getDescription())
                .gstNumber(request.getGstNumber())
                .cgst(request.getCgst() != null ? request.getCgst() : BigDecimal.ZERO)
                .sgst(request.getSgst() != null ? request.getSgst() : BigDecimal.ZERO)
                .build();
        bill = billRepository.save(bill);

        notificationService.notifyUser(patient.getUserId(), "Bill Generated",
                "A new bill of " + request.getTotalAmount() + " has been generated.",
                NotificationType.BILL_GENERATED);
        return entityMapper.toBillResponse(bill);
    }

    public PageResponse<BillResponse> getMyBills(int page, int size) {
        User user = securityUtils.getCurrentUser();
        if (user.getRole() != UserRole.PATIENT) {
            return getAll(page, size);
        }
        Patient patient = patientRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found"));
        Pageable pageable = PageUtils.of(page, size, "createdAt", "desc");
        return PageResponse.from(billRepository.findByPatientId(patient.getId(), pageable)
                .map(entityMapper::toBillResponse));
    }

    @Transactional
    public PaymentResponse recordPayment(PaymentRequest request) {
        Bill bill = billRepository.findById(request.getBillId())
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found"));
        if (bill.getStatus() == BillStatus.CANCELLED) {
            throw new BadRequestException("Cannot pay a cancelled bill");
        }

        Payment payment = Payment.builder()
                .clinicId(resolveClinicId())
                .billId(request.getBillId())
                .amount(request.getAmount())
                .method(request.getMethod() != null ? request.getMethod() : PaymentMethod.CASH)
                .transactionRef(request.getTransactionRef())
                .status(PaymentStatus.COMPLETED)
                .build();
        payment = paymentRepository.save(payment);

        BigDecimal newPaid = bill.getPaidAmount().add(request.getAmount());
        bill.setPaidAmount(newPaid);
        if (newPaid.compareTo(bill.getTotalAmount()) >= 0) {
            bill.setStatus(BillStatus.PAID);
        } else if (newPaid.compareTo(BigDecimal.ZERO) > 0) {
            bill.setStatus(BillStatus.PARTIAL);
        }
        billRepository.save(bill);
        return entityMapper.toPaymentResponse(payment);
    }

    private Bill findBill(UUID id) {
        return billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found: " + id));
    }

    private UUID resolveClinicId() {
        return clinicContextService != null ? clinicContextService.resolveClinicId() : null;
    }
}
