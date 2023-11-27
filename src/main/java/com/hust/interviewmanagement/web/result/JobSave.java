package com.hust.interviewmanagement.web.result;

import com.hust.interviewmanagement.entities.Job;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JobSave {
    private Job job;
    private String message;
}
