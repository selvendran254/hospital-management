package com.hospital.management.service;

import com.hospital.management.domain.entity.Appointment;
import com.hospital.management.domain.entity.Doctor;
import com.hospital.management.domain.entity.Patient;
import com.hospital.management.dto.request.AppointmentRequest;
import com.hospital.management.dto.response.AppointmentResponse;
import com.hospital.management.mapper.EntityMapper;
import com.hospital.management.repository.AppointmentRepository;
import com.hospital.management.repository.DoctorRepository;
import com.hospital.management.repository.PatientRepository;
import com.hospital.management.util.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private NotificationService notificationService;
    @Mock
    private EntityMapper entityMapper;
    @Mock
    private SecurityUtils securityUtils;

    @InjectMocks
    private AppointmentService appointmentService;

    @Test
    void createAppointmentSupportsVideoFields() {
        UUID patientId = UUID.randomUUID();
        UUID doctorId = UUID.randomUUID();

        AppointmentRequest request = new AppointmentRequest();
        request.setPatientId(patientId);
        request.setDoctorId(doctorId);
        request.setAppointmentDate(LocalDate.now().plusDays(1));
        request.setStartTime(LocalTime.of(10, 0));
        request.setEndTime(LocalTime.of(10, 30));
        request.setIsVideo(true);
        request.setMeetingUrl("https://meet.local/abc");

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(Patient.builder().id(patientId).build()));
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(Doctor.builder().id(doctorId).build()));
        when(appointmentRepository.findConflictingAppointments(any(), any(), any(), any())).thenReturn(List.of());
        when(appointmentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(entityMapper.toAppointmentResponse(any())).thenAnswer(inv -> {
            Appointment a = inv.getArgument(0);
            return AppointmentResponse.builder()
                    .patientId(a.getPatientId())
                    .doctorId(a.getDoctorId())
                    .isVideo(a.getIsVideo())
                    .meetingUrl(a.getMeetingUrl())
                    .build();
        });

        AppointmentResponse response = appointmentService.create(request);

        assertThat(response.getIsVideo()).isTrue();
        assertThat(response.getMeetingUrl()).isEqualTo("https://meet.local/abc");
    }
}
