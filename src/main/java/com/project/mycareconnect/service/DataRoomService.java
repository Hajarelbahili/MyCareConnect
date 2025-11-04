package com.project.mycareconnect.service;


import com.project.mycareconnect.dto.DtoDataRoom.DataRoomDto;
import com.project.mycareconnect.dto.DtoDataRoom.DataRoomRequest;
import com.project.mycareconnect.dto.DtoDataRoom.DataRoomResponseDto;
import com.project.mycareconnect.exception.FileProcessingException;
import com.project.mycareconnect.exception.InvalidDataException;
import com.project.mycareconnect.model.Appointment;
import com.project.mycareconnect.model.Assistant;
import com.project.mycareconnect.model.DataRoom;
import com.project.mycareconnect.repository.AppointmentRepository;
import com.project.mycareconnect.repository.AssistantRepository;
import com.project.mycareconnect.repository.DataRoomRepository;
import com.project.mycareconnect.util.DataRoomMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DataRoomService {

    private final DataRoomRepository dataRoomRepository;
    private final AppointmentRepository appointmentRepository;
    private final AssistantRepository assistantRepository;
    private final DataRoomMapper dataRoomMapper;

    public List<DataRoomDto> upload(DataRoomRequest request) {

        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new InvalidDataException("Appointment not found with id " + request.getAppointmentId()));

        Assistant assistant = assistantRepository.findById(request.getUploadedBy())
                .orElseThrow(() -> new InvalidDataException("Assistant not found with id " + request.getUploadedBy()));
        request.getDocuments().forEach(d -> {
            System.out.println(d.getFile());
            System.out.println(d.getFile() != null ? d.getFile().getClass() : "null");
        });


        return request.getDocuments().stream()
                .map(dto -> {
                    try {
                        DataRoom dataRoom = dataRoomMapper.toEntity(dto);
                        dataRoom.setAppointment(appointment);
                        dataRoom.setUploadedBy(assistant);

                        DataRoom saved = dataRoomRepository.save(dataRoom);
                        return dataRoomMapper.toDto(saved);
                    } catch (IOException e) {
                        throw new FileProcessingException("Impossible de lire le fichier : " + dto.getFile().getOriginalFilename(), e);
                    }
                })
                .collect(Collectors.toList());
    }



    public List<DataRoomResponseDto> getDocumentsByAppointment(Long appointmentId) {
        return dataRoomRepository.findDtosByAppointmentId(appointmentId);
    }

    public DataRoom getFile(Long fileId) {
        return dataRoomRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("Fichier introuvable"));
    }


    public void deleteDoc(Long docId) {
        DataRoom dataRoom = dataRoomRepository.findById(docId)
                .orElseThrow(() -> new InvalidDataException("Fichier introuvable avec l'id : " + docId));

        dataRoomRepository.delete(dataRoom);
    }
}

