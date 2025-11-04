package com.project.mycareconnect.controller;

import com.project.mycareconnect.dto.DtoDataRoom.DataRoomDto;
import com.project.mycareconnect.dto.DtoDataRoom.DataRoomRequest;
import com.project.mycareconnect.dto.DtoDataRoom.DataRoomResponseDto;
import com.project.mycareconnect.dto.DtoDataRoom.DataRoomResponseProjection;
import com.project.mycareconnect.model.Appointment;
import com.project.mycareconnect.model.Assistant;
import com.project.mycareconnect.model.DataRoom;
import com.project.mycareconnect.repository.AppointmentRepository;
import com.project.mycareconnect.repository.AssistantRepository;
import com.project.mycareconnect.service.DataRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/dataroom")
@RequiredArgsConstructor
public class DataRoomController {

    private final DataRoomService dataRoomService;

    @PreAuthorize("hasRole('assistant')")
    @PostMapping("/upload")
    public ResponseEntity<List<DataRoomDto>> uploadDocuments(
            @Valid @ModelAttribute DataRoomRequest request) throws IOException {
        List<DataRoomDto> result = dataRoomService.upload(request);
        return ResponseEntity.ok(result);
    }
//tester
  @GetMapping("/appointmentDocumentss/{appointmentId}")
public ResponseEntity<List<DataRoomResponseDto>> getDocumentsByAppointments(@PathVariable Long appointmentId) {
    List<DataRoomResponseDto> result = dataRoomService.getDocumentsByAppointment(appointmentId);
    return result.isEmpty()
            ? ResponseEntity.noContent().build()
            : ResponseEntity.ok(result);
}



    // Liste des fichiers liés à un rendez-vous

    @GetMapping("/appointmentDocuments/{appointmentId}")
    public ResponseEntity<List<DataRoomResponseDto>> getDocumentsByAppointment(@PathVariable Long appointmentId) {
        return ResponseEntity.ok(dataRoomService.getDocumentsByAppointment(appointmentId));
    }

    // Télécharger un fichier
    @GetMapping("/download/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long fileId) {
        DataRoom dr = dataRoomService.getFile(fileId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dr.getOriginalName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(dr.getContent());
    }
    @PreAuthorize("hasRole('assistant')")
    @DeleteMapping("/delete/{docId}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long docId) {
        dataRoomService.deleteDoc(docId);
        return ResponseEntity.noContent().build(); // 204
    }



}

