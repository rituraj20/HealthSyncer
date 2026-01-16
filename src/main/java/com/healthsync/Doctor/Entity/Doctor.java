
package com.healthsync.Doctor.Entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    String doctorName;
    @Column(unique = true,  nullable = false)
   private long mobileNumber;
    @Column(unique = true,  nullable = false)
   private String email;
    private  String address;
    @Column( nullable = false)
    private String specialization;
    @Column( nullable = false)
    private int yearOfExperience ;
    @Column(  nullable = false)
    private String gender;
    @Column( nullable = false)
    private String password;
    @Column( nullable = false)
    private String confirmPassword;
}

