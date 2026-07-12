package com.hospital.management.service;

import com.hospital.management.domain.entity.*;
import com.hospital.management.domain.enums.FoodOrderStatus;
import com.hospital.management.domain.enums.ParkingSlotStatus;
import com.hospital.management.dto.request.*;
import com.hospital.management.dto.response.AmbulanceRouteResponse;
import com.hospital.management.dto.response.MapLocationResponse;
import com.hospital.management.exception.BadRequestException;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OperationsService {

    private final ClinicRepository clinicRepository;
    private final AmbulanceRepository ambulanceRepository;
    private final CanteenMenuItemRepository menuItemRepository;
    private final FoodOrderRepository foodOrderRepository;
    private final ParkingSlotRepository parkingSlotRepository;

    public MapLocationResponse getHospitalLocation() {
        Clinic clinic = clinicRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Hospital location not configured"));
        double lat = clinic.getLatitude() != null ? clinic.getLatitude() : 13.0827;
        double lng = clinic.getLongitude() != null ? clinic.getLongitude() : 80.2707;
        return MapLocationResponse.builder()
                .name(clinic.getName())
                .address(clinic.getAddress())
                .latitude(lat)
                .longitude(lng)
                .googleMapsUrl("https://www.google.com/maps?q=" + lat + "," + lng)
                .directionsUrl("https://www.google.com/maps/dir/?api=1&destination=" + lat + "," + lng)
                .build();
    }

    public List<AmbulanceRouteResponse> getAmbulanceRoutes() {
        Clinic hospital = clinicRepository.findAll().stream().findFirst().orElse(null);
        double hospLat = hospital != null && hospital.getLatitude() != null ? hospital.getLatitude() : 13.0827;
        double hospLng = hospital != null && hospital.getLongitude() != null ? hospital.getLongitude() : 80.2707;

        return ambulanceRepository.findAll().stream().map(amb -> {
            double lat = amb.getLatitude() != null ? amb.getLatitude() : hospLat + 0.01;
            double lng = amb.getLongitude() != null ? amb.getLongitude() : hospLng + 0.01;
            double destLat = amb.getDestinationLat() != null ? amb.getDestinationLat() : hospLat;
            double destLng = amb.getDestinationLng() != null ? amb.getDestinationLng() : hospLng;
            return AmbulanceRouteResponse.builder()
                    .id(amb.getId())
                    .vehicleNumber(amb.getVehicleNumber())
                    .driverName(amb.getDriverName())
                    .driverPhone(amb.getDriverPhone())
                    .location(amb.getLocation())
                    .latitude(lat)
                    .longitude(lng)
                    .destinationLat(destLat)
                    .destinationLng(destLng)
                    .routeUrl("https://www.google.com/maps/dir/?api=1&origin=" + lat + "," + lng
                            + "&destination=" + destLat + "," + destLng)
                    .available(amb.getAvailable())
                    .build();
        }).toList();
    }

    public List<CanteenMenuItem> getMenu() {
        return menuItemRepository.findByAvailableTrue();
    }

    public List<CanteenMenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }

    @Transactional
    public CanteenMenuItem createMenuItem(CanteenMenuItemRequest request) {
        CanteenMenuItem item = CanteenMenuItem.builder()
                .clinicId(request.getClinicId())
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .category(request.getCategory())
                .veg(request.getVeg() != null ? request.getVeg() : true)
                .available(request.getAvailable() != null ? request.getAvailable() : true)
                .imageUrl(request.getImageUrl())
                .build();
        return menuItemRepository.save(item);
    }

    @Transactional
    public CanteenMenuItem updateMenuItem(UUID id, CanteenMenuItemRequest request) {
        CanteenMenuItem item = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setPrice(request.getPrice());
        item.setCategory(request.getCategory());
        if (request.getVeg() != null) item.setVeg(request.getVeg());
        if (request.getAvailable() != null) item.setAvailable(request.getAvailable());
        item.setImageUrl(request.getImageUrl());
        return menuItemRepository.save(item);
    }

    @Transactional
    public void deleteMenuItem(UUID id) {
        menuItemRepository.deleteById(id);
    }

    @Transactional
    public FoodOrder placeFoodOrder(FoodOrderRequest request) {
        CanteenMenuItem item = menuItemRepository.findById(request.getMenuItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));
        if (!item.getAvailable()) {
            throw new BadRequestException("Item is not available");
        }
        int qty = request.getQuantity() != null ? request.getQuantity() : 1;
        FoodOrder order = FoodOrder.builder()
                .patientId(request.getPatientId())
                .menuItemId(item.getId())
                .itemName(item.getName())
                .quantity(qty)
                .totalAmount(item.getPrice().multiply(BigDecimal.valueOf(qty)))
                .roomNumber(request.getRoomNumber())
                .notes(request.getNotes())
                .status(FoodOrderStatus.PLACED)
                .build();
        return foodOrderRepository.save(order);
    }

    public List<FoodOrder> getFoodOrders() {
        return foodOrderRepository.findAll();
    }

    public List<FoodOrder> getPatientFoodOrders(UUID patientId) {
        return foodOrderRepository.findByPatientIdOrderByCreatedAtDesc(patientId);
    }

    @Transactional
    public FoodOrder updateFoodOrderStatus(UUID id, FoodOrderStatus status) {
        FoodOrder order = foodOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        order.setStatus(status);
        return foodOrderRepository.save(order);
    }

    public List<ParkingSlot> getParkingSlots() {
        return parkingSlotRepository.findAll();
    }

    public List<ParkingSlot> getAvailableParking() {
        return parkingSlotRepository.findByStatus(ParkingSlotStatus.AVAILABLE);
    }

    @Transactional
    public ParkingSlot createParkingSlot(ParkingSlotRequest request) {
        ParkingSlot slot = ParkingSlot.builder()
                .slotNumber(request.getSlotNumber())
                .floorLevel(request.getFloorLevel())
                .slotType(request.getSlotType())
                .status(ParkingSlotStatus.AVAILABLE)
                .build();
        return parkingSlotRepository.save(slot);
    }

    @Transactional
    public ParkingSlot reserveParking(ParkingReserveRequest request) {
        ParkingSlot slot;
        if (request.getSlotId() != null) {
            slot = parkingSlotRepository.findById(request.getSlotId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parking slot not found"));
        } else {
            slot = parkingSlotRepository.findByStatus(ParkingSlotStatus.AVAILABLE).stream().findFirst()
                    .orElseThrow(() -> new BadRequestException("No parking slots available"));
        }
        if (slot.getStatus() != ParkingSlotStatus.AVAILABLE) {
            throw new BadRequestException("Slot is not available");
        }
        slot.setStatus(ParkingSlotStatus.RESERVED);
        slot.setVisitorName(request.getVisitorName());
        slot.setVisitorPhone(request.getVisitorPhone());
        slot.setVehicleNumber(request.getVehicleNumber());
        return parkingSlotRepository.save(slot);
    }

    @Transactional
    public ParkingSlot releaseParking(UUID id) {
        ParkingSlot slot = parkingSlotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parking slot not found"));
        slot.setStatus(ParkingSlotStatus.AVAILABLE);
        slot.setVisitorName(null);
        slot.setVisitorPhone(null);
        slot.setVehicleNumber(null);
        return parkingSlotRepository.save(slot);
    }

    public long getAvailableParkingCount() {
        return parkingSlotRepository.countByStatus(ParkingSlotStatus.AVAILABLE);
    }
}
