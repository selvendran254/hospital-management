package com.hospital.management.storage;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@ConditionalOnProperty(name = "app.storage.provider", havingValue = "s3")
public class S3StorageService implements StorageService {

    @Override
    public String store(MultipartFile file, String folder) {
        String key = folder + "/" + UUID.randomUUID();
        return "s3://" + key;
    }

    @Override
    public byte[] load(String filePath) {
        return new byte[0];
    }

    @Override
    public void delete(String filePath) {
    }

    @Override
    public String getPublicUrl(String filePath) {
        return "https://dummy-s3.local/" + filePath;
    }
}
