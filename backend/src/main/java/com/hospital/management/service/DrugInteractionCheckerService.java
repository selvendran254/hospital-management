package com.hospital.management.service;

import com.hospital.management.dto.response.ExtensionResponses;
import com.hospital.management.repository.DrugInteractionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DrugInteractionCheckerService {

    private final DrugInteractionRepository drugInteractionRepository;

    public ExtensionResponses.DrugInteractionCheckResponse check(String drugA, String drugB) {
        return drugInteractionRepository.findByDrugAIgnoreCaseOrDrugBIgnoreCase(drugA, drugB).stream()
                .filter(item ->
                        (item.getDrugA().equalsIgnoreCase(drugA) && item.getDrugB().equalsIgnoreCase(drugB))
                                || (item.getDrugA().equalsIgnoreCase(drugB) && item.getDrugB().equalsIgnoreCase(drugA)))
                .findFirst()
                .map(item -> ExtensionResponses.DrugInteractionCheckResponse.builder()
                        .severity(item.getSeverity())
                        .advisory(item.getAdvisory())
                        .build())
                .orElse(ExtensionResponses.DrugInteractionCheckResponse.builder()
                        .severity("NONE")
                        .advisory("No interaction known in local rule set")
                        .build());
    }
}
