package com.hospital.management.service;

import com.hospital.management.domain.entity.User;
import com.hospital.management.domain.enums.UserRole;
import com.hospital.management.dto.request.LoginRequest;
import com.hospital.management.dto.response.AuthResponse;
import com.hospital.management.mapper.EntityMapper;
import com.hospital.management.repository.UserRepository;
import com.hospital.management.security.JwtService;
import com.hospital.management.util.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PatientService patientService;
    @Mock
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private EntityMapper entityMapper;
    @Mock
    private SecurityUtils securityUtils;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private AuthService authService;

    @Test
    void loginReturnsTokenForEnabledUser() {
        UUID id = UUID.randomUUID();
        User user = User.builder()
                .id(id)
                .email("user@hospital.com")
                .role(UserRole.PATIENT)
                .enabled(true)
                .build();
        LoginRequest request = new LoginRequest();
        request.setEmail(user.getEmail());
        request.setPassword("secret");

        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwt-token");

        AuthResponse response = authService.login(request);

        assertThat(response.getToken()).isEqualTo("jwt-token");
        assertThat(response.getUserId()).isEqualTo(id);
        assertThat(response.getEmail()).isEqualTo("user@hospital.com");
    }
}
