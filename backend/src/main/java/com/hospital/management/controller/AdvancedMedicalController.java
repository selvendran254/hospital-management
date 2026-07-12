package com.hospital.management.controller;

import com.hospital.management.domain.entity.MedicalImage;
import com.hospital.management.domain.entity.OperationTheatre;
import com.hospital.management.domain.entity.OrganTransplantRegistry;
import com.hospital.management.domain.entity.SurgerySchedule;
import com.hospital.management.dto.request.FeatureRequests;
import com.hospital.management.dto.response.FeatureResponses;
import com.hospital.management.service.AdvancedMedicalService;
import com.hospital.management.service.AiReportSummaryService;
import com.hospital.management.service.VideoCallService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/medical/advanced")
@RequiredArgsConstructor
public class AdvancedMedicalController {

    private final AdvancedMedicalService advancedMedicalService;
    private final AiReportSummaryService aiReportSummaryService;
    private final VideoCallService videoCallService;

    @PostMapping("/images")
    public ResponseEntity<MedicalImage> addImage(@Valid @RequestBody FeatureRequests.MedicalImageRequest request) {
        return ResponseEntity.ok(advancedMedicalService.addMedicalImage(request));
    }

    @GetMapping("/images/patient/{patientId}")
    public ResponseEntity<List<MedicalImage>> listImages(@PathVariable UUID patientId) {
        return ResponseEntity.ok(advancedMedicalService.imagesForPatient(patientId));
    }

    @GetMapping("/images/{imageId}/viewer")
    public ResponseEntity<MedicalImage> viewImage(@PathVariable UUID imageId) {
        return ResponseEntity.ok(advancedMedicalService.viewMedicalImage(imageId));
    }

    @PostMapping("/ai/lab-summary")
    public ResponseEntity<FeatureResponses.AiSummaryResponse> summarizeLab(@Valid @RequestBody FeatureRequests.LabResultRequest request) {
        return ResponseEntity.ok(aiReportSummaryService.summarizeLabReport(request));
    }

    @PostMapping("/operation-theatres")
    public ResponseEntity<OperationTheatre> addTheatre(@Valid @RequestBody FeatureRequests.OperationTheatreRequest request) {
        return ResponseEntity.ok(advancedMedicalService.createTheatre(request));
    }

    @PostMapping("/surgeries")
    public ResponseEntity<SurgerySchedule> schedule(@Valid @RequestBody FeatureRequests.SurgeryScheduleRequest request) {
        return ResponseEntity.ok(advancedMedicalService.scheduleSurgery(request));
    }

    @GetMapping("/surgeries")
    public ResponseEntity<List<SurgerySchedule>> listSurgeries(@RequestParam LocalDate date) {
        return ResponseEntity.ok(advancedMedicalService.surgeriesByDate(date));
    }

    @PostMapping("/transplants")
    public ResponseEntity<OrganTransplantRegistry> registerTransplant(@Valid @RequestBody FeatureRequests.TransplantRegistryRequest request) {
        return ResponseEntity.ok(advancedMedicalService.registerTransplant(request));
    }

    @GetMapping("/transplants")
    public ResponseEntity<List<OrganTransplantRegistry>> listTransplants() {
        return ResponseEntity.ok(advancedMedicalService.transplants());
    }

    @PostMapping("/telemedicine/room")
    public ResponseEntity<FeatureResponses.VideoRoomResponse> generateRoom(@Valid @RequestBody FeatureRequests.VideoRoomRequest request) {
        return ResponseEntity.ok(videoCallService.generateRoom(request));
    }
}
