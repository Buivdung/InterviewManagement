package com.hust.interviewmanagement.entities;

import com.hust.interviewmanagement.enums.ERole;
import com.hust.interviewmanagement.enums.EStatus;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;
    private String email;
    private String password;

    private boolean checkPassword;
    private Boolean status;

    @Enumerated(EnumType.STRING)
    private ERole role;

    @OneToOne
    @ToString.Exclude
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", unique = true)
    private Users user;


}
