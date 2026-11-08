package com.crashfinder.repository;

import com.crashfinder.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findByUserEmailIgnoreCaseOrderByCreatedAtAsc(String userEmail);
    Optional<Device> findByDeviceUid(String deviceUid);
    Optional<Device> findByDeviceUidAndUserEmailIgnoreCase(String deviceUid, String userEmail);
    void deleteByDeviceUid(String deviceUid);
}

