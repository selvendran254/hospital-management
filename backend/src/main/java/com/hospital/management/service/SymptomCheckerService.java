package com.hospital.management.service;

import com.hospital.management.dto.response.ExtensionResponses;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SymptomCheckerService {

    public ExtensionResponses.SymptomSuggestionResponse check(List<String> symptoms) {
        List<String> suggestions = new ArrayList<>();
        String triage = "LOW";
        String joined = String.join(" ", symptoms).toLowerCase();
        if (joined.contains("chest pain") || joined.contains("breath")) {
            suggestions.add("Consult cardiology immediately");
            triage = "HIGH";
        }
        if (joined.contains("fever")) {
            suggestions.add("Hydration and physician review within 24 hours");
        }
        if (suggestions.isEmpty()) {
            suggestions.add("Schedule a general physician consultation");
        }
        return ExtensionResponses.SymptomSuggestionResponse.builder()
                .suggestions(suggestions)
                .triageLevel(triage)
                .build();
    }
}
