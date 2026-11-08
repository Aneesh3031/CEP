package com.crashfinder.repository;

import com.crashfinder.model.LoginRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoginRecordRepository extends JpaRepository<LoginRecord, Long> {
    List<LoginRecord> findTop50ByOrderByLoginTimeDesc();
    List<LoginRecord> findByEmailIgnoreCaseOrderByLoginTimeDesc(String email);
    long countBySuccess(boolean success);
    Page<LoginRecord> findAllByOrderByLoginTimeDesc(Pageable pageable);
}
