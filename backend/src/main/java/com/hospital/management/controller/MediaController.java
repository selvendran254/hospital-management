package com.hospital.management.controller;

import com.hospital.management.domain.entity.MediaAsset;
import com.hospital.management.domain.enums.MediaCategory;
import com.hospital.management.dto.response.FeatureResponses;
import com.hospital.management.service.MediaUploadService;
import com.hospital.management.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaUploadService mediaUploadService;
    private final SecurityUtils securityUtils;

    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPTIONIST')")
    public ResponseEntity<FeatureResponses.MediaUploadResponse> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam MediaCategory category) {
        return ResponseEntity.ok(mediaUploadService.upload(category, file, securityUtils.getCurrentUser().getId()));
    }

    @GetMapping("/{category}")
    public ResponseEntity<List<MediaAsset>> list(@PathVariable MediaCategory category) {
        return ResponseEntity.ok(mediaUploadService.list(category));
    }
}
