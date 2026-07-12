package com.hospital.management.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "insurance_policies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsurancePolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    @Column(name = "provider_name", nullable = false, length = 120)
    private String providerName;

    @Column(name = "policy_number", nullable = false, length = 120)
    private String policyNumber;

    @Column(name = "valid_till")
    private LocalDate validTill;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;
}
