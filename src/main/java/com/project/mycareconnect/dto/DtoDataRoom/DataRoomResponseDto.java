package com.project.mycareconnect.dto.DtoDataRoom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataRoomResponseDto {
    private Long id;
    private String originalName;
    private String uploadedByName; // ou uploadedById si tu veux
    private LocalDateTime uploadDate;
}

