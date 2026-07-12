package com.hospital.management.controller;

import com.hospital.management.dto.request.FeatureRequests;
import com.hospital.management.dto.response.MessageResponse;
import com.hospital.management.service.SmsWhatsAppService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/integrations")
@RequiredArgsConstructor
public class IntegrationController {

    private final SmsWhatsAppService smsWhatsAppService;

    @PostMapping("/twilio/send")
    public ResponseEntity<MessageResponse> send(@Valid @RequestBody FeatureRequests.SmsRequest request) {
        if (Boolean.TRUE.equals(request.getWhatsapp())) {
            smsWhatsAppService.sendWhatsApp(request.getTo(), request.getMessage());
        } else {
            smsWhatsAppService.sendSms(request.getTo(), request.getMessage());
        }
        return ResponseEntity.ok(MessageResponse.of("Message dispatched"));
    }
}
