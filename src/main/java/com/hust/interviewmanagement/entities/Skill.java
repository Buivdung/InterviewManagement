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
public class Skill  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skill_id")
    private Long id;

    @Column(name = "skill_name")
    private String name;

    @OneToMany(mappedBy = "skill", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<SkillCandidate> skillCandidates;


    @OneToMany(mappedBy = "skill", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<SkillJob> skillJobs;


}
