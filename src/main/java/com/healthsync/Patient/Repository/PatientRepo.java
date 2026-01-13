package com.healthsync.Patient.Repository;

import com.healthsync.Patient.Entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepo extends JpaRepository<Patient, Integer> {
    boolean existsByEmail(String email);
    boolean existsByMobileNumber(long mobileNumber);
    Optional<Patient> findByMobileNumber(long mobileNumber);
    void deleteByMobileNumber(long mobileNumber);
}
