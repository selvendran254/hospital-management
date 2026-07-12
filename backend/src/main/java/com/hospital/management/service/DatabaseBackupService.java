package com.hospital.management.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DatabaseBackupService {

    private final ObjectMapper objectMapper;

    @Value("${spring.datasource.url}")
    private String datasourceUrl;
    @Value("${spring.datasource.username}")
    private String datasourceUsername;
    @Value("${spring.datasource.password:}")
    private String datasourcePassword;
    @Value("${app.backup.directory:./backups}")
    private String backupDirectory;

    @Scheduled(cron = "${app.backup.cron:0 0 2 * * *}")
    public void scheduledBackup() {
        try {
            triggerBackup();
        } catch (Exception ex) {
            log.error("Scheduled backup failed", ex);
        }
    }

    public String triggerBackup() throws IOException, InterruptedException {
        Path dir = Path.of(backupDirectory);
        Files.createDirectories(dir);
        String filename = "hospital-backup-" + Instant.now().toString().replace(":", "-");
        Path sqlFile = dir.resolve(filename + ".sql");
        if (datasourceUrl.startsWith("jdbc:postgresql://")) {
            boolean exported = exportPostgres(sqlFile);
            if (exported) {
                return sqlFile.toAbsolutePath().toString();
            }
        }
        Path fallbackFile = dir.resolve(filename + ".json");
        Map<String, Object> fallback = new LinkedHashMap<>();
        fallback.put("generatedAt", Instant.now().toString());
        fallback.put("datasource", datasourceUrl);
        fallback.put("message", "pg_dump unavailable or failed; fallback metadata backup created");
        objectMapper.writeValue(fallbackFile.toFile(), fallback);
        return fallbackFile.toAbsolutePath().toString();
    }

    private boolean exportPostgres(Path sqlFile) throws IOException, InterruptedException {
        String hostPortDb = datasourceUrl.replace("jdbc:postgresql://", "");
        String hostPort = hostPortDb.substring(0, hostPortDb.indexOf('/'));
        String dbName = hostPortDb.substring(hostPortDb.indexOf('/') + 1).split("\\?")[0];
        String host = hostPort.contains(":") ? hostPort.split(":")[0] : hostPort;
        String port = hostPort.contains(":") ? hostPort.split(":")[1] : "5432";

        ProcessBuilder pb = new ProcessBuilder(
                "pg_dump",
                "-h", host,
                "-p", port,
                "-U", datasourceUsername,
                "-d", dbName,
                "-f", sqlFile.toAbsolutePath().toString()
        );
        pb.environment().put("PGPASSWORD", datasourcePassword == null ? "" : datasourcePassword);
        Process process = pb.start();
        int exit = process.waitFor();
        if (exit != 0) {
            log.warn("pg_dump exited with {}", exit);
            return false;
        }
        return Files.exists(sqlFile);
    }
}
