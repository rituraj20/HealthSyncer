package com.healthsync.Doctor.Repository;

import com.healthsync.Doctor.Entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepo extends JpaRepository<Doctor, Integer> {
    boolean existsByEmail(String email);
    boolean existsByMobileNumber(long mobileNumber);
    Optional<Doctor> findByMobileNumber(long mobileNumber);
    void deleteByMobileNumber(long mobileNumber);
}
