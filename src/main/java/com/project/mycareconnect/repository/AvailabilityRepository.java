package com.project.mycareconnect.repository;

import com.project.mycareconnect.model.Availability;
import com.project.mycareconnect.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {



    List<Availability> findByDoctor_User_Id(Long userId);

    List<Availability> findByDoctor(Doctor doctor);
}
