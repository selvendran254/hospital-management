package com.hospital.management.service;

import com.hospital.management.domain.entity.*;
import com.hospital.management.domain.enums.Gender;
import com.hospital.management.domain.enums.UserRole;
import com.hospital.management.dto.request.DoctorAvailabilityRequest;
import com.hospital.management.dto.request.DoctorLeaveRequest;
import com.hospital.management.dto.request.DoctorRequest;
import com.hospital.management.dto.response.*;
import com.hospital.management.exception.BadRequestException;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.mapper.EntityMapper;
import com.hospital.management.repository.*;
import com.hospital.management.util.PageUtils;
import com.hospital.management.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final DoctorAvailabilityRepository availabilityRepository;
    private final DoctorLeaveRepository leaveRepository;
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityMapper entityMapper;
    private final SecurityUtils securityUtils;
    private final ClinicContextService clinicContextService;

    public PageResponse<DoctorResponse> getAll(int page, int size, String query, String sortBy, String sortDir,
                                               UUID departmentId, Gender gender, Boolean available,
                                               Integer minExperience, Integer maxExperience) {
        Pageable pageable = PageUtils.of(page, size, sortBy, sortDir);
        boolean hasFilters = departmentId != null || gender != null || available != null
                || minExperience != null || maxExperience != null;
        if (hasFilters || (query != null && !query.isBlank())) {
            return PageResponse.from(doctorRepository.searchWithFilters(
                    query, departmentId, gender, available, minExperience, maxExperience, pageable)
                    .map(entityMapper::toDoctorResponse));
        }
        return PageResponse.from(doctorRepository.findAll(pageable).map(entityMapper::toDoctorResponse));
    }

    public List<DoctorResponse> getPublicDoctors() {
        return doctorRepository.findByAvailableTrue().stream()
                .map(entityMapper::toDoctorResponse)
                .toList();
    }

    public DoctorResponse getById(UUID id) {
        return entityMapper.toDoctorResponse(findDoctor(id));
    }

    @Transactional
    public DoctorResponse create(DoctorRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered");
        }
        if (request.getDepartmentId() != null) {
            departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        }
        User user = User.builder()
                .clinicId(resolveClinicId())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.DOCTOR)
                .enabled(true)
                .build();
        user = userRepository.save(user);

        Doctor doctor = Doctor.builder()
                .clinicId(resolveClinicId())
                .userId(user.getId())
                .departmentId(request.getDepartmentId())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .specialization(request.getSpecialization())
                .qualification(request.getQualification())
                .experienceYears(request.getExperienceYears() != null ? request.getExperienceYears() : 0)
                .gender(request.getGender())
                .phone(request.getPhone())
                .bio(request.getBio())
                .consultationFee(request.getConsultationFee())
                .available(request.getAvailable() != null ? request.getAvailable() : true)
                .build();
        return entityMapper.toDoctorResponse(doctorRepository.save(doctor));
    }

    @Transactional
    public DoctorResponse update(UUID id, DoctorRequest request) {
        Doctor doctor = findDoctor(id);
        if (request.getDepartmentId() != null) {
            departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
            doctor.setDepartmentId(request.getDepartmentId());
        }
        doctor.setFirstName(request.getFirstName());
        doctor.setLastName(request.getLastName());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setQualification(request.getQualification());
        if (request.getExperienceYears() != null) doctor.setExperienceYears(request.getExperienceYears());
        doctor.setGender(request.getGender());
        doctor.setPhone(request.getPhone());
        doctor.setBio(request.getBio());
        if (request.getConsultationFee() != null) doctor.setConsultationFee(request.getConsultationFee());
        if (request.getAvailable() != null) doctor.setAvailable(request.getAvailable());
        return entityMapper.toDoctorResponse(doctorRepository.save(doctor));
    }

    @Transactional
    public void delete(UUID id) {
        Doctor doctor = findDoctor(id);
        doctor.setAvailable(false);
        doctorRepository.save(doctor);
    }

    public DoctorDashboardResponse getDashboard() {
        Doctor doctor = getCurrentDoctor();
        LocalDate today = LocalDate.now();
        return DoctorDashboardResponse.builder()
                .todayAppointments(appointmentRepository.countByDoctorIdAndAppointmentDateAndStatus(
                        doctor.getId(), today, com.hospital.management.domain.enums.AppointmentStatus.APPROVED))
                .pendingAppointments(appointmentRepository.countByDoctorIdAndStatus(
                        doctor.getId(), com.hospital.management.domain.enums.AppointmentStatus.PENDING))
                .completedAppointments(appointmentRepository.countByDoctorIdAndStatus(
                        doctor.getId(), com.hospital.management.domain.enums.AppointmentStatus.COMPLETED))
                .totalPatients(patientRepository.count())
                .upcomingLeaves(leaveRepository.countByDoctorIdAndEndDateGreaterThanEqual(doctor.getId(), today))
                .build();
    }

    public List<DoctorAvailabilityResponse> getSchedule(UUID doctorId) {
        UUID resolvedDoctorId = doctorId != null ? doctorId : getCurrentDoctor().getId();
        return availabilityRepository.findByDoctorId(resolvedDoctorId).stream()
                .map(entityMapper::toDoctorAvailabilityResponse)
                .toList();
    }

    @Transactional
    public List<DoctorAvailabilityResponse> updateSchedule(List<DoctorAvailabilityRequest> requests) {
        Doctor doctor = getCurrentDoctor();
        availabilityRepository.deleteByDoctorId(doctor.getId());
        List<DoctorAvailability> saved = new ArrayList<>();
        for (DoctorAvailabilityRequest request : requests) {
            DoctorAvailability availability = DoctorAvailability.builder()
                    .doctorId(doctor.getId())
                    .dayOfWeek(request.getDayOfWeek())
                    .startTime(request.getStartTime())
                    .endTime(request.getEndTime())
                    .slotDurationMins(request.getSlotDurationMins() != null ? request.getSlotDurationMins() : 30)
                    .build();
            saved.add(availabilityRepository.save(availability));
        }
        return saved.stream().map(entityMapper::toDoctorAvailabilityResponse).toList();
    }

    public PageResponse<DoctorLeaveResponse> getLeaves(int page, int size) {
        Doctor doctor = getCurrentDoctor();
        Pageable pageable = PageUtils.of(page, size, "startDate", "desc");
        return PageResponse.from(leaveRepository.findByDoctorId(doctor.getId(), pageable)
                .map(entityMapper::toDoctorLeaveResponse));
    }

    @Transactional
    public DoctorLeaveResponse requestLeave(DoctorLeaveRequest request) {
        Doctor doctor = getCurrentDoctor();
        DoctorLeave leave = DoctorLeave.builder()
                .doctorId(doctor.getId())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .reason(request.getReason())
                .build();
        return entityMapper.toDoctorLeaveResponse(leaveRepository.save(leave));
    }

    public PageResponse<PatientResponse> getPatients(int page, int size) {
        Pageable pageable = PageUtils.of(page, size, "createdAt", "desc");
        return PageResponse.from(patientRepository.findAll(pageable).map(entityMapper::toPatientResponse));
    }

    public Doctor getCurrentDoctor() {
        User user = securityUtils.getCurrentUser();
        return doctorRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor profile not found"));
    }

    private Doctor findDoctor(UUID id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found: " + id));
    }

    private UUID resolveClinicId() {
        return clinicContextService != null ? clinicContextService.resolveClinicId() : null;
    }
}
