package com.hospital.management.dto.response;

import com.hospital.management.domain.enums.PaymentMethod;
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
public class PaymentResponse {

    private UUID id;
    private UUID billId;
    private BigDecimal amount;
    private PaymentMethod method;
    private String transactionRef;
    private Instant paidAt;
}
