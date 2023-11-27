package com.hust.interviewmanagement.service.impl;

import com.hust.interviewmanagement.entities.Benefits;
import com.hust.interviewmanagement.repository.BenefitsRepository;
import com.hust.interviewmanagement.service.BenefitsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BenefitsServiceImpl implements BenefitsService {
    private final BenefitsRepository benefitsRepository;

    @Override
    public List<Benefits> findAllBenefits() {
        return benefitsRepository.findAll();
    }
}
