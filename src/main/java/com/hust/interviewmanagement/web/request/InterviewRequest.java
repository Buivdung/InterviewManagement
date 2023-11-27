package com.hust.interviewmanagement.web.request;

import com.hust.interviewmanagement.enums.EResult;
import com.hust.interviewmanagement.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InterviewRequest {
    private Long id;
    private String title;
    private Long candidateId;
    private LocalDateTime schedule;
    private List<Long> interviewId;
    private boolean location;
    private Long recruiterId;
    private String meeting;
    private String note;
    private EStatus status;
    private EResult result;
}
