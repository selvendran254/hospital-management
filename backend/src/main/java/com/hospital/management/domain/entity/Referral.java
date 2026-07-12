package com.hospital.management.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "referrals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Referral {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "from_doctor_id", nullable = false)
    private UUID fromDoctorId;

    @Column(name = "to_doctor_id", nullable = false)
    private UUID toDoctorId;

    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String reason;

    @Column(name = "referral_date", nullable = false)
    private LocalDate referralDate;
}
