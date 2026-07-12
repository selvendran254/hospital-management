package com.hospital.management.service;

import com.hospital.management.domain.entity.Department;
import com.hospital.management.dto.request.DepartmentRequest;
import com.hospital.management.dto.response.DepartmentResponse;
import com.hospital.management.dto.response.PageResponse;
import com.hospital.management.exception.BadRequestException;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.mapper.EntityMapper;
import com.hospital.management.repository.DepartmentRepository;
import com.hospital.management.util.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EntityMapper entityMapper;

    public PageResponse<DepartmentResponse> getAll(int page, int size, String sortBy, String sortDir) {
        Pageable pageable = PageUtils.of(page, size, sortBy, sortDir);
        return PageResponse.from(departmentRepository.findAll(pageable).map(entityMapper::toDepartmentResponse));
    }

    public List<DepartmentResponse> getPublicDepartments() {
        return departmentRepository.findByActiveTrue().stream()
                .map(entityMapper::toDepartmentResponse)
                .toList();
    }

    public DepartmentResponse getById(UUID id) {
        return entityMapper.toDepartmentResponse(findDepartment(id));
    }

    @Transactional
    public DepartmentResponse create(DepartmentRequest request) {
        if (departmentRepository.existsByNameIgnoreCase(request.getName())) {
            throw new BadRequestException("Department already exists: " + request.getName());
        }
        Department department = Department.builder()
                .name(request.getName())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .active(request.getActive() != null ? request.getActive() : true)
                .build();
        return entityMapper.toDepartmentResponse(departmentRepository.save(department));
    }

    @Transactional
    public DepartmentResponse update(UUID id, DepartmentRequest request) {
        Department department = findDepartment(id);
        department.setName(request.getName());
        department.setDescription(request.getDescription());
        department.setImageUrl(request.getImageUrl());
        if (request.getActive() != null) {
            department.setActive(request.getActive());
        }
        return entityMapper.toDepartmentResponse(departmentRepository.save(department));
    }

    @Transactional
    public void delete(UUID id) {
        Department department = findDepartment(id);
        department.setActive(false);
        departmentRepository.save(department);
    }

    private Department findDepartment(UUID id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found: " + id));
    }
}
