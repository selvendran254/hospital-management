package com.hospital.management.service;

import com.hospital.management.dto.response.ExtensionResponses;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class ExportService {

    public ExtensionResponses.ExportFileResponse exportCsv(List<String> headers, List<List<String>> rows) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.join(",", headers)).append("\n");
        for (List<String> row : rows) {
            sb.append(String.join(",", row)).append("\n");
        }
        return ExtensionResponses.ExportFileResponse.builder()
                .format("csv")
                .content(sb.toString().getBytes(StandardCharsets.UTF_8))
                .build();
    }

    public ExtensionResponses.ExportFileResponse exportExcel(List<String> headers, List<List<String>> rows) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.join("\t", headers)).append("\n");
        for (List<String> row : rows) {
            sb.append(String.join("\t", row)).append("\n");
        }
        return ExtensionResponses.ExportFileResponse.builder()
                .format("xlsx")
                .content(sb.toString().getBytes(StandardCharsets.UTF_8))
                .build();
    }
}
