package com.hust.interviewmanagement.service;

import com.hust.interviewmanagement.entities.Users;
import com.hust.interviewmanagement.web.request.SearchRequest;
import com.hust.interviewmanagement.web.request.UserChangePassword;
import com.hust.interviewmanagement.web.request.UserRequest;
import com.hust.interviewmanagement.web.result.UserSave;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

public interface UserService {
    List<Users> findUserByRoleInterviewAndRecruiter();
    List<Users> findUserByRoleManager();
    List<Users> findUserByRoleInterview();
    List<Users> findUserByRoleRecruiter();
    List<Users> findUserByRoleRecruiterAndManager();
    UserSave saveUser(UserRequest userRequest) throws MessagingException;
    UserSave updateUser( UserRequest userRequest);
    List<Users> saveAllUser(MultipartFile file) throws MessagingException;
    Page<Users> findAllUser(SearchRequest searchRequest);
    Users findUserById(Long id);
    void deleteUserById(Long id);
    Users findUserByAccountId(String email);
    Users changePassword(UserChangePassword userChangePassword);

}
