package com.hospital.management.service;

import com.hospital.management.domain.entity.Staff;
import com.hospital.management.domain.entity.User;
import com.hospital.management.dto.request.StaffRequest;
import com.hospital.management.dto.response.PageResponse;
import com.hospital.management.dto.response.StaffResponse;
import com.hospital.management.exception.BadRequestException;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.mapper.EntityMapper;
import com.hospital.management.repository.DepartmentRepository;
import com.hospital.management.repository.StaffRepository;
import com.hospital.management.repository.UserRepository;
import com.hospital.management.util.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StaffService {

    private final StaffRepository staffRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityMapper entityMapper;
    private final ClinicContextService clinicContextService;

    public PageResponse<StaffResponse> getAll(int page, int size) {
        Pageable pageable = PageUtils.of(page, size, "firstName", "asc");
        return PageResponse.from(staffRepository.findAll(pageable).map(entityMapper::toStaffResponse));
    }

    public StaffResponse getById(UUID id) {
        return entityMapper.toStaffResponse(findStaff(id));
    }

    @Transactional
    public StaffResponse create(StaffRequest request) {
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
                .role(request.getRole())
                .enabled(true)
                .build();
        user = userRepository.save(user);

        Staff staff = Staff.builder()
                .clinicId(resolveClinicId())
                .userId(user.getId())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(request.getRole())
                .departmentId(request.getDepartmentId())
                .phone(request.getPhone())
                .hireDate(request.getHireDate())
                .active(request.getActive() != null ? request.getActive() : true)
                .build();
        return entityMapper.toStaffResponse(staffRepository.save(staff));
    }

    @Transactional
    public StaffResponse update(UUID id, StaffRequest request) {
        Staff staff = findStaff(id);
        staff.setFirstName(request.getFirstName());
        staff.setLastName(request.getLastName());
        staff.setRole(request.getRole());
        staff.setDepartmentId(request.getDepartmentId());
        staff.setPhone(request.getPhone());
        if (request.getHireDate() != null) staff.setHireDate(request.getHireDate());
        if (request.getActive() != null) staff.setActive(request.getActive());
        return entityMapper.toStaffResponse(staffRepository.save(staff));
    }

    @Transactional
    public void delete(UUID id) {
        Staff staff = findStaff(id);
        staff.setActive(false);
        staffRepository.save(staff);
    }

    private Staff findStaff(UUID id) {
        return staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found: " + id));
    }

    private UUID resolveClinicId() {
        return clinicContextService != null ? clinicContextService.resolveClinicId() : null;
    }
}
