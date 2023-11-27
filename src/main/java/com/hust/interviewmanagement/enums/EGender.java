package com.hust.interviewmanagement.enums;

public enum EGender {
    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other");

    private final String value;

    EGender(String value) {
        this.value = value;
    }

    public String getGender() {
        return value;
    }
}
