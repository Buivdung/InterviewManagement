package com.hust.interviewmanagement.web.request;

import com.google.gson.annotations.SerializedName;
import com.hust.interviewmanagement.enums.EGender;
import com.hust.interviewmanagement.enums.ERole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserImport {
    @SerializedName("Full name")
    private String fullName;
    @SerializedName("Email")
    private String email;
    @SerializedName("Date of birth")
    private String dob;
    @SerializedName("Phone no.")
    private String phoneNumber;
    @SerializedName("Address")
    private String address;
    @SerializedName("Role")
    private ERole role;
    @SerializedName("Gender")
    private EGender gender;
    @SerializedName("Department")
    private String department;
    @SerializedName("Notes")
    private String notes;
}
