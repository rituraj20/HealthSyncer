package com.healthsync.Doctor.Service;

import com.healthsync.Doctor.Entity.Doctor;
import com.healthsync.Doctor.Repository.DoctorRepo;
import com.healthsync.Patient.Exception.ApiException;
import com.healthsync.Patient.Exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class DoctorService {
    private final DoctorRepo doctorRepo;
    private final PasswordEncoder passwordEncoder;

    public DoctorService(DoctorRepo doctorRepo, PasswordEncoder passwordEncoder) {
        this.doctorRepo = doctorRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<Object> createDoctor(Doctor doctor) {
        validateMobile(doctor.getMobileNumber());
        validateEmail(doctor.getEmail());

        if (doctorRepo.existsByEmail(doctor.getEmail()) || doctorRepo.existsByMobileNumber(doctor.getMobileNumber())) {
            throw new ApiException("Doctor already exists");
        }
        if (doctor.getPassword() == null) {
            throw new ApiException("Password must not be null");
        }
        if (!doctor.getPassword().equals(doctor.getConfirmPassword())) {
            throw new ApiException("Passwords do not match");
        }
        validatePassword(doctor.getPassword());

        doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
        doctor.setConfirmPassword(doctor.getPassword());
        return ResponseEntity.ok(doctorRepo.save(doctor));
    }

    public ResponseEntity<Object> updateDoctor(Doctor doctor, long mobileNumber) {
        Doctor existing = doctorRepo.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with mobile: " + mobileNumber));

        if (doctor.getMobileNumber() != 0) {
            validateMobile(doctor.getMobileNumber());

            if (!Objects.equals(existing.getMobileNumber(), doctor.getMobileNumber())
                    && doctorRepo.existsByMobileNumber(doctor.getMobileNumber())) {
                throw new ApiException("Mobile number already exists");
            }

            existing.setMobileNumber(doctor.getMobileNumber());
        }
        if (doctor.getDoctorName() != null) {
            existing.setDoctorName(doctor.getDoctorName());
        }
        if (doctor.getPassword() != null) {
            validatePassword(doctor.getPassword());
            existing.setPassword(passwordEncoder.encode(doctor.getPassword()));
            existing.setConfirmPassword(existing.getPassword());
        }

        if (doctor.getEmail() != null) {
            validateEmail(doctor.getEmail());
            if (!doctor.getEmail().equals(existing.getEmail()) && doctorRepo.existsByEmail(doctor.getEmail())) {
                throw new ApiException("Email already exists");
            }
            existing.setEmail(doctor.getEmail());
        }

        if (doctor.getGender() != null) {
            existing.setGender(doctor.getGender());
        }

        return ResponseEntity.ok(doctorRepo.save(existing));
    }

    public ResponseEntity<Object> deleteDoctor(long mobileNumber) {
        if (!doctorRepo.existsByMobileNumber(mobileNumber)) {
            throw new ResourceNotFoundException("Doctor not found with mobile: " + mobileNumber);
        }

        doctorRepo.deleteByMobileNumber(mobileNumber);
        return ResponseEntity.ok(mobileNumber);
    }

    public ResponseEntity<Object> getDoctorByMobileNumber(long mobileNumber) {

        return doctorRepo.findByMobileNumber(mobileNumber)
                .map(doctor -> ResponseEntity.ok((Object) doctor))
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with mobile: " + mobileNumber));
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
