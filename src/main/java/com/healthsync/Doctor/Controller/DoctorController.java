package com.healthsync.Doctor.Controller;
import com.healthsync.Doctor.Entity.Doctor;
import com.healthsync.Doctor.Service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/doctors")
@RestController
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }
    @PostMapping
    public ResponseEntity<Object> createdoctor(@RequestBody Doctor doctor) {
        return doctorService.createDoctor(doctor);
    }
    @PutMapping("/update/{mobileNumber}")
    public ResponseEntity<Object> updatePatient(@RequestBody Doctor doctor, @PathVariable long mobileNumber) {
        return doctorService.updateDoctor(doctor, mobileNumber);
    }
    @DeleteMapping("/delete/{mobileNumber}")
    public ResponseEntity<Object> deleteStudent(@PathVariable long mobileNumber) {
        return doctorService.deleteDoctor(mobileNumber);
    }

    @GetMapping("/getDoctor/{mobileNumber}")
    public ResponseEntity<Object> getPatientByMobileNumber(@PathVariable long mobileNumber) {
        return doctorService.getDoctorByMobileNumber(mobileNumber);
    }
}
