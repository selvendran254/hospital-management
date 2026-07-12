package com.hospital.management.dto.request;

import com.hospital.management.domain.enums.BloodGroup;
import com.hospital.management.domain.enums.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientUpdateRequest {

    private String firstName;
    private String lastName;
    private String phone;
    private LocalDate dateOfBirth;
    private Gender gender;
    private BloodGroup bloodGroup;
    private String address;
    private String emergencyContact;
    private String medicalHistory;
}
