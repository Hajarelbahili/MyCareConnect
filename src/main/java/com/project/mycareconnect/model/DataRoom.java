package com.project.mycareconnect.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "data_files")
public class DataRoom {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String originalName;

        // Contenu du fichier stocké directement en BLOB
        @Lob
        @Basic(fetch = FetchType.LAZY)
        @Column(name = "file_content")
        private byte[] content;

        @CreationTimestamp
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
        private LocalDateTime uploadDate;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "appointment_id", nullable = false)
        private Appointment appointment;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "assistant_id", nullable = false)
        private Assistant uploadedBy;


}
