package com.hospital.management.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "drug_interactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DrugInteraction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "drug_a", nullable = false, length = 150)
    private String drugA;

    @Column(name = "drug_b", nullable = false, length = 150)
    private String drugB;

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String severity = "MEDIUM";

    @Column(nullable = false, columnDefinition = "TEXT")
    private String advisory;
}
