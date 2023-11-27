package com.hust.interviewmanagement.controller;

import com.hust.interviewmanagement.entities.Department;
import com.hust.interviewmanagement.entities.Users;
import com.hust.interviewmanagement.service.DepartmentService;
import com.hust.interviewmanagement.service.UserService;
import com.hust.interviewmanagement.utils.MessageList;
import com.hust.interviewmanagement.utils.SearchRequestUtil;
import com.hust.interviewmanagement.utils.SessionUtil;
import com.hust.interviewmanagement.web.request.SearchRequest;
import com.hust.interviewmanagement.web.request.UserChangePassword;
import com.hust.interviewmanagement.web.request.UserRequest;
import com.hust.interviewmanagement.web.result.UserSave;
import jakarta.mail.MessagingException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
@MultipartConfig
public class UserController {
    private final SessionUtil sessionUtil;
    private final DepartmentService departmentService;
    private final UserService userService;
    private final SearchRequestUtil searchRequestUtil;

    @GetMapping({"", "/"})
    public String user(HttpServletRequest req,
                       Model model,
                       @ModelAttribute SearchRequest searchRequest) {
        sessionUtil.setTitle(req, "USER MANAGEMENT");
        Page<Users> users = userService.findAllUser(searchRequestUtil.getSearchRequest(searchRequest));
        SearchRequest searchResponse = searchRequestUtil.setPageMax(users.getTotalPages(), searchRequest);
        model.addAttribute(MessageList.Common.USERS, users);
        model.addAttribute(MessageList.Common.SEARCH_RESPONSE, searchResponse);
        return "ui/user/list";
    }

    @GetMapping("/create")
    public String getUserCreate(@ModelAttribute UserRequest userRequest, Model model) {
        List<Department> departments = departmentService.findAllDepartment();
        model.addAttribute(MessageList.Common.DEPARTMENTS, departments);
        return "ui/user/add";
    }

    @PostMapping("/create")
    public String postUserCreate(@ModelAttribute UserRequest userRequest,
                                 RedirectAttributes ra) throws MessagingException {
        UserSave userSave = userService.saveUser(userRequest);
        if (userSave.getUser() == null) {
            ra.addFlashAttribute(MessageList.Common.ALERT, MessageList.MessageUser.CREATE_USER_FAIL);
            ra.addFlashAttribute(MessageList.Common.MESSAGE, userSave.getMessage());
            ra.addFlashAttribute("UserRequest", userRequest);
        } else {
            ra.addFlashAttribute(MessageList.Common.ALERT, MessageList.MessageUser.CREATE_USER_SUCCESS);
        }
        return "redirect:/user/create";
    }

    @PostMapping("/import")
    public String importUserCreate(@ModelAttribute MultipartFile fileImport,
                                   RedirectAttributes ra) throws MessagingException {
        if (userService.saveAllUser(fileImport).isEmpty()) {
            ra.addFlashAttribute(MessageList.Common.ALERT, MessageList.MessageUser.CREATE_USER_FAIL);
            ra.addFlashAttribute(MessageList.Common.MESSAGE, MessageList.MessageUser.EMAIL);
        } else {
            ra.addFlashAttribute(MessageList.Common.ALERT, MessageList.MessageUser.CREATE_USER_SUCCESS);
        }
        return "redirect:/user";
    }

    @GetMapping("/{id}")
    public String userDetail(@PathVariable Long id, Model model) {
        Users user = userService.findUserById(id);
        model.addAttribute(MessageList.Common.USER, user);
        return "ui/user/detail";
    }

    @GetMapping("/edit/{id}")
    public String getUserEdit(@PathVariable Long id, Model model) {
        Users user = userService.findUserById(id);
        List<Department> departments = departmentService.findAllDepartment();
        model.addAttribute(MessageList.Common.DEPARTMENTS, departments);
        model.addAttribute(MessageList.Common.USER, user);
        return "ui/user/edit";
    }

    @PostMapping("/edit/{id}")
    public String postUserEdit(@PathVariable Long id,
                               @ModelAttribute UserRequest userRequest,
                               Model model) {
        if (Objects.equals(id, userRequest.getId())) {
            UserSave userSave = userService.updateUser(userRequest);
            if (userSave.getUser() == null) {
                model.addAttribute(MessageList.Common.ALERT, MessageList.MessageUser.EDIT_USER_FAIL);
                model.addAttribute(MessageList.Common.MESSAGE, userSave.getMessage());
            } else {
                model.addAttribute(MessageList.Common.ALERT, MessageList.MessageUser.EDIT_USER_SUCCESS);
            }
        } else {
            model.addAttribute(MessageList.Common.ALERT, MessageList.MessageUser.EDIT_USER_FAIL);
        }
        return getUserEdit(id, model);
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id,
                             RedirectAttributes ra) {
        userService.deleteUserById(id);
        ra.addFlashAttribute(MessageList.Common.ALERT, MessageList.MessageUser.DELETE_USER_SUCCESS);
        return "redirect:/user";
    }

    @GetMapping("/changePassword")
    public String changePassword(@ModelAttribute UserChangePassword userChangePassword, Model model) {
        model.addAttribute("user", userChangePassword);
        return "ui/user/change-password";
    }

    @PostMapping("/changePassword")
    public String changePasswordForm(@ModelAttribute UserChangePassword userChangePassword,
                                     HttpServletRequest req,
                                     Model model) {
        if (!Objects.equals(userChangePassword.getNewPassword(), userChangePassword.getNewPasswordRepeat())) {
            model.addAttribute(MessageList.Common.MESSAGE, "The entered passwords are not the same !!!");
            model.addAttribute(MessageList.Common.USER, userChangePassword);
            return "ui/user/change-password";
        } else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Users user = userService.findUserByAccountId(authentication.getName());
            HttpSession httpSession = req.getSession();
            httpSession.setAttribute(MessageList.Common.USER, user);
            userService.changePassword(userChangePassword);
            model.addAttribute(MessageList.Common.ALERT, "Password changed successfully !!!");
            return "layout/navigation";
        }
    }

}
