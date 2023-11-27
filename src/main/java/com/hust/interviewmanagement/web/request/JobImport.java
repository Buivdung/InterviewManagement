package com.hust.interviewmanagement.web.request;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class JobImport {
    @SerializedName("Job Title")
    private String title;

    @SerializedName("Start Date")
    private LocalDate startDate;

    @SerializedName("End Date")
    private LocalDate endDate;

    @SerializedName("Salary From")
    private BigDecimal salaryFrom;

    @SerializedName("Salary To")
    private BigDecimal salaryTo;

    @SerializedName("Working Address")
    private String workingAddress;

    @SerializedName("Description")
    private String description;

    @SerializedName("Skills")
    private String skills;

    @SerializedName("Benefits")
    private String benefits;

    @SerializedName("Level")
    private String level;
}
