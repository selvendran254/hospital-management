package com.hospital.management.service;

import com.hospital.management.domain.entity.*;
import com.hospital.management.domain.enums.SurgeryStatus;
import com.hospital.management.domain.enums.TransplantStatus;
import com.hospital.management.dto.request.FeatureRequests;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdvancedMedicalService {

    private final MedicalImageRepository medicalImageRepository;
    private final OperationTheatreRepository operationTheatreRepository;
    private final SurgeryScheduleRepository surgeryScheduleRepository;
    private final OrganTransplantRegistryRepository transplantRegistryRepository;
    private final ClinicContextService clinicContextService;

    @Transactional
    public MedicalImage addMedicalImage(FeatureRequests.MedicalImageRequest request) {
        return medicalImageRepository.save(MedicalImage.builder()
                .clinicId(clinicContextService.resolveClinicId())
                .patientId(request.getPatientId())
                .imageUrl(request.getImageUrl())
                .imageType(request.getImageType())
                .notes(request.getNotes())
                .build());
    }

    public List<MedicalImage> imagesForPatient(UUID patientId) {
        return medicalImageRepository.findByClinicIdAndPatientId(clinicContextService.resolveClinicId(), patientId);
    }

    public MedicalImage viewMedicalImage(UUID imageId) {
        return medicalImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Medical image not found: " + imageId));
    }

    @Transactional
    public OperationTheatre createTheatre(FeatureRequests.OperationTheatreRequest request) {
        return operationTheatreRepository.save(OperationTheatre.builder()
                .clinicId(clinicContextService.resolveClinicId())
                .theatreCode(request.getTheatreCode())
                .name(request.getName())
                .floorNumber(request.getFloorNumber())
                .active(true)
                .build());
    }

    @Transactional
    public SurgerySchedule scheduleSurgery(FeatureRequests.SurgeryScheduleRequest request) {
        return surgeryScheduleRepository.save(SurgerySchedule.builder()
                .clinicId(clinicContextService.resolveClinicId())
                .patientId(request.getPatientId())
                .doctorId(request.getDoctorId())
                .operationTheatreId(request.getOperationTheatreId())
                .surgeryName(request.getSurgeryName())
                .surgeryDate(request.getSurgeryDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .status(request.getStatus() == null ? SurgeryStatus.SCHEDULED : request.getStatus())
                .notes(request.getNotes())
                .build());
    }

    public List<SurgerySchedule> surgeriesByDate(LocalDate date) {
        return surgeryScheduleRepository.findByClinicIdAndSurgeryDate(clinicContextService.resolveClinicId(), date);
    }

    @Transactional
    public OrganTransplantRegistry registerTransplant(FeatureRequests.TransplantRegistryRequest request) {
        return transplantRegistryRepository.save(OrganTransplantRegistry.builder()
                .clinicId(clinicContextService.resolveClinicId())
                .donorName(request.getDonorName())
                .recipientName(request.getRecipientName())
                .organType(request.getOrganType())
                .status(request.getStatus() == null ? TransplantStatus.REGISTERED : request.getStatus())
                .notes(request.getNotes())
                .build());
    }

    public List<OrganTransplantRegistry> transplants() {
        return transplantRegistryRepository.findByClinicId(clinicContextService.resolveClinicId());
    }
}
