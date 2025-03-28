package com.example.piproject.repository;

import com.example.piproject.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
    public interface ApplicationRepository extends JpaRepository<JobApplication, Long> {
        List<JobApplication> findByUserId(Long userId);
        List<JobApplication> findByJobOfferId(Long jobOfferId);}

