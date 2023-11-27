package com.hust.interviewmanagement.web.request;

import com.hust.interviewmanagement.enums.EResult;
import lombok.Data;

@Data
public class ResultRequest {
    private Long id;
    private EResult result;
    private String note;
}
