package com.hust.interviewmanagement.web.request;

import com.hust.interviewmanagement.enums.EGender;
import com.hust.interviewmanagement.enums.EHighestLevel;
import com.hust.interviewmanagement.enums.EPosition;
import com.hust.interviewmanagement.enums.EStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CandidateRequest {
    private Long id;
    @NotEmpty
    private String fullName;
    @NotEmpty
    @Email
    private String email;
    @Past
    private LocalDate dob;
    private String address;
    @NotEmpty
    private String phoneNumber;
    @NotNull
    private EGender gender;
    private MultipartFile cv;
    @NotNull
    private EPosition position;
    private List<Long> skills;
    @Positive
    private Long recruiterId;
    @NotNull
    private EStatus status;
    @PositiveOrZero
    private Long yearOfExperience;
    @NotNull
    private EHighestLevel highestLevel;
    private String note;
}