package com.hust.interviewmanagement.web.request;

import com.hust.interviewmanagement.enums.EContractType;
import com.hust.interviewmanagement.enums.EPosition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OfferRequest {
    private Long id;
    private Long candidateId;
    private EPosition position;
    private Long approvedBy;
    private EContractType contractType;
    private LocalDate contractFrom;
    private LocalDate contractTo;
    private Long level;
    private Long department;
    private Long recruiterId;
    private LocalDate dueDate;
    private BigDecimal basicSalary;
    private String notes;
}
