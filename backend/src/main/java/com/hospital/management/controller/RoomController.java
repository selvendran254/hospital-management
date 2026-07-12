package com.hospital.management.controller;

import com.hospital.management.dto.request.AdmissionRequest;
import com.hospital.management.dto.request.BedRequest;
import com.hospital.management.dto.request.RoomRequest;
import com.hospital.management.dto.response.AdmissionResponse;
import com.hospital.management.dto.response.BedResponse;
import com.hospital.management.dto.response.PageResponse;
import com.hospital.management.dto.response.RoomResponse;
import com.hospital.management.service.RoomService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Rooms & Beds")
public class RoomController {

    private final RoomService roomService;

    @GetMapping("/rooms")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR')")
    public ResponseEntity<PageResponse<RoomResponse>> getAllRooms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(roomService.getAllRooms(page, size));
    }

    @GetMapping("/rooms/{id}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable UUID id) {
        return ResponseEntity.ok(roomService.getRoomById(id));
    }

    @PostMapping("/rooms")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoomResponse> createRoom(@Valid @RequestBody RoomRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roomService.createRoom(request));
    }

    @PutMapping("/rooms/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable UUID id,
                                                   @Valid @RequestBody RoomRequest request) {
        return ResponseEntity.ok(roomService.updateRoom(id, request));
    }

    @DeleteMapping("/rooms/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRoom(@PathVariable UUID id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/beds")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<PageResponse<BedResponse>> getAllBeds(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(roomService.getAllBeds(page, size));
    }

    @PostMapping("/beds")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BedResponse> createBed(@Valid @RequestBody BedRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roomService.createBed(request));
    }

    @PutMapping("/beds/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BedResponse> updateBed(@PathVariable UUID id,
                                                 @Valid @RequestBody BedRequest request) {
        return ResponseEntity.ok(roomService.updateBed(id, request));
    }

    @DeleteMapping("/beds/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBed(@PathVariable UUID id) {
        roomService.deleteBed(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admissions")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR')")
    public ResponseEntity<PageResponse<AdmissionResponse>> getAdmissions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(roomService.getAdmissions(page, size));
    }

    @PostMapping("/admissions")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<AdmissionResponse> admit(@Valid @RequestBody AdmissionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roomService.admit(request));
    }

    @PatchMapping("/admissions/{id}/discharge")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR')")
    public ResponseEntity<AdmissionResponse> discharge(@PathVariable UUID id) {
        return ResponseEntity.ok(roomService.discharge(id));
    }
}
