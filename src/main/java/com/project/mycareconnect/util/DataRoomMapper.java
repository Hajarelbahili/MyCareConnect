package com.project.mycareconnect.util;

import com.project.mycareconnect.dto.DtoDataRoom.DataRoomDto;
import com.project.mycareconnect.dto.DtoDataRoom.DataRoomResponseDto;
import com.project.mycareconnect.model.DataRoom;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DataRoomMapper {

    // DTO -> Entity (sans les relations)
    public DataRoom toEntity(DataRoomDto dto) throws IOException {
        DataRoom dataRoom = new DataRoom();
        if (dto.getFile() != null) {
            dataRoom.setOriginalName(dto.getFile().getOriginalFilename());
            dataRoom.setContent(dto.getFile().getBytes()); // byte[] réel du fichier

        }
        return dataRoom;
    }

    // Entity -> DTO (utile pour exposer en API)
    public DataRoomDto toDto(DataRoom entity) {
        DataRoomDto dto = new DataRoomDto();

        // ⚠️ Pas de file content en sortie → sécurité et performance
        return dto;
    }
    public DataRoomResponseDto toResponseDto(DataRoom entity) {
        String uploadedBy = (entity.getUploadedBy() != null && entity.getUploadedBy().getUser() != null)
                ? entity.getUploadedBy().getUser().getUsername()
                : "Inconnu";

        return new DataRoomResponseDto(
                entity.getId(),
                entity.getOriginalName(),
                uploadedBy,
                entity.getUploadDate()
        );
    }
}