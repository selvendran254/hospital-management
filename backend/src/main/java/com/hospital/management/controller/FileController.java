package com.hospital.management.controller;

import com.hospital.management.dto.response.FileUploadResponse;
import com.hospital.management.dto.response.MessageResponse;
import com.hospital.management.service.EmailService;
import com.hospital.management.storage.StorageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "Files & Contact")
public class FileController {

    private final StorageService storageService;
    private final EmailService emailService;

    @PostMapping("/files/upload")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<FileUploadResponse> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(defaultValue = "general") String folder) {
        String path = storageService.store(file, folder);
        return ResponseEntity.ok(FileUploadResponse.builder()
                .filePath(path)
                .url(storageService.getPublicUrl(path))
                .build());
    }

    @PostMapping("/contact")
    public ResponseEntity<MessageResponse> contact(@RequestBody Map<String, String> request) {
        String email = request.getOrDefault("email", "unknown");
        String subject = request.getOrDefault("subject", "Contact Form");
        String message = request.getOrDefault("message", "");
        emailService.sendEmail("admin@hospital.com", "Contact: " + subject,
                "From: " + request.get("name") + " <" + email + ">\n" + message);
        return ResponseEntity.ok(MessageResponse.of("Message sent successfully"));
    }
}
