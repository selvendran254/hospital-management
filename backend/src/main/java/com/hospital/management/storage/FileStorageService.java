package com.hospital.management.storage;

import com.hospital.management.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@ConditionalOnProperty(name = "app.storage.provider", havingValue = "local", matchIfMissing = true)
public class FileStorageService implements StorageService {

    private final Path basePath;
    private final String baseUrl;

    public FileStorageService(
            @Value("${app.storage.local.base-path}") String basePath,
            @Value("${app.storage.local.base-url}") String baseUrl) {
        this.basePath = Paths.get(basePath).toAbsolutePath().normalize();
        this.baseUrl = baseUrl;
        try {
            Files.createDirectories(this.basePath);
        } catch (IOException e) {
            throw new BadRequestException("Could not create upload directory");
        }
    }

    @Override
    public String store(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File is empty");
        }
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        }
        String filename = UUID.randomUUID() + extension;
        Path targetDir = basePath.resolve(folder);
        try {
            Files.createDirectories(targetDir);
            Path targetPath = targetDir.resolve(filename);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            return folder + "/" + filename;
        } catch (IOException e) {
            throw new BadRequestException("Failed to store file: " + e.getMessage());
        }
    }

    @Override
    public byte[] load(String filePath) {
        try {
            Path path = basePath.resolve(filePath).normalize();
            if (!path.startsWith(basePath)) {
                throw new BadRequestException("Invalid file path");
            }
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new BadRequestException("Failed to read file: " + e.getMessage());
        }
    }

    @Override
    public void delete(String filePath) {
        try {
            Path path = basePath.resolve(filePath).normalize();
            if (path.startsWith(basePath)) {
                Files.deleteIfExists(path);
            }
        } catch (IOException e) {
            throw new BadRequestException("Failed to delete file: " + e.getMessage());
        }
    }

    @Override
    public String getPublicUrl(String filePath) {
        return baseUrl + "/" + filePath;
    }
}
