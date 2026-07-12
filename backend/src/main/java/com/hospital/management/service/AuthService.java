package com.hospital.management.service;

import com.hospital.management.domain.entity.User;
import com.hospital.management.dto.request.ForgotPasswordRequest;
import com.hospital.management.dto.request.LoginRequest;
import com.hospital.management.dto.request.RegisterPatientRequest;
import com.hospital.management.dto.response.AuthResponse;
import com.hospital.management.dto.response.MessageResponse;
import com.hospital.management.dto.response.UserResponse;
import com.hospital.management.exception.BadRequestException;
import com.hospital.management.mapper.EntityMapper;
import com.hospital.management.repository.UserRepository;
import com.hospital.management.security.JwtService;
import com.hospital.management.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PatientService patientService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EntityMapper entityMapper;
    private final SecurityUtils securityUtils;
    private final EmailService emailService;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));
        if (!Boolean.TRUE.equals(user.getEnabled())) {
            throw new BadRequestException("Account is disabled");
        }
        return buildAuthResponse(user);
    }

    @Transactional
    public AuthResponse register(RegisterPatientRequest request) {
        patientService.registerPatient(request);
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Registration failed"));
        return buildAuthResponse(user);
    }

    public MessageResponse forgotPassword(ForgotPasswordRequest request) {
        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            String token = UUID.randomUUID().toString();
            emailService.sendPasswordResetEmail(user.getEmail(), token);
        });
        return MessageResponse.of("If the email exists, a reset link has been sent");
    }

    public UserResponse getProfile() {
        return entityMapper.toUserResponse(securityUtils.getCurrentUser());
    }

    public MessageResponse logout() {
        return MessageResponse.of("Logged out successfully");
    }

    private AuthResponse buildAuthResponse(User user) {
        return AuthResponse.builder()
                .token(jwtService.generateToken(user))
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }
}
