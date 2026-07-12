package com.hospital.management.service;

import com.hospital.management.domain.entity.User;
import com.hospital.management.dto.response.PageResponse;
import com.hospital.management.dto.response.UserResponse;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.mapper.EntityMapper;
import com.hospital.management.repository.UserRepository;
import com.hospital.management.util.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EntityMapper entityMapper;

    public PageResponse<UserResponse> getAll(int page, int size, String sortBy, String sortDir) {
        Pageable pageable = PageUtils.of(page, size, sortBy, sortDir);
        return PageResponse.from(userRepository.findAll(pageable).map(entityMapper::toUserResponse));
    }

    public UserResponse getById(UUID id) {
        return entityMapper.toUserResponse(findUser(id));
    }

    @Transactional
    public UserResponse updateStatus(UUID id, boolean enabled) {
        User user = findUser(id);
        user.setEnabled(enabled);
        return entityMapper.toUserResponse(userRepository.save(user));
    }

    private User findUser(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
    }
}
