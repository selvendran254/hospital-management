package com.hospital.management.domain.enums;

/**
 * Alias for user roles in the system. {@link UserRole} is the canonical enum used by JPA entities.
 */
public enum Role {
    ADMIN,
    DOCTOR,
    RECEPTIONIST,
    LABORATORY_STAFF,
    PHARMACIST,
    PATIENT;

    public UserRole toUserRole() {
        return UserRole.valueOf(name());
    }

    public static Role fromUserRole(UserRole userRole) {
        return valueOf(userRole.name());
    }
}
