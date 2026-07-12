package com.hospital.management.domain.entity;

import com.hospital.management.domain.enums.AdmissionStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "admissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Admission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", insertable = false, updatable = false)
    private Patient patient;

    @Column(name = "bed_id", nullable = false)
    private UUID bedId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bed_id", insertable = false, updatable = false)
    private Bed bed;

    @Column(name = "admitted_by")
    private UUID admittedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admitted_by", insertable = false, updatable = false)
    private User admittedByUser;

    @CreationTimestamp
    @Column(name = "admission_date", nullable = false, updatable = false)
    private Instant admissionDate;

    @Column(name = "discharge_date")
    private Instant dischargeDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private AdmissionStatus status = AdmissionStatus.ADMITTED;

    @Column(columnDefinition = "TEXT")
    private String diagnosis;

    @Column(columnDefinition = "TEXT")
    private String notes;
}
