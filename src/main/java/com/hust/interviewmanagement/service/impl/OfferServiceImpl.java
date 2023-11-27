package com.hust.interviewmanagement.service.impl;

import com.hust.interviewmanagement.entities.*;
import com.hust.interviewmanagement.enums.EContractType;
import com.hust.interviewmanagement.enums.EStatus;
import com.hust.interviewmanagement.repository.*;
import com.hust.interviewmanagement.service.OfferService;
import com.hust.interviewmanagement.utils.MessageList;
import com.hust.interviewmanagement.web.request.OfferRequest;
import com.hust.interviewmanagement.web.request.SearchRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {
    private final OfferRepository offerRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ResultInterviewRepository resultInterviewRepository;
    private final DepartmentRepository departmentRepository;
    private final LevelRepository levelRepository;
    private final CandidateRepository candidateRepository;

    @Override
    public Page<Offer> findAllOffer(SearchRequest searchRequest) {
        Pageable pageable = PageRequest.of(searchRequest.getPageNumber() - 1, SearchRequest.PAGE_SIZE);
        if (searchRequest.getStatus() == null) {
            return offerRepository.findAll(
                    searchRequest.getParam(),
                    searchRequest.getDepartment(),
                    pageable
            );
        }
        return offerRepository.findAll(
                searchRequest.getParam(),
                searchRequest.getDepartment(),
                searchRequest.getStatus(),
                pageable
        );
    }

    @Override
    public void deleteOffer(Long id) {
        offerRepository.deleteById(id);
    }

    @Override
    public List<Offer> findAllOfferByDate(LocalDate fromDate, LocalDate toDate) {
        return offerRepository.findOfferByDate(fromDate, toDate);
    }

    @Override
    public Offer saveOffer(OfferRequest offerRequest) {
        Offer offer = offerRepository.save(getOffer(new Offer(), offerRequest));
        Candidate candidate = offer.getResultInterview().getCandidate();
        candidate.setStatus(EStatus.WAITING_FOR_RESPONSE);
        candidateRepository.save(candidate);
        return offer;
    }

    @Override
    public Offer findOfferById(Long id) {
        return offerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(MessageList.MessageOffer.DONT_OFFER));
    }

    @Override
    public void approveOffer(Long id, String notes) {
        Offer offer = findOfferById(id);
        offer.setStatus(EStatus.APPROVED_OFFER);
        setNotes(notes, offer);
        offerRepository.save(offer);
        Long candidateId = offer.getResultInterview().getCandidate().getId();
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new IllegalArgumentException(MessageList.MessageCandidate.DONT_CANDIDATE));
        candidate.setStatus(EStatus.ACCEPTED_OFFER);
        candidateRepository.save(candidate);
    }

    @Override
    public void rejectOffer(Long id, String notes) {
        Offer offer = findOfferById(id);
        offer.setStatus(EStatus.REJECTED_OFFER);
        setNotes(notes, offer);
        offerRepository.save(offer);
        Long candidateId = offer.getResultInterview().getCandidate().getId();
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new IllegalArgumentException(MessageList.MessageCandidate.DONT_CANDIDATE));
        candidate.setStatus(EStatus.DECLINED_OFFER);
        candidateRepository.save(candidate);
    }

    @Override
    public Offer updateOffer(OfferRequest offerRequest) {
        Offer offer = findOfferById(offerRequest.getId());
        return offerRepository.save(getOffer(offer, offerRequest));
    }

    private void setNotes(String notes, Offer offer) {
        if (notes != null && !notes.isEmpty()) {
            offer.setNotes(notes);
        }
    }

    private Offer getOffer(Offer offer, OfferRequest offerRequest) {
        modelMapper.map(offerRequest, offer);
        ResultInterview resultInterview = resultInterviewRepository.findByCandidate_Id(offerRequest.getCandidateId()).orElseThrow();
        Users recruiter = userRepository.findById(offerRequest.getRecruiterId())
                .orElseThrow(() -> new IllegalArgumentException(MessageList.MessageUser.DONT_USER));
        Users manager = userRepository.findById(offerRequest.getApprovedBy())
                .orElseThrow(() -> new IllegalArgumentException(MessageList.MessageUser.DONT_USER));
        Department department = departmentRepository.findById(offerRequest.getDepartment()).orElseThrow();
        Level level = levelRepository.findById(offerRequest.getLevel()).orElseThrow();
        setContractTo(offer, offerRequest);
        offer.setLevel(level);
        offer.setDepartment(department);
        if (offer.getStatus() == null) {
            offer.setStatus(EStatus.WAITING_FOR_APPROVAL);
        }
        offer.setRecruiter(recruiter);
        offer.setManager(manager);
        offer.setResultInterview(resultInterview);
        return offer;
    }

    private void setContractTo(Offer offer, OfferRequest offerRequest) {
        LocalDate contractTo;
        if (EContractType.TRAIL.equals(offerRequest.getContractType())) {

            contractTo = offerRequest.getContractFrom().plusMonths(2);

        } else if (EContractType.TRAINING.equals(offerRequest.getContractType())) {

            contractTo = offerRequest.getContractFrom().plusMonths(3);

        } else if (EContractType.ONE_YEAR.equals(offerRequest.getContractType())) {
            contractTo = offerRequest.getContractFrom().plusYears(1);
        } else if (EContractType.THREE_YEAR.equals(offerRequest.getContractType())) {
            contractTo = offerRequest.getContractFrom().plusYears(3);
        } else {
            contractTo = offerRequest.getContractFrom().plusYears(10);
        }
        offer.setContractTo(contractTo);
    }
}
