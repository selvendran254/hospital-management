package com.hospital.management.storage;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    String store(MultipartFile file, String folder);

    byte[] load(String filePath);

    void delete(String filePath);

    String getPublicUrl(String filePath);
}
