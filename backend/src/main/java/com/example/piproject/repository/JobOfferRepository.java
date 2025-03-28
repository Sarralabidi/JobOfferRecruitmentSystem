package com.example.piproject.repository;

import com.example.piproject.entity.JobOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


    @Repository
    public interface JobOfferRepository extends JpaRepository<JobOffer, Long> {}

