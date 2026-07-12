package com.hospital.management.dto.request;

import com.hospital.management.domain.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PaymentRequest {

    @NotNull
    private UUID billId;

    @NotNull
    private BigDecimal amount;

    private PaymentMethod method;
    private String transactionRef;
}
