package com.hust.interviewmanagement.enums;

public enum ERole {
    ROLE_RECRUITER("Recruiter"),
    ROLE_INTERVIEW("Interview"),
    ROLE_ADMIN("Admin"),
    ROLE_MANAGER("Manager");

    private final String value;

    ERole(String value) {
        this.value = value;
    }

    public String getRole(){
        return this.value;
    }
}
