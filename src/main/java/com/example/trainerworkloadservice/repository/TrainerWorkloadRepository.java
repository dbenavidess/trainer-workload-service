package com.example.trainerworkloadservice.repository;

import com.example.trainerworkloadservice.model.TrainerWorkload;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerWorkloadRepository extends MongoRepository<TrainerWorkload, String> {
}