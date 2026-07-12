package com.hospital.management.service;

import com.hospital.management.dto.request.ExtensionRequests;
import com.hospital.management.dto.response.ExtensionResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentGatewayService {

    private final RazorpayPaymentService razorpayPaymentService;

    public ExtensionResponses.PaymentGatewayResponse createOrder(ExtensionRequests.PaymentRequest request) {
        return razorpayPaymentService.createOrder(request);
    }
}
