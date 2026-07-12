package com.hospital.management.dto.response;

import com.hospital.management.domain.enums.BillStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillResponse {

    private UUID id;
    private UUID patientId;
    private String patientName;
    private UUID appointmentId;
    private UUID admissionId;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private BigDecimal balanceAmount;
    private String gstNumber;
    private BigDecimal cgst;
    private BigDecimal sgst;
    private BillStatus status;
    private String description;
    private Instant createdAt;
}
