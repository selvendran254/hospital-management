package com.hospital.management.repository;

import com.hospital.management.domain.entity.Appointment;
import com.hospital.management.domain.enums.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    Page<Appointment> findByPatientId(UUID patientId, Pageable pageable);

    Page<Appointment> findByDoctorId(UUID doctorId, Pageable pageable);

    List<Appointment> findByDoctorIdAndAppointmentDate(UUID doctorId, LocalDate date);

    List<Appointment> findByDoctorIdAndAppointmentDateAndStatus(
            UUID doctorId, LocalDate date, AppointmentStatus status);
    List<Appointment> findByClinicId(UUID clinicId);
    long countByClinicId(UUID clinicId);

    long countByDoctorIdAndAppointmentDateAndStatus(UUID doctorId, LocalDate date, AppointmentStatus status);

    long countByDoctorIdAndStatus(UUID doctorId, AppointmentStatus status);

    @Query("SELECT a FROM Appointment a WHERE a.doctorId = :doctorId AND a.appointmentDate = :date " +
           "AND a.startTime < :endTime AND a.endTime > :startTime AND a.status <> 'CANCELLED'")
    List<Appointment> findConflictingAppointments(
            @Param("doctorId") UUID doctorId,
            @Param("date") LocalDate date,
            @Param("startTime") java.time.LocalTime startTime,
            @Param("endTime") java.time.LocalTime endTime);
}
