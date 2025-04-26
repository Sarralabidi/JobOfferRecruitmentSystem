package com.example.piproject.repository;

import com.example.piproject.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
    public interface ApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByUserId(Long userId);

    List<JobApplication> findByJobOfferId(Long jobOfferId);

    Optional<JobApplication> findFirstByUserId(Long userId);

    @Query("SELECT j.cvFile FROM JobApplication j WHERE j.id = :id")
    byte[] findCvById(@Param("id") Long id);

    Object countAllByStatusIsLike(String status);
    @Query("SELECT jo.title FROM JobOffer jo " +
            "WHERE jo.id = (SELECT ja.jobOffer.id FROM JobApplication ja " +
            "GROUP BY ja.jobOffer.id " +
            "ORDER BY COUNT(ja.id) DESC " +
            "LIMIT 1)")
    String findTitleOfMostAppliedJobOffer();

    long countByApplicationDate(java.sql.Date date);

    @Query("SELECT a.jobOffer.title, COUNT(a) FROM JobApplication a GROUP BY a.jobOffer.title")
    List<Object[]> countApplicationsGroupedByJobTitle();

    @Query("SELECT CAST(a.applicationDate AS date), COUNT(a) " +
            "FROM JobApplication a " +
            "WHERE a.applicationDate >= :startDate " +
            "GROUP BY CAST(a.applicationDate AS date) " +
            "ORDER BY CAST(a.applicationDate AS date)")
    List<Object[]> countApplicationsGroupedByDay(@Param("startDate") Date startDate);




}

