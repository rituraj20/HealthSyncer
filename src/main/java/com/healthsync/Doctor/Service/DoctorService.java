package com.healthsync.Doctor.Service;

import com.healthsync.Doctor.Entity.Doctor;
import com.healthsync.Doctor.Repository.DoctorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class DoctorService {
    @Autowired
   private DoctorRepo doctorRepo;
    public ResponseEntity<Object> createDoctor(Doctor doctor) {

        if (doctor.getMobileNumber() < 1000000000L || doctor.getMobileNumber() > 9999999999L) {
            return ResponseEntity.badRequest().body("Mobile number must be exactly 10 digits");
        }
        if (doctor.getEmail() == null || !doctor.getEmail().endsWith("@gmail.com")) {
            return ResponseEntity.badRequest().body("Email must end with @gmail.com");
        }
        if(doctorRepo.existsByEmail(doctor.getEmail()) || doctorRepo.existsByMobileNumber(doctor.getMobileNumber()) ){
            return ResponseEntity.badRequest().body("Patient already exists");
        }
        if(doctor.getPassword()==null){
            return ResponseEntity.badRequest().body("Password must not be null");
        }
        if(!doctor.getPassword().equals(doctor.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Passwords do not match");
        }
        String password = doctor.getPassword();

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
            doctorRepo.save(doctor);
            return ResponseEntity.ok(doctor);
        }
        catch(Exception e) {
            return ResponseEntity.badRequest().body("Unable to save the records");
        }

    }

    public ResponseEntity<Object> updateDoctor(Doctor doctor, long mobileNumber) {
        Optional<Doctor> op=doctorRepo.findByMobileNumber(mobileNumber);
        if(op.isEmpty()) return ResponseEntity.badRequest().build();
        try {
            Doctor s=op.get();
            if (doctor.getMobileNumber() != 0) {
                System.out.println("mobile Number"+ doctor.getMobileNumber());
                if (doctor.getMobileNumber() < 1000000000L || doctor.getMobileNumber() > 9999999999L) {
                    return ResponseEntity.badRequest().body("Mobile number must be exactly 10 digits");
                }

                if (!Objects.equals(s.getMobileNumber(), doctor.getMobileNumber())
                        && doctorRepo.existsByMobileNumber(doctor.getMobileNumber())) {
                    return ResponseEntity.badRequest().body("Mobile number already exists");
                }

                s.setMobileNumber(doctor.getMobileNumber());
            }
            if(doctor.getDoctorName()!=null){
                s.setDoctorName(doctor.getDoctorName());
            }
            if(doctor.getPassword()!=null){
                String password = doctor.getPassword();

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
                s.setPassword(doctor.getPassword());
            }




            if (doctor.getEmail() != null) {
                System.out.println("email "+ doctor.getEmail() );

                if (!doctor.getEmail().endsWith("@gmail.com")) {
                    return ResponseEntity.badRequest().body("Email must end with @gmail.com");
                }

                if (!doctor.getEmail().equals(s.getEmail())
                        || doctorRepo.existsByEmail(doctor.getEmail())) {
                    return ResponseEntity.badRequest().body("Email already exists");
                }

                s.setEmail(doctor.getEmail());
            }

            if(doctor.getGender()!=null){
                s.setGender(doctor.getGender());
            }

            doctorRepo.save(s);
            return ResponseEntity.ok(s);
        }
        catch(Exception e){
            return  ResponseEntity.badRequest().build();
        }
    }
    

    public ResponseEntity<Object> deleteDoctor(long mobileNumber) {
        if(!doctorRepo.existsByMobileNumber(mobileNumber)) {
            return ResponseEntity.badRequest().build();
            //false commit
        }
        try {
            doctorRepo.deleteByMobileNumber(mobileNumber);
            return ResponseEntity.ok(mobileNumber);
        }
        catch(Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<Object> getDoctorByMobileNumber(long mobileNumber) {

        return doctorRepo.findByMobileNumber(mobileNumber)
                .map(doctor -> ResponseEntity.ok((Object) doctor))
                .orElseThrow(() ->
                        new RuntimeException("User not found with mobile: " + mobileNumber));
    }


}
