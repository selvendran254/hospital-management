package com.hospital.management.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Service
public class TwilioSmsService {

    @Value("${app.integrations.twilio.account-sid:}")
    private String accountSid;
    @Value("${app.integrations.twilio.auth-token:}")
    private String authToken;
    @Value("${app.integrations.twilio.from-sms:}")
    private String fromSms;
    @Value("${app.integrations.twilio.from-whatsapp:}")
    private String fromWhatsapp;

    public void sendSms(String to, String message) {
        send(false, to, message);
    }

    public void sendWhatsApp(String to, String message) {
        send(true, to, message);
    }

    private void send(boolean whatsapp, String to, String message) {
        if (!configured()) {
            log.info("Twilio fallback mode [{}] to={} message={}", whatsapp ? "whatsapp" : "sms", to, message);
            return;
        }
        try {
            String endpoint = "https://api.twilio.com/2010-04-01/Accounts/" + accountSid + "/Messages.json";
            String from = whatsapp ? "whatsapp:" + fromWhatsapp : fromSms;
            String target = whatsapp ? "whatsapp:" + to : to;
            String form = "To=" + encode(target) + "&From=" + encode(from) + "&Body=" + encode(message);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .header("Authorization", "Basic " + basicAuth())
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(form))
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                log.error("Twilio send failed status={} body={}", response.statusCode(), response.body());
            }
        } catch (Exception ex) {
            log.error("Twilio send failed", ex);
        }
    }

    private String basicAuth() {
        String pair = accountSid + ":" + authToken;
        return Base64.getEncoder().encodeToString(pair.getBytes(StandardCharsets.UTF_8));
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private boolean configured() {
        return has(accountSid) && has(authToken) && has(fromSms);
    }

    private boolean has(String value) {
        return value != null && !value.isBlank();
    }
}
