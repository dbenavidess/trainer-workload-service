package com.example.trainerworkloadservice.repository;

import com.example.trainerworkloadservice.model.TrainerWorkload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerWorkloadRepository extends JpaRepository<TrainerWorkload, String> {
    // String = trainerUsername as primary key
}