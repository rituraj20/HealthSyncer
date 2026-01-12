package com.healthsync.Patient.Service;

import com.healthsync.Patient.Entity.Patient;
import com.healthsync.Patient.Repository.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
import java.util.Objects;
import java.util.Optional;

@Service
public class PatientService {
    @Autowired
    PatientRepo patientRepo;

    public ResponseEntity<Object> createPatient(Patient patient) {

        if (patient.getMobileNumber() < 1000000000L || patient.getMobileNumber() > 9999999999L) {
            return ResponseEntity.badRequest().body("Mobile number must be exactly 10 digits");
        }
        if (patient.getEmail() == null || !patient.getEmail().endsWith("@gmail.com")) {
            return ResponseEntity.badRequest().body("Email must end with @gmail.com");
        }
        if(patientRepo.existsByEmail(patient.getEmail()) || patientRepo.existsByMobileNumber(patient.getMobileNumber()) ){
            return ResponseEntity.badRequest().body("Patient already exists");
        }
        if(patient.getPassword()==null){
            return ResponseEntity.badRequest().body("Password must not be null");
        }
        if(!patient.getPassword().equals(patient.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Passwords do not match");
        }
        String password = patient.getPassword();

        String passwordRegex =
                "^(?=.*[a-z])" +        // at least one lowercase
                        "(?=.*[A-Z])" +         // at least one uppercase
                        "(?=.*\\d)" +           // at least one digit
                        "(?=.*[@$!%*?&])" +     // at least one special character
                        "[A-Za-z\\d@$!%*?&]{8,15}$";

        if (!password.matches(passwordRegex)) {
            return ResponseEntity.badRequest().body(
                    "Password must be 8-15 characters and include uppercase, lowercase, number, and special character"
            );
        }

        try {
            patientRepo.save(patient);
            return ResponseEntity.ok(patient);
        }
        catch(Exception e) {
            return ResponseEntity.badRequest().body("Unable to save the records");
        }

    }

    public ResponseEntity<Object> updatePatient(Patient patient, long mobileNumber) {
        Optional<Patient> op=patientRepo.findByMobileNumber(mobileNumber);
        if(op.isEmpty()) return ResponseEntity.badRequest().build();
        try {
            Patient s=op.get();
            if (patient.getMobileNumber() != 0) {
                System.out.println("mobile Number"+ patient.getMobileNumber());
                if (patient.getMobileNumber() < 1000000000L || patient.getMobileNumber() > 9999999999L) {
                    return ResponseEntity.badRequest().body("Mobile number must be exactly 10 digits");
                }

                if (!Objects.equals(s.getMobileNumber(), patient.getMobileNumber())
                        && patientRepo.existsByMobileNumber(patient.getMobileNumber())) {
                    return ResponseEntity.badRequest().body("Mobile number already exists");
                }

                s.setMobileNumber(patient.getMobileNumber());
            }
            if(patient.getName()!=null){
                s.setName(patient.getName());
            }
            if(patient.getPassword()!=null){
                String password = patient.getPassword();

                String passwordRegex =
                        "^(?=.*[a-z])" +        // at least one lowercase
                                "(?=.*[A-Z])" +         // at least one uppercase
                                "(?=.*\\d)" +           // at least one digit
                                "(?=.*[@$!%*?&])" +     // at least one special character
                                "[A-Za-z\\d@$!%*?&]{8,15}$";

                if (!password.matches(passwordRegex)) {
                    return ResponseEntity.badRequest().body(
                            "Password must be 8-15 characters and include uppercase, lowercase, number, and special character"
                    );
                }
                s.setPassword(patient.getPassword());
            }


            if(patient.getDob()!=null){
                s.setDob(patient.getDob());
            }

            if (patient.getEmail() != null) {
                System.out.println("email "+ patient.getEmail() );

                if (!patient.getEmail().endsWith("@gmail.com")) {
                    return ResponseEntity.badRequest().body("Email must end with @gmail.com");
                }

                if (!patient.getEmail().equals(s.getEmail())
                        || patientRepo.existsByEmail(patient.getEmail())) {
                    return ResponseEntity.badRequest().body("Email already exists");
                }

                s.setEmail(patient.getEmail());
            }

            if(patient.getGender()!=null){
                s.setGender(patient.getGender());
            }

            patientRepo.save(s);
            return ResponseEntity.ok(s);
        }
        catch(Exception e){
            return  ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<Object> deletePatient(long mobileNumber) {
        if(!patientRepo.existsByMobileNumber(mobileNumber)) {
            return ResponseEntity.badRequest().build();
        }
        try {
            patientRepo.deleteByMobileNumber(mobileNumber);
            return ResponseEntity.ok(mobileNumber);
        }
        catch(Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
