package com.project.mycareconnect.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity

public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    private Long doctorId;


    @ManyToOne
    @JoinColumn(name = "speciality_id")
    private Speciality speciality;


    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;


    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments;


    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Availability> availability;

    // ✅ Durée standard de consultation (en minutes)
    @Column(name = "consultation_duration", nullable = false)
    private Integer consultationDuration;
}
