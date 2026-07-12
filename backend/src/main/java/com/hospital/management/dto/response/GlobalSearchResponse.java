package com.hospital.management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GlobalSearchResponse {

    private List<DoctorResponse> doctors;
    private List<PatientResponse> patients;
    private List<MedicineResponse> medicines;
    private List<DepartmentResponse> departments;
    private List<LabTestResponse> labTests;
}
