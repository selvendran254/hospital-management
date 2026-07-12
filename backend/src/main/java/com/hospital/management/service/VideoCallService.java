package com.hospital.management.service;

import com.hospital.management.dto.request.FeatureRequests;
import com.hospital.management.dto.response.FeatureResponses;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class VideoCallService {

    @Value("${app.integrations.jitsi.base-url:https://meet.jit.si}")
    private String jitsiBaseUrl;

    public FeatureResponses.VideoRoomResponse generateRoom(FeatureRequests.VideoRoomRequest request) {
        String roomName = "hms-" + request.getAppointmentId() + "-" + UUID.randomUUID().toString().substring(0, 8);
        String roomUrl = jitsiBaseUrl + "/" + roomName + "#userInfo.displayName=%22" + request.getParticipantName() + "%22";
        return FeatureResponses.VideoRoomResponse.builder()
                .roomName(roomName)
                .roomUrl(roomUrl)
                .build();
    }
}
