package com.healthsync.Patient.Service;

import com.healthsync.Patient.Entity.Patient;
import com.healthsync.Patient.Repository.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientService {
    @Autowired
    PatientRepo patientRepo;

    public ResponseEntity<Object> createPatient(Patient patient) {
        if(patientRepo.existsByEmail(patient.getEmail()) || patientRepo.existsByMobileNumber(patient.getMobileNumber()) ){
            return ResponseEntity.badRequest().body("Patient already exists");
        }

        try {
            patientRepo.save(patient);
            return ResponseEntity.ok(patient);
        }
        catch(Exception e) {
            return ResponseEntity.badRequest().body("Unable to save the records");
        }

    }

    public ResponseEntity<Object> updatePatient(Patient patient, int mobileNumber) {
        Optional<Patient> op=patientRepo.findByMobileNumber(mobileNumber);
        if(op.isEmpty()) return ResponseEntity.badRequest().build();
        try {
            Patient s=op.get();
            s.setMobileNumber(patient.getMobileNumber());
            s.setName(patient.getName());
            patientRepo.save(s);
            return ResponseEntity.ok(s);
        }
        catch(Exception e){
            return  ResponseEntity.badRequest().build();
        }
    }
}
