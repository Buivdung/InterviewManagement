package com.hust.interviewmanagement.web.request;


import com.hust.interviewmanagement.enums.EStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobRequest {

    private Long id;


    private String title;


    private LocalDate startDate;

    private LocalDate endDate;

    private BigDecimal salaryFrom;

    private BigDecimal salaryTo;

    private String workingAddress;

    private String description;


    private List<Long> skills;

    private List<Long> benefits;

    private Long level;

    private EStatus status;

}