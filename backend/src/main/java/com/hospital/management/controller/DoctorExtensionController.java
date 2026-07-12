package com.hospital.management.controller;

import com.hospital.management.domain.entity.DoctorNote;
import com.hospital.management.domain.entity.PrescriptionTemplate;
import com.hospital.management.domain.entity.Referral;
import com.hospital.management.domain.entity.TelemedicineSession;
import com.hospital.management.dto.request.ExtensionRequests;
import com.hospital.management.dto.response.ExtensionResponses;
import com.hospital.management.service.ExtensionCrudService;
import com.hospital.management.service.SymptomCheckerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors/ext")
@RequiredArgsConstructor
public class DoctorExtensionController {

    private final ExtensionCrudService extensionCrudService;
    private final SymptomCheckerService symptomCheckerService;

    @PostMapping("/prescription-templates")
    public ResponseEntity<PrescriptionTemplate> createTemplate(@Valid @RequestBody ExtensionRequests.PrescriptionTemplateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(extensionCrudService.createPrescriptionTemplate(request));
    }

    @PostMapping("/telemedicine-sessions")
    public ResponseEntity<TelemedicineSession> createSession(@Valid @RequestBody ExtensionRequests.TelemedicineSessionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(extensionCrudService.createTelemedicineSession(request));
    }

    @PostMapping("/notes")
    public ResponseEntity<DoctorNote> createNote(@Valid @RequestBody ExtensionRequests.DoctorNoteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(extensionCrudService.createDoctorNote(request));
    }

    @PostMapping("/referrals")
    public ResponseEntity<Referral> createReferral(@Valid @RequestBody ExtensionRequests.ReferralRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(extensionCrudService.createReferral(request));
    }

    @PostMapping("/symptom-checker")
    public ResponseEntity<ExtensionResponses.SymptomSuggestionResponse> check(@RequestBody List<String> symptoms) {
        return ResponseEntity.ok(symptomCheckerService.check(symptoms));
    }
}
