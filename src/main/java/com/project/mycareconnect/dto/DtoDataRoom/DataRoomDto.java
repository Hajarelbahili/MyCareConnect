package com.project.mycareconnect.dto.DtoDataRoom;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class DataRoomDto {

    private MultipartFile file;


}