package com.hospital.management.controller;

import com.hospital.management.domain.entity.FamilyMember;
import com.hospital.management.domain.entity.QueueToken;
import com.hospital.management.domain.entity.RecurringAppointment;
import com.hospital.management.dto.request.ExtensionRequests;
import com.hospital.management.dto.response.ExtensionResponses;
import com.hospital.management.service.ExtensionCrudService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/appointments/ext")
@RequiredArgsConstructor
public class AppointmentExtensionController {

    private final ExtensionCrudService extensionCrudService;

    @PostMapping("/queue-token")
    public ResponseEntity<QueueToken> issueToken(@Valid @RequestBody ExtensionRequests.QueueTokenRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(extensionCrudService.issueQueueToken(request));
    }

    @GetMapping("/queue-token")
    public ResponseEntity<List<QueueToken>> getQueue(@RequestParam UUID doctorId, @RequestParam LocalDate date) {
        return ResponseEntity.ok(extensionCrudService.getQueue(doctorId, date));
    }

    @PostMapping("/family-member")
    public ResponseEntity<FamilyMember> addFamilyMember(@Valid @RequestBody ExtensionRequests.FamilyMemberRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(extensionCrudService.createFamilyMember(request));
    }

    @GetMapping("/family-member/{patientId}")
    public ResponseEntity<List<FamilyMember>> getFamily(@PathVariable UUID patientId) {
        return ResponseEntity.ok(extensionCrudService.getFamilyMembers(patientId));
    }

    @PostMapping("/recurring")
    public ResponseEntity<RecurringAppointment> createRecurring(@Valid @RequestBody ExtensionRequests.RecurringAppointmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(extensionCrudService.createRecurringAppointment(request));
    }

    @GetMapping("/recurring/{doctorId}")
    public ResponseEntity<List<RecurringAppointment>> getRecurring(@PathVariable UUID doctorId) {
        return ResponseEntity.ok(extensionCrudService.getRecurringAppointments(doctorId));
    }

    @GetMapping("/doctor-availability")
    public ResponseEntity<ExtensionResponses.DoctorAvailabilityCalendarResponse> getAvailability(
            @RequestParam UUID doctorId,
            @RequestParam LocalDate date) {
        return ResponseEntity.ok(extensionCrudService.getAvailability(doctorId, date));
    }
}
