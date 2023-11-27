package com.hust.interviewmanagement.enums;

import lombok.Getter;

@Getter
public enum EResult {
    NA("N/a"),OPEN("Open"),PASS("Pass"),FAIL("Fail"),CANCEL("Cancel");
    private final String value;

    EResult(String value) {
        this.value = value;
    }

}
