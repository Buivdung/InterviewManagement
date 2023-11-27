package com.hust.interviewmanagement.web.resp;

import com.hust.interviewmanagement.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CandidateResp {
    private EStatus eStatus;
    private String notes;
    private List<String> interviews;
}
