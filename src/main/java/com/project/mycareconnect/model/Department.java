package com.project.mycareconnect.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Long departmentId;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(length = 500)
    private String description;

    private String floor;

    private String phoneNumber;
    private String email;
    @Lob
    @Column(name = "image")
    private byte[] imageData;
    private String imageName;
    private String imageType;

    private boolean active = true;


    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // empêche Jackson de sérialiser
    private List<Speciality> specialities;

    @OneToMany(mappedBy = "department")
    @JsonIgnore
    private List<Doctor> doctors;

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // empêche Jackson de sérialiser
    private List<Assistant> assistants;
}
