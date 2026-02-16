package com.healthsync.Patient.Security;

import com.healthsync.Doctor.Repository.DoctorRepo;
import com.healthsync.Patient.Repository.PatientRepo;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationUserDetailsService implements UserDetailsService {

    private final PatientRepo patientRepo;
    private final DoctorRepo doctorRepo;

    public ApplicationUserDetailsService(PatientRepo patientRepo, DoctorRepo doctorRepo) {
        this.patientRepo = patientRepo;
        this.doctorRepo = doctorRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return patientRepo.findByEmail(username)
                .<UserDetails>map(patient -> User.builder()
                        .username(patient.getEmail())
                        .password(patient.getPassword())
                        .authorities(List.of(new SimpleGrantedAuthority("ROLE_PATIENT")))
                        .build())
                .or(() -> doctorRepo.findByEmail(username)
                        .map(doctor -> User.builder()
                                .username(doctor.getEmail())
                                .password(doctor.getPassword())
                                .authorities(List.of(new SimpleGrantedAuthority("ROLE_DOCTOR")))
                                .build()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
