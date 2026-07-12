package com.hospital.management.controller;

import com.hospital.management.domain.entity.CanteenMenuItem;
import com.hospital.management.domain.entity.FoodOrder;
import com.hospital.management.domain.entity.ParkingSlot;
import com.hospital.management.domain.enums.FoodOrderStatus;
import com.hospital.management.dto.request.*;
import com.hospital.management.dto.response.AmbulanceRouteResponse;
import com.hospital.management.dto.response.MapLocationResponse;
import com.hospital.management.service.OperationsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Operations")
public class OperationsController {

    private final OperationsService operationsService;

    @GetMapping("/public/maps/hospital")
    public ResponseEntity<MapLocationResponse> getHospitalLocation() {
        return ResponseEntity.ok(operationsService.getHospitalLocation());
    }

    @GetMapping("/public/maps/ambulances")
    public ResponseEntity<List<AmbulanceRouteResponse>> getAmbulanceRoutes() {
        return ResponseEntity.ok(operationsService.getAmbulanceRoutes());
    }

    @GetMapping("/public/canteen/menu")
    public ResponseEntity<List<CanteenMenuItem>> getMenu() {
        return ResponseEntity.ok(operationsService.getMenu());
    }

    @PostMapping("/canteen/orders")
    @PreAuthorize("hasAnyRole('PATIENT', 'RECEPTIONIST', 'ADMIN')")
    public ResponseEntity<FoodOrder> placeOrder(@Valid @RequestBody FoodOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(operationsService.placeFoodOrder(request));
    }

    @GetMapping("/canteen/orders")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<List<FoodOrder>> getOrders() {
        return ResponseEntity.ok(operationsService.getFoodOrders());
    }

    @GetMapping("/canteen/orders/patient/{patientId}")
    @PreAuthorize("hasAnyRole('PATIENT', 'ADMIN')")
    public ResponseEntity<List<FoodOrder>> getPatientOrders(@PathVariable UUID patientId) {
        return ResponseEntity.ok(operationsService.getPatientFoodOrders(patientId));
    }

    @PatchMapping("/canteen/orders/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<FoodOrder> updateOrderStatus(@PathVariable UUID id, @RequestBody Map<String, String> body) {
        FoodOrderStatus status = FoodOrderStatus.valueOf(body.get("status"));
        return ResponseEntity.ok(operationsService.updateFoodOrderStatus(id, status));
    }

    @GetMapping("/canteen/menu")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CanteenMenuItem>> getAllMenu() {
        return ResponseEntity.ok(operationsService.getAllMenuItems());
    }

    @PostMapping("/canteen/menu")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CanteenMenuItem> createMenuItem(@Valid @RequestBody CanteenMenuItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(operationsService.createMenuItem(request));
    }

    @PutMapping("/canteen/menu/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CanteenMenuItem> updateMenuItem(@PathVariable UUID id, @Valid @RequestBody CanteenMenuItemRequest request) {
        return ResponseEntity.ok(operationsService.updateMenuItem(id, request));
    }

    @DeleteMapping("/canteen/menu/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable UUID id) {
        operationsService.deleteMenuItem(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/public/parking")
    public ResponseEntity<List<ParkingSlot>> getAvailableParking() {
        return ResponseEntity.ok(operationsService.getAvailableParking());
    }

    @GetMapping("/parking")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<List<ParkingSlot>> getAllParking() {
        return ResponseEntity.ok(operationsService.getParkingSlots());
    }

    @GetMapping("/parking/stats")
    public ResponseEntity<Map<String, Long>> getParkingStats() {
        return ResponseEntity.ok(Map.of("available", operationsService.getAvailableParkingCount()));
    }

    @PostMapping("/parking/slots")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParkingSlot> createSlot(@Valid @RequestBody ParkingSlotRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(operationsService.createParkingSlot(request));
    }

    @PostMapping("/public/parking/reserve")
    public ResponseEntity<ParkingSlot> reserveParking(@Valid @RequestBody ParkingReserveRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(operationsService.reserveParking(request));
    }

    @PatchMapping("/parking/{id}/release")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<ParkingSlot> releaseParking(@PathVariable UUID id) {
        return ResponseEntity.ok(operationsService.releaseParking(id));
    }
}
