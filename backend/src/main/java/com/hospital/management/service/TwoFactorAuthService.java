package com.hospital.management.service;

import com.hospital.management.domain.entity.TwoFactorAuthSecret;
import com.hospital.management.domain.entity.User;
import com.hospital.management.dto.request.FeatureRequests;
import com.hospital.management.exception.BadRequestException;
import com.hospital.management.repository.TwoFactorAuthSecretRepository;
import com.hospital.management.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TwoFactorAuthService {

    private static final String BASE32_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";

    private final TwoFactorAuthSecretRepository secretRepository;
    private final SecurityUtils securityUtils;

    @Value("${spring.application.name:hospital-management}")
    private String appName;

    @Transactional
    public Map<String, String> setupCurrentUser() {
        User user = securityUtils.getCurrentUser();
        String secret = generateBase32Secret();
        TwoFactorAuthSecret entity = secretRepository.findByUserId(user.getId())
                .orElse(TwoFactorAuthSecret.builder().userId(user.getId()).build());
        entity.setSecret(secret);
        entity.setEnabled(false);
        secretRepository.save(entity);
        String otpAuth = "otpauth://totp/" + appName + ":" + user.getEmail()
                + "?secret=" + secret + "&issuer=" + appName + "&algorithm=SHA1&digits=6&period=30";
        String qrUrl = "https://api.qrserver.com/v1/create-qr-code/?size=240x240&data="
                + java.net.URLEncoder.encode(otpAuth, StandardCharsets.UTF_8);
        Map<String, String> response = new LinkedHashMap<>();
        response.put("secret", secret);
        response.put("otpauthUrl", otpAuth);
        response.put("qrCodeUrl", qrUrl);
        return response;
    }

    @Transactional
    public boolean verifyCurrentUser(FeatureRequests.TwoFactorVerifyRequest request) {
        User user = securityUtils.getCurrentUser();
        TwoFactorAuthSecret secret = secretRepository.findByUserId(user.getId())
                .orElseThrow(() -> new BadRequestException("2FA is not initialized for user"));
        boolean valid = verifyCode(secret.getSecret(), request.getOtp(), Instant.now().getEpochSecond());
        if (valid) {
            secret.setEnabled(true);
            secretRepository.save(secret);
        }
        return valid;
    }

    private String generateBase32Secret() {
        byte[] random = new byte[20];
        new SecureRandom().nextBytes(random);
        return base32Encode(random);
    }

    private String base32Encode(byte[] data) {
        StringBuilder out = new StringBuilder();
        int buffer = 0;
        int bitsLeft = 0;
        for (byte b : data) {
            buffer <<= 8;
            buffer |= b & 0xFF;
            bitsLeft += 8;
            while (bitsLeft >= 5) {
                int idx = (buffer >> (bitsLeft - 5)) & 0x1F;
                bitsLeft -= 5;
                out.append(BASE32_ALPHABET.charAt(idx));
            }
        }
        if (bitsLeft > 0) {
            int idx = (buffer << (5 - bitsLeft)) & 0x1F;
            out.append(BASE32_ALPHABET.charAt(idx));
        }
        return out.toString();
    }

    private boolean verifyCode(String secret, String code, long epochSeconds) {
        if (code == null || !code.matches("\\d{6}")) {
            return false;
        }
        long currentStep = epochSeconds / 30;
        for (int delta = -1; delta <= 1; delta++) {
            String generated = generateTotp(secret, currentStep + delta);
            if (code.equals(generated)) {
                return true;
            }
        }
        return false;
    }

    private String generateTotp(String secret, long timeStep) {
        try {
            byte[] key = base32Decode(secret);
            byte[] time = ByteBuffer.allocate(8).putLong(timeStep).array();
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec(key, "HmacSHA1"));
            byte[] hash = mac.doFinal(time);
            int offset = hash[hash.length - 1] & 0x0F;
            int binary = ((hash[offset] & 0x7F) << 24)
                    | ((hash[offset + 1] & 0xFF) << 16)
                    | ((hash[offset + 2] & 0xFF) << 8)
                    | (hash[offset + 3] & 0xFF);
            int otp = binary % 1_000_000;
            return String.format("%06d", otp);
        } catch (Exception ex) {
            throw new BadRequestException("Unable to generate TOTP");
        }
    }

    private byte[] base32Decode(String base32) {
        int buffer = 0;
        int bitsLeft = 0;
        byte[] out = new byte[base32.length() * 5 / 8];
        int outPos = 0;
        for (char c : base32.replace("=", "").toUpperCase().toCharArray()) {
            int val = BASE32_ALPHABET.indexOf(c);
            if (val < 0) {
                continue;
            }
            buffer = (buffer << 5) | val;
            bitsLeft += 5;
            if (bitsLeft >= 8) {
                out[outPos++] = (byte) ((buffer >> (bitsLeft - 8)) & 0xFF);
                bitsLeft -= 8;
            }
        }
        byte[] result = new byte[outPos];
        System.arraycopy(out, 0, result, 0, outPos);
        return result;
    }
}
