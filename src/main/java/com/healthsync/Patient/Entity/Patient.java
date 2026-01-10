
package com.healthsync.Patient.Entity;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Builder
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    String name;
    @Column(unique = true,  nullable = false)
    long mobileNumber;
    @Column(unique = true,  nullable = false)
    String email;
    String address;
    String dob;
    String gender;

}
