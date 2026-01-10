
package com.healthsync.healthsync.Entity;
import jakarta.persistence.*;
import lombok.*;
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
    StringBuffer name;
    long mobileNumber;
    StringBuffer email;
    StringBuffer address;
    StringBuffer dob;
    StringBuffer gender;

}
