package com.hust.interviewmanagement.web.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserChangePassword {

    private String newPassword;
    private String newPasswordRepeat;
    private String email;
}
