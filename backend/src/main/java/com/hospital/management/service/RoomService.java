package com.hospital.management.service;

import com.hospital.management.domain.entity.*;
import com.hospital.management.domain.enums.AdmissionStatus;
import com.hospital.management.domain.enums.BedStatus;
import com.hospital.management.dto.request.AdmissionRequest;
import com.hospital.management.dto.request.BedRequest;
import com.hospital.management.dto.request.RoomRequest;
import com.hospital.management.dto.response.AdmissionResponse;
import com.hospital.management.dto.response.BedResponse;
import com.hospital.management.dto.response.PageResponse;
import com.hospital.management.dto.response.RoomResponse;
import com.hospital.management.exception.BadRequestException;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.mapper.EntityMapper;
import com.hospital.management.repository.AdmissionRepository;
import com.hospital.management.repository.BedRepository;
import com.hospital.management.repository.PatientRepository;
import com.hospital.management.repository.RoomRepository;
import com.hospital.management.util.PageUtils;
import com.hospital.management.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final BedRepository bedRepository;
    private final AdmissionRepository admissionRepository;
    private final PatientRepository patientRepository;
    private final EntityMapper entityMapper;
    private final SecurityUtils securityUtils;

    public PageResponse<RoomResponse> getAllRooms(int page, int size) {
        Pageable pageable = PageUtils.of(page, size, "roomNumber", "asc");
        return PageResponse.from(roomRepository.findAll(pageable).map(entityMapper::toRoomResponse));
    }

    public RoomResponse getRoomById(UUID id) {
        return entityMapper.toRoomResponse(findRoom(id));
    }

    @Transactional
    public RoomResponse createRoom(RoomRequest request) {
        if (roomRepository.existsByRoomNumber(request.getRoomNumber())) {
            throw new BadRequestException("Room number already exists");
        }
        Room room = Room.builder()
                .roomNumber(request.getRoomNumber())
                .roomType(request.getRoomType())
                .floor(request.getFloor() != null ? request.getFloor() : 1)
                .departmentId(request.getDepartmentId())
                .dailyRate(request.getDailyRate())
                .active(request.getActive() != null ? request.getActive() : true)
                .build();
        return entityMapper.toRoomResponse(roomRepository.save(room));
    }

    @Transactional
    public RoomResponse updateRoom(UUID id, RoomRequest request) {
        Room room = findRoom(id);
        room.setRoomNumber(request.getRoomNumber());
        room.setRoomType(request.getRoomType());
        if (request.getFloor() != null) room.setFloor(request.getFloor());
        room.setDepartmentId(request.getDepartmentId());
        if (request.getDailyRate() != null) room.setDailyRate(request.getDailyRate());
        if (request.getActive() != null) room.setActive(request.getActive());
        return entityMapper.toRoomResponse(roomRepository.save(room));
    }

    @Transactional
    public void deleteRoom(UUID id) {
        Room room = findRoom(id);
        room.setActive(false);
        roomRepository.save(room);
    }

    public PageResponse<BedResponse> getAllBeds(int page, int size) {
        Pageable pageable = PageUtils.of(page, size, "bedNumber", "asc");
        return PageResponse.from(bedRepository.findAll(pageable).map(entityMapper::toBedResponse));
    }

    @Transactional
    public BedResponse createBed(BedRequest request) {
        roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        Bed bed = Bed.builder()
                .roomId(request.getRoomId())
                .bedNumber(request.getBedNumber())
                .status(request.getStatus() != null ? request.getStatus() : BedStatus.AVAILABLE)
                .build();
        return entityMapper.toBedResponse(bedRepository.save(bed));
    }

    @Transactional
    public BedResponse updateBed(UUID id, BedRequest request) {
        Bed bed = findBed(id);
        if (request.getRoomId() != null) bed.setRoomId(request.getRoomId());
        if (request.getBedNumber() != null) bed.setBedNumber(request.getBedNumber());
        if (request.getStatus() != null) bed.setStatus(request.getStatus());
        return entityMapper.toBedResponse(bedRepository.save(bed));
    }

    @Transactional
    public void deleteBed(UUID id) {
        bedRepository.delete(findBed(id));
    }

    public PageResponse<AdmissionResponse> getAdmissions(int page, int size) {
        Pageable pageable = PageUtils.of(page, size, "admissionDate", "desc");
        return PageResponse.from(admissionRepository.findAll(pageable).map(entityMapper::toAdmissionResponse));
    }

    @Transactional
    public AdmissionResponse admit(AdmissionRequest request) {
        patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        Bed bed = bedRepository.findById(request.getBedId())
                .orElseThrow(() -> new ResourceNotFoundException("Bed not found"));
        if (bed.getStatus() != BedStatus.AVAILABLE) {
            throw new BadRequestException("Bed is not available");
        }

        User user = securityUtils.getCurrentUser();
        Admission admission = Admission.builder()
                .patientId(request.getPatientId())
                .bedId(request.getBedId())
                .admittedBy(user.getId())
                .diagnosis(request.getDiagnosis())
                .notes(request.getNotes())
                .status(AdmissionStatus.ADMITTED)
                .build();
        admission = admissionRepository.save(admission);

        bed.setStatus(BedStatus.OCCUPIED);
        bedRepository.save(bed);
        return entityMapper.toAdmissionResponse(admission);
    }

    @Transactional
    public AdmissionResponse discharge(UUID id) {
        Admission admission = admissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admission not found"));
        if (admission.getStatus() == AdmissionStatus.DISCHARGED) {
            throw new BadRequestException("Patient already discharged");
        }
        admission.setStatus(AdmissionStatus.DISCHARGED);
        admission.setDischargeDate(Instant.now());
        admission = admissionRepository.save(admission);

        Bed bed = bedRepository.findById(admission.getBedId())
                .orElseThrow(() -> new ResourceNotFoundException("Bed not found"));
        bed.setStatus(BedStatus.AVAILABLE);
        bedRepository.save(bed);
        return entityMapper.toAdmissionResponse(admission);
    }

    private Room findRoom(UUID id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found: " + id));
    }

    private Bed findBed(UUID id) {
        return bedRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bed not found: " + id));
    }
}
