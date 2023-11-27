package com.hust.interviewmanagement.web.result;

import com.hust.interviewmanagement.entities.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSave {
    private Users user;
    private String message;
}
