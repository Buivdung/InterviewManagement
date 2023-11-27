package com.hust.interviewmanagement.web.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OfferExport {
    private String candidateName;
    private String email;
    private String approved;
    private String interviewNotes;
    private String department;
    private String status;
    private String contractType;
    private String fromDate;
    private String toDate;
    private String level;
    private String dueDate;
    private String basicSalary;
    private String notes;
    private String createDate;
    private String updateDate;
}
