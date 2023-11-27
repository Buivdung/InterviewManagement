package com.hust.interviewmanagement.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@IdClass(KeyBenefitsJob.class)
public class BenefitsJob implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "benefits_id")
    private Benefits benefits;

    @Id
    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;
}
