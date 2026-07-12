package com.hospital.management.service;

import com.hospital.management.dto.response.SearchResultItem;
import com.hospital.management.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final MedicineRepository medicineRepository;
    private final DepartmentRepository departmentRepository;
    private final LabTestRepository labTestRepository;

    public Map<String, List<SearchResultItem>> globalSearch(String query) {
        List<SearchResultItem> results = new ArrayList<>();
        if (query == null || query.isBlank()) {
            return Map.of("results", results);
        }

        doctorRepository.search(query, PageRequest.of(0, 5)).forEach(doctor ->
                results.add(SearchResultItem.builder()
                        .type("doctor")
                        .id(doctor.getId().toString())
                        .title(doctor.getFirstName() + " " + doctor.getLastName())
                        .build()));

        patientRepository.search(query, PageRequest.of(0, 5)).forEach(patient ->
                results.add(SearchResultItem.builder()
                        .type("patient")
                        .id(patient.getId().toString())
                        .title(patient.getFirstName() + " " + patient.getLastName())
                        .build()));

        medicineRepository.findByNameContainingIgnoreCase(query, PageRequest.of(0, 5)).forEach(medicine ->
                results.add(SearchResultItem.builder()
                        .type("medicine")
                        .id(medicine.getId().toString())
                        .title(medicine.getName())
                        .build()));

        departmentRepository.findAll().stream()
                .filter(d -> d.getName().toLowerCase().contains(query.toLowerCase()))
                .limit(5)
                .forEach(department ->
                        results.add(SearchResultItem.builder()
                                .type("department")
                                .id(department.getId().toString())
                                .title(department.getName())
                                .build()));

        labTestRepository.findAll().stream()
                .filter(t -> t.getName().toLowerCase().contains(query.toLowerCase()))
                .limit(5)
                .forEach(test ->
                        results.add(SearchResultItem.builder()
                                .type("lab_test")
                                .id(test.getId().toString())
                                .title(test.getName())
                                .build()));

        return Map.of("results", results);
    }
}
