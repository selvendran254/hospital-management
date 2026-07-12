package com.hospital.management.service;

import com.hospital.management.domain.entity.Notification;
import com.hospital.management.domain.entity.User;
import com.hospital.management.domain.enums.NotificationType;
import com.hospital.management.dto.response.NotificationResponse;
import com.hospital.management.dto.response.PageResponse;
import com.hospital.management.mapper.EntityMapper;
import com.hospital.management.repository.NotificationRepository;
import com.hospital.management.util.PageUtils;
import com.hospital.management.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final EntityMapper entityMapper;
    private final SecurityUtils securityUtils;

    public PageResponse<NotificationResponse> getAll(int page, int size) {
        User user = securityUtils.getCurrentUser();
        Pageable pageable = PageUtils.of(page, size, "createdAt", "desc");
        return PageResponse.from(notificationRepository.findByUserId(user.getId(), pageable)
                .map(entityMapper::toNotificationResponse));
    }

    @Transactional
    public void markAsRead(UUID id) {
        User user = securityUtils.getCurrentUser();
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new com.hospital.management.exception.ResourceNotFoundException("Notification not found"));
        if (!notification.getUserId().equals(user.getId())) {
            throw new com.hospital.management.exception.BadRequestException("Not your notification");
        }
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Transactional
    public void markAllAsRead() {
        User user = securityUtils.getCurrentUser();
        notificationRepository.findByUserIdAndRead(user.getId(), false, Pageable.unpaged())
                .forEach(notification -> {
                    notification.setRead(true);
                    notificationRepository.save(notification);
                });
    }

    public Map<String, Long> getUnreadCount() {
        User user = securityUtils.getCurrentUser();
        return Map.of("count", notificationRepository.countByUserIdAndReadFalse(user.getId()));
    }

    @Transactional
    public void notifyUser(UUID userId, String title, String message, NotificationType type) {
        Notification notification = Notification.builder()
                .userId(userId)
                .title(title)
                .message(message)
                .type(type)
                .read(false)
                .build();
        notificationRepository.save(notification);
    }
}
