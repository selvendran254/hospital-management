package com.hospital.management.dto.response;

import com.hospital.management.domain.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private UUID id;
    private String email;
    private UserRole role;
    private Boolean enabled;
    private String profileImageUrl;
    private Instant createdAt;
}
