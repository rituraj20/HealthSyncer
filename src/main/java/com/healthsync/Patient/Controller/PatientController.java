package com.healthsync.Patient.Controller;
import com.healthsync.Patient.Entity.Patient;
import com.healthsync.Patient.Service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/patients")
@RestController
public class PatientController {

    @Autowired
    public PatientService patientService;
    @PostMapping
    public ResponseEntity<Object> createPatient(@RequestBody  Patient patient) {
        return patientService.createPatient(patient);
    }
    @PutMapping("/update/{mobileNumber}")
    public ResponseEntity<Object> updatePatient(@RequestBody  Patient patient, @PathVariable long mobileNumber) {
        return patientService.updatePatient(patient, mobileNumber);
    }
    @DeleteMapping("{mobileNumber}")
    public ResponseEntity<Object> deleteStudent(@PathVariable long mobileNumber) {
        return patientService.deletePatient(mobileNumber);
    }

    @GetMapping("/getPatient/{mobileNumber}")
    public ResponseEntity<Object> getPatientByMobileNumber( long mobileNumber) {
        return patientService.getPatientByMobileNumber(mobileNumber);
    }
}
