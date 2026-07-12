package com.hospital.management.service;

import com.hospital.management.domain.entity.MediaAsset;
import com.hospital.management.domain.enums.MediaCategory;
import com.hospital.management.dto.response.FeatureResponses;
import com.hospital.management.repository.MediaAssetRepository;
import com.hospital.management.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MediaUploadService {

    private final StorageService storageService;
    private final MediaAssetRepository mediaAssetRepository;
    private final ClinicContextService clinicContextService;

    public FeatureResponses.MediaUploadResponse upload(MediaCategory category, MultipartFile file, UUID uploadedBy) {
        UUID clinicId = clinicContextService.resolveClinicId();
        String folder = "media/" + category.name().toLowerCase();
        String path = storageService.store(file, folder);
        String publicUrl = storageService.getPublicUrl(path);
        MediaAsset asset = mediaAssetRepository.save(MediaAsset.builder()
                .clinicId(clinicId)
                .category(category)
                .fileType(file.getContentType() == null ? "application/octet-stream" : file.getContentType())
                .filePath(path)
                .publicUrl(publicUrl)
                .uploadedBy(uploadedBy)
                .build());
        return FeatureResponses.MediaUploadResponse.builder()
                .mediaId(asset.getId())
                .filePath(asset.getFilePath())
                .publicUrl(asset.getPublicUrl())
                .category(asset.getCategory().name())
                .build();
    }

    public List<MediaAsset> list(MediaCategory category) {
        return mediaAssetRepository.findByClinicIdAndCategory(clinicContextService.resolveClinicId(), category);
    }
}
