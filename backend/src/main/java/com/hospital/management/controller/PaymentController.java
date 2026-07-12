package com.hospital.management.controller;

import com.hospital.management.dto.request.ExtensionRequests;
import com.hospital.management.dto.request.FeatureRequests;
import com.hospital.management.dto.response.ExtensionResponses;
import com.hospital.management.dto.response.FeatureResponses;
import com.hospital.management.service.PaymentGatewayService;
import com.hospital.management.service.RazorpayPaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments/gateway")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentGatewayService paymentGatewayService;
    private final RazorpayPaymentService razorpayPaymentService;

    @PostMapping("/create-order")
    public ResponseEntity<ExtensionResponses.PaymentGatewayResponse> createOrder(
            @Valid @RequestBody ExtensionRequests.PaymentRequest request) {
        return ResponseEntity.ok(paymentGatewayService.createOrder(request));
    }

    @PostMapping("/verify")
    public ResponseEntity<FeatureResponses.PaymentVerificationResponse> verifyPayment(
            @Valid @RequestBody FeatureRequests.VerifyPaymentRequest request) {
        return ResponseEntity.ok(razorpayPaymentService.verifySignature(request));
    }
}
