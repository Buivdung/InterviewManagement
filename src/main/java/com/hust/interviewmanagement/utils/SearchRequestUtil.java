package com.hust.interviewmanagement.utils;

import com.hust.interviewmanagement.web.request.SearchRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SearchRequestUtil {
    public SearchRequest getSearchRequest(SearchRequest searchRequest){
        if (searchRequest.getPageNumber() == null) {
            searchRequest.setPageNumber(1);
        }
        if (searchRequest.getParam() == null || searchRequest.getParam().isEmpty()) {
            searchRequest.setParam("");
        }
        if(searchRequest.getInterviewer() == null || searchRequest.getInterviewer().isEmpty()){
            searchRequest.setInterviewer("");
        }
        if(searchRequest.getDepartment() == null || searchRequest.getDepartment().isEmpty()){
            searchRequest.setDepartment("");
        }
        return searchRequest;
    }
    public SearchRequest setPageMax(int pageMax, SearchRequest searchRequest){
        if (pageMax > 0) {
            List<Integer> integers = new ArrayList<>();
            for (int i = 0; i < pageMax; i++) {
                integers.add(i);
            }
            searchRequest.setPageMaxNumber(integers);
        }
        return searchRequest;
    }
}
