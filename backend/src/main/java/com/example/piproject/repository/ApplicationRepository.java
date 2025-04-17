package com.example.piproject.repository;

import com.example.piproject.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
    public interface ApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByUserId(Long userId);

    List<JobApplication> findByJobOfferId(Long jobOfferId);

    Optional<JobApplication> findFirstByUserId(Long userId);

    @Query("SELECT j.cvFile FROM JobApplication j WHERE j.id = :id")
    byte[] findCvById(@Param("id") Long id);
}

