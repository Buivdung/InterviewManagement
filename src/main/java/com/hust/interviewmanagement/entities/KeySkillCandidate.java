package com.hust.interviewmanagement.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class KeySkillCandidate implements Serializable {
    private Candidate candidate;
    private Skill skill;
}
