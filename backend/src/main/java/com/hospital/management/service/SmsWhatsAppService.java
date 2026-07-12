package com.hospital.management.service;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsWhatsAppService {

    private final TwilioSmsService twilioSmsService;

    public void sendSms(String phone, String message) {
        twilioSmsService.sendSms(phone, message);
        log.debug("SMS requested for {}", phone);
    }

    public void sendWhatsApp(String phone, String message) {
        twilioSmsService.sendWhatsApp(phone, message);
        log.debug("WhatsApp requested for {}", phone);
    }
}
