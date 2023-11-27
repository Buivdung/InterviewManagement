package com.hust.interviewmanagement.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Benefits implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "benefits_id")
    private Long id;

    @Column(name = "benefits_name")
    private String name;

    @OneToMany(mappedBy = "benefits", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<BenefitsJob> benefitsJobs;

}
