package com.hust.interviewmanagement.enums;

import lombok.Getter;

@Getter
public enum EHighestLevel {

    HIGH_SCHOOL(" High school"),

    BACHELOR_S_DEGREE("Bachelor's Degree"),

    MASTER_DEGREE_PhD("Master degree, PhD ");

    private final String value;

    EHighestLevel(String value) {
        this.value = value;
    }

}
