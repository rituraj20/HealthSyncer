package com.healthsync.Patient.Service;

import com.healthsync.Patient.Entity.Patient;
import com.healthsync.Patient.Exception.ApiException;
import com.healthsync.Patient.Exception.ResourceNotFoundException;
import com.healthsync.Patient.Repository.PatientRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PatientService {
    private final PatientRepo patientRepo;
    private final PasswordEncoder passwordEncoder;

    public PatientService(PatientRepo patientRepo, PasswordEncoder passwordEncoder) {
        this.patientRepo = patientRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<Object> createPatient(Patient patient) {
        validateMobile(patient.getMobileNumber());
        validateEmail(patient.getEmail());

        if (patientRepo.existsByEmail(patient.getEmail()) || patientRepo.existsByMobileNumber(patient.getMobileNumber())) {
            throw new ApiException("Patient already exists");
        }
        if (patient.getPassword() == null) {
            throw new ApiException("Password must not be null");
        }
        if (!patient.getPassword().equals(patient.getConfirmPassword())) {
            throw new ApiException("Passwords do not match");
        }
        validatePassword(patient.getPassword());

        patient.setPassword(passwordEncoder.encode(patient.getPassword()));
        patient.setConfirmPassword(patient.getPassword());
        return ResponseEntity.ok(patientRepo.save(patient));
    }

    public ResponseEntity<Object> updatePatient(Patient patient, long mobileNumber) {
        Patient existing = patientRepo.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with mobile: " + mobileNumber));

        if (patient.getMobileNumber() != 0) {
            validateMobile(patient.getMobileNumber());
            if (!Objects.equals(existing.getMobileNumber(), patient.getMobileNumber())
                    && patientRepo.existsByMobileNumber(patient.getMobileNumber())) {
                throw new ApiException("Mobile number already exists");
            }
            existing.setMobileNumber(patient.getMobileNumber());
        }
        if (patient.getName() != null) {
            existing.setName(patient.getName());
        }
        if (patient.getPassword() != null) {
            validatePassword(patient.getPassword());
            existing.setPassword(passwordEncoder.encode(patient.getPassword()));
            existing.setConfirmPassword(existing.getPassword());
        }
        if (patient.getDob() != null) {
            existing.setDob(patient.getDob());
        }

        if (patient.getEmail() != null) {
            validateEmail(patient.getEmail());
            if (!patient.getEmail().equals(existing.getEmail()) && patientRepo.existsByEmail(patient.getEmail())) {
                throw new ApiException("Email already exists");
            }
            existing.setEmail(patient.getEmail());
        }

        if (patient.getGender() != null) {
            existing.setGender(patient.getGender());
        }

        return ResponseEntity.ok(patientRepo.save(existing));
    }

    public ResponseEntity<Object> deletePatient(long mobileNumber) {
        if (!patientRepo.existsByMobileNumber(mobileNumber)) {
            throw new ResourceNotFoundException("Patient not found with mobile: " + mobileNumber);
        }
        patientRepo.deleteByMobileNumber(mobileNumber);
        return ResponseEntity.ok(mobileNumber);
    }

    public ResponseEntity<Object> getPatientByMobileNumber(long mobileNumber) {
        return patientRepo.findByMobileNumber(mobileNumber)
                .map(patient -> ResponseEntity.ok((Object) patient))
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with mobile: " + mobileNumber));
    }

    private void validateMobile(long mobileNumber) {
        if (mobileNumber < 1000000000L || mobileNumber > 9999999999L) {
            throw new ApiException("Mobile number must be exactly 10 digits");
        }
    }

    private void validateEmail(String email) {
        if (email == null || !email.endsWith("@gmail.com")) {
            throw new ApiException("Email must end with @gmail.com");
        }
    }

    private void validatePassword(String password) {
        String passwordRegex =
                "^(?=.*[a-z])" +
                        "(?=.*[A-Z])" +
                        "(?=.*\\d)" +
                        "(?=.*[@$!%*?&])" +
                        "[A-Za-z\\d@$!%*?&]{8,15}$";

        if (!password.matches(passwordRegex)) {
            throw new ApiException("Password must be 8-15 characters and include uppercase, lowercase, number, and special character");
        }
    }
}
