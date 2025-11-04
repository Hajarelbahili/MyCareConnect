package com.project.mycareconnect.dto.DtoDataRoom;

import java.time.LocalDateTime;

public interface DataRoomResponseProjection {
    Long getId();
    String getOriginalName();
    LocalDateTime getUploadDate();
    AssistantInfo getUploadedBy();

    interface AssistantInfo {
        String getUser(); // ou getUsername(), selon ton modèle Assistant -> User
    }
}

