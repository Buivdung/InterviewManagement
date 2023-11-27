package com.hust.interviewmanagement.config;

import com.hust.interviewmanagement.entities.Candidate;
import com.hust.interviewmanagement.entities.Offer;
import com.hust.interviewmanagement.web.request.CandidateRequest;
import com.hust.interviewmanagement.web.request.OfferRequest;
import org.modelmapper.ExpressionMap;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class CommonBean {

    private static final ExpressionMap<CandidateRequest, Candidate> MAPPER_CANDIDATE = mapper -> mapper.skip(Candidate::setCv);
    private static final ExpressionMap<OfferRequest, Offer> MAPPER_OFFER = mapper -> mapper.skip(Offer::setStatus);

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.createTypeMap(CandidateRequest.class, Candidate.class).addMappings(MAPPER_CANDIDATE);
        modelMapper.createTypeMap(OfferRequest.class, Offer.class).addMappings(MAPPER_OFFER);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
        return modelMapper;
    }

    @Bean("taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(20);
        executor.setThreadNamePrefix("UserThread -");
        executor.initialize();
        return executor;
    }

}
