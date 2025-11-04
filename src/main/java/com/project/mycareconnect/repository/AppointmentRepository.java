package com.project.mycareconnect.repository;

import com.project.mycareconnect.enums.AppointmentStatus;
import com.project.mycareconnect.model.Appointment;
import com.project.mycareconnect.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment,Long> {

    @Query(value = "SELECT can_book(:doctorId, :patientId, :date, :startTime)", nativeQuery = true)
    boolean canBook(
            @Param("doctorId") Long doctorId,
            @Param("patientId") Long patientId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime
    );
    @Query("SELECT DISTINCT a FROM Appointment a " +
            "JOIN FETCH a.doctor d " +
            "JOIN FETCH a.patient p " +
            "WHERE (:status IS NULL OR a.status = :status) " +
            "AND (:patientId IS NULL OR a.patient.patient_id = :patientId) " +
            "AND (:doctorId IS NULL OR a.doctor.doctorId = :doctorId)")
    List<Appointment> searchAppointments(
            @Param("patientId") Long patientId,
            @Param("doctorId") Long doctorId,
            @Param("status") AppointmentStatus status
    );


}
