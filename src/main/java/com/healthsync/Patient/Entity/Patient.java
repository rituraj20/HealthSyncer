
package com.healthsync.Patient.Entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.processing.Pattern;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    String name;
    @Column(unique = true,  nullable = false)

   private long mobileNumber;
    @Column(unique = true,  nullable = false)
   private String email;
    private  String address;
    private String dob;
    @Column(unique = true,  nullable = false)
    private String gender;
    private String password;
    @Column(unique = true,  nullable = false)
    private String confirmPassword;
}

