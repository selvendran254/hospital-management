package com.hospital.management.service;

import com.hospital.management.domain.entity.Appointment;
import com.hospital.management.domain.entity.Doctor;
import com.hospital.management.domain.entity.Patient;
import com.hospital.management.domain.entity.User;
import com.hospital.management.domain.enums.AppointmentStatus;
import com.hospital.management.domain.enums.NotificationType;
import com.hospital.management.domain.enums.UserRole;
import com.hospital.management.dto.request.AppointmentRequest;
import com.hospital.management.dto.request.AppointmentUpdateRequest;
import com.hospital.management.dto.response.AppointmentResponse;
import com.hospital.management.dto.response.PageResponse;
import com.hospital.management.exception.BadRequestException;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.mapper.EntityMapper;
import com.hospital.management.repository.AppointmentRepository;
import com.hospital.management.repository.DoctorRepository;
import com.hospital.management.repository.PatientRepository;
import com.hospital.management.util.PageUtils;
import com.hospital.management.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final NotificationService notificationService;
    private final EntityMapper entityMapper;
    private final SecurityUtils securityUtils;
    private final ClinicContextService clinicContextService;

    public PageResponse<AppointmentResponse> getAll(int page, int size, String sortBy, String sortDir) {
        Pageable pageable = PageUtils.of(page, size, sortBy, sortDir);
        return PageResponse.from(appointmentRepository.findAll(pageable).map(entityMapper::toAppointmentResponse));
    }

    public AppointmentResponse getById(UUID id) {
        return entityMapper.toAppointmentResponse(findAppointment(id));
    }

    @Transactional
    public AppointmentResponse create(AppointmentRequest request) {
        validateEntities(request.getPatientId(), request.getDoctorId());
        validateNoConflict(request.getDoctorId(), request.getAppointmentDate(),
                request.getStartTime(), request.getEndTime(), null);

        Appointment appointment = Appointment.builder()
                .clinicId(resolveClinicId())
                .patientId(request.getPatientId())
                .doctorId(request.getDoctorId())
                .appointmentDate(request.getAppointmentDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .reason(request.getReason())
                .notes(request.getNotes())
                .isVideo(request.getIsVideo() != null ? request.getIsVideo() : false)
                .meetingUrl(request.getMeetingUrl())
                .status(AppointmentStatus.PENDING)
                .build();
        Appointment saved = appointmentRepository.save(appointment);

        patientRepository.findById(request.getPatientId()).ifPresent(patient ->
                notificationService.notifyUser(patient.getUserId(), "Appointment Booked",
                        "Your appointment has been booked and is pending approval.",
                        NotificationType.APPOINTMENT_BOOKED));

        return entityMapper.toAppointmentResponse(saved);
    }

    @Transactional
    public AppointmentResponse update(UUID id, AppointmentUpdateRequest request) {
        Appointment appointment = findAppointment(id);
        if (appointment.getStatus() == AppointmentStatus.CANCELLED
                || appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new BadRequestException("Cannot update a " + appointment.getStatus().name().toLowerCase() + " appointment");
        }
        if (request.getAppointmentDate() != null) appointment.setAppointmentDate(request.getAppointmentDate());
        if (request.getStartTime() != null) appointment.setStartTime(request.getStartTime());
        if (request.getEndTime() != null) appointment.setEndTime(request.getEndTime());
        if (request.getReason() != null) appointment.setReason(request.getReason());
        if (request.getNotes() != null) appointment.setNotes(request.getNotes());
        if (request.getIsVideo() != null) appointment.setIsVideo(request.getIsVideo());
        if (request.getMeetingUrl() != null) appointment.setMeetingUrl(request.getMeetingUrl());
        if (request.getStatus() != null) appointment.setStatus(request.getStatus());
        return entityMapper.toAppointmentResponse(appointmentRepository.save(appointment));
    }

    @Transactional
    public AppointmentResponse cancel(UUID id) {
        return updateStatus(id, AppointmentStatus.CANCELLED, NotificationType.APPOINTMENT_CANCELLED,
                "Appointment Cancelled", "Your appointment has been cancelled.");
    }

    @Transactional
    public AppointmentResponse approve(UUID id) {
        return updateStatus(id, AppointmentStatus.APPROVED, NotificationType.APPOINTMENT_APPROVED,
                "Appointment Approved", "Your appointment has been approved.");
    }

    @Transactional
    public AppointmentResponse complete(UUID id) {
        return updateStatus(id, AppointmentStatus.COMPLETED, NotificationType.GENERAL,
                "Appointment Completed", "Your appointment has been marked as completed.");
    }

    @Transactional
    public AppointmentResponse checkIn(UUID id) {
        Appointment appointment = findAppointment(id);
        if (appointment.getStatus() != AppointmentStatus.APPROVED) {
            throw new BadRequestException("Only approved appointments can be checked in");
        }
        appointment.setNotes(appendNote(appointment.getNotes(), "Patient checked in"));
        return entityMapper.toAppointmentResponse(appointmentRepository.save(appointment));
    }

    public PageResponse<AppointmentResponse> getMyAppointments(int page, int size) {
        User user = securityUtils.getCurrentUser();
        Pageable pageable = PageUtils.of(page, size, "appointmentDate", "desc");
        if (user.getRole() == UserRole.PATIENT) {
            Patient patient = patientRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found"));
            return PageResponse.from(appointmentRepository.findByPatientId(patient.getId(), pageable)
                    .map(entityMapper::toAppointmentResponse));
        }
        if (user.getRole() == UserRole.DOCTOR) {
            Doctor doctor = doctorRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Doctor profile not found"));
            return PageResponse.from(appointmentRepository.findByDoctorId(doctor.getId(), pageable)
                    .map(entityMapper::toAppointmentResponse));
        }
        return getAll(page, size, "appointmentDate", "desc");
    }

    private AppointmentResponse updateStatus(UUID id, AppointmentStatus status,
                                             NotificationType type, String title, String message) {
        Appointment appointment = findAppointment(id);
        appointment.setStatus(status);
        Appointment saved = appointmentRepository.save(appointment);
        patientRepository.findById(appointment.getPatientId()).ifPresent(patient ->
                notificationService.notifyUser(patient.getUserId(), title, message, type));
        return entityMapper.toAppointmentResponse(saved);
    }

    private void validateEntities(UUID patientId, UUID doctorId) {
        patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
    }

    private void validateNoConflict(UUID doctorId, java.time.LocalDate date,
                                  java.time.LocalTime start, java.time.LocalTime end, UUID excludeId) {
        var conflicts = appointmentRepository.findConflictingAppointments(doctorId, date, start, end);
        boolean hasConflict = conflicts.stream()
                .anyMatch(a -> excludeId == null || !a.getId().equals(excludeId));
        if (hasConflict) {
            throw new BadRequestException("Doctor has a conflicting appointment at this time");
        }
    }

    private String appendNote(String existing, String note) {
        return existing == null ? note : existing + "\n" + note;
    }

    private Appointment findAppointment(UUID id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found: " + id));
    }

    private UUID resolveClinicId() {
        return clinicContextService != null ? clinicContextService.resolveClinicId() : null;
    }
}
