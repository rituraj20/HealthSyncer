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
        if(patientRepo.existsById(patient.getId())){
            return ResponseEntity.badRequest().body("Student with same id already exists");
        }
        try {
            patientRepo.save(patient);
            return ResponseEntity.ok(patient);
        }
        catch(Exception e) {
            return ResponseEntity.badRequest().body("Unable to save the records");
        }

    }

    public ResponseEntity<Object> updatePatient(Patient patient, int id) {
        Optional<Patient> op=patientRepo.findById(id);
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
