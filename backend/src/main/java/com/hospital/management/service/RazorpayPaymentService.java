package com.hospital.management.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.management.dto.request.ExtensionRequests;
import com.hospital.management.dto.request.FeatureRequests;
import com.hospital.management.dto.response.ExtensionResponses;
import com.hospital.management.dto.response.FeatureResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RazorpayPaymentService {

    private static final String API_BASE = "https://api.razorpay.com/v1/orders";
    private final ObjectMapper objectMapper;

    @Value("${app.integrations.razorpay.key-id:}")
    private String keyId;

    @Value("${app.integrations.razorpay.key-secret:}")
    private String keySecret;

    public ExtensionResponses.PaymentGatewayResponse createOrder(ExtensionRequests.PaymentRequest request) {
        if (!isConfigured()) {
            return ExtensionResponses.PaymentGatewayResponse.builder()
                    .gatewayOrderId("razorpay_dummy_" + UUID.randomUUID())
                    .status("CREATED")
                    .message("Dummy Razorpay order generated because credentials are missing")
                    .build();
        }
        try {
            HttpClient client = HttpClient.newHttpClient();
            Map<String, Object> payload = new LinkedHashMap<>();
            payload.put("amount", request.getAmount().multiply(java.math.BigDecimal.valueOf(100)).intValue());
            payload.put("currency", request.getCurrency());
            payload.put("receipt", request.getBillId().toString());
            payload.put("payment_capture", 1);
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE))
                    .header("Authorization", "Basic " + encodedAuth())
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(payload)))
                    .build();
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                log.error("Razorpay order create failed status={} body={}", response.statusCode(), response.body());
                return ExtensionResponses.PaymentGatewayResponse.builder()
                        .gatewayOrderId("razorpay_dummy_" + UUID.randomUUID())
                        .status("FAILED")
                        .message("Razorpay API call failed, using fallback")
                        .build();
            }
            JsonNode body = objectMapper.readTree(response.body());
            return ExtensionResponses.PaymentGatewayResponse.builder()
                    .gatewayOrderId(body.path("id").asText())
                    .status("CREATED")
                    .message("Razorpay order created successfully")
                    .build();
        } catch (Exception ex) {
            log.error("Error while creating Razorpay order", ex);
            return ExtensionResponses.PaymentGatewayResponse.builder()
                    .gatewayOrderId("razorpay_dummy_" + UUID.randomUUID())
                    .status("FAILED")
                    .message("Razorpay unavailable, fallback order returned")
                    .build();
        }
    }

    public FeatureResponses.PaymentVerificationResponse verifySignature(FeatureRequests.VerifyPaymentRequest request) {
        if (!isConfigured()) {
            return FeatureResponses.PaymentVerificationResponse.builder()
                    .valid(Boolean.TRUE)
                    .message("Dummy verification accepted because credentials are missing")
                    .build();
        }
        try {
            String payload = request.getOrderId() + "|" + request.getPaymentId();
            String expected = hmacSha256(payload, keySecret);
            boolean valid = expected.equals(request.getSignature());
            return FeatureResponses.PaymentVerificationResponse.builder()
                    .valid(valid)
                    .message(valid ? "Payment signature verified" : "Payment signature invalid")
                    .build();
        } catch (Exception ex) {
            log.error("Razorpay signature verification failed", ex);
            return FeatureResponses.PaymentVerificationResponse.builder()
                    .valid(Boolean.FALSE)
                    .message("Signature verification failed")
                    .build();
        }
    }

    private boolean isConfigured() {
        return keyId != null && !keyId.isBlank() && keySecret != null && !keySecret.isBlank();
    }

    private String encodedAuth() {
        return Base64.getEncoder().encodeToString((keyId + ":" + keySecret).getBytes(StandardCharsets.UTF_8));
    }

    private String hmacSha256(String payload, String secret) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] digest = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
