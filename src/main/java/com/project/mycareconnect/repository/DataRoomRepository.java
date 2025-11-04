package com.project.mycareconnect.repository;

import com.project.mycareconnect.dto.DtoDataRoom.DataRoomResponseDto;
import com.project.mycareconnect.dto.DtoDataRoom.DataRoomResponseProjection;
import com.project.mycareconnect.model.DataRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DataRoomRepository extends JpaRepository<DataRoom, Long> {
   // List<DataRoom> findByAppointment_AppointmentId(Long appointmentId);

    List<DataRoom> findByAppointmentAppointmentId(Long appointmentId);

    @Query("SELECT new com.project.mycareconnect.dto.DtoDataRoom.DataRoomResponseDto(" +
            "d.id, d.originalName, d.uploadedBy.user.username, d.uploadDate) " +
            "FROM DataRoom d " +
            "WHERE d.appointment.appointmentId = :appointmentId")
    List<DataRoomResponseDto> findDtosByAppointmentId(@Param("appointmentId") Long appointmentId);


}


