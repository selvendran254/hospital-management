package com.hospital.management.dto.response;

import com.hospital.management.domain.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffResponse {

    private UUID id;
    private UUID userId;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private UserRole role;
    private UUID departmentId;
    private String departmentName;
    private String phone;
    private LocalDate hireDate;
    private Boolean active;
}
