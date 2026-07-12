package com.hospital.management.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SupplierRequest {

    @NotBlank
    private String name;

    private String contactPerson;
    private String phone;
    private String email;
    private String address;
}
