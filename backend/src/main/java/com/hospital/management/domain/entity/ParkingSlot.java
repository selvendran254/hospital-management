package com.hospital.management.domain.entity;

import com.hospital.management.domain.enums.ParkingSlotStatus;
import com.hospital.management.domain.enums.ParkingSlotType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "parking_slots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "clinic_id")
    private UUID clinicId;

    @Column(name = "slot_number", nullable = false, unique = true, length = 20)
    private String slotNumber;

    @Column(name = "floor_level", length = 20)
    private String floorLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private ParkingSlotType slotType = ParkingSlotType.VISITOR;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private ParkingSlotStatus status = ParkingSlotStatus.AVAILABLE;

    @Column(name = "vehicle_number", length = 20)
    private String vehicleNumber;

    @Column(name = "visitor_name", length = 150)
    private String visitorName;

    @Column(name = "visitor_phone", length = 20)
    private String visitorPhone;
}
