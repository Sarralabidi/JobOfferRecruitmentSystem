package com.example.piproject.repository;

import com.example.piproject.entity.JobOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


    @Repository
    public interface JobOfferRepository extends JpaRepository<JobOffer, Long> {
        String findJobDescriptionById(Long jobOfferId);

        @Query("SELECT j.description FROM JobOffer j WHERE j.id = :jobOfferId")
        String findJobOfferDescriptionById(@Param("jobOfferId") Long jobOfferId);

        Object countAllByStatusIsLike(String status);
    }


