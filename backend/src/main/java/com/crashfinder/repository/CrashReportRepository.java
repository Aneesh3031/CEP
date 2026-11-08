package com.crashfinder.repository;

import com.crashfinder.model.CrashReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CrashReportRepository extends JpaRepository<CrashReport, Long> {
    List<CrashReport> findTop20ByOrderByCreatedAtDesc();
    List<CrashReport> findByDeviceUidOrderByCreatedAtDesc(String deviceUid);
    List<CrashReport> findByUserEmailIgnoreCaseOrderByCreatedAtDesc(String userEmail);
    Optional<CrashReport> findByReportId(String reportId);
    void deleteByReportId(String reportId);
    void deleteByDeviceUid(String deviceUid);
    long countByResultNot(String result);
}

