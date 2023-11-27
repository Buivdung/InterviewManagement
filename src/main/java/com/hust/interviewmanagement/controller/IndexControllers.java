package com.hust.interviewmanagement.controller;

import com.hust.interviewmanagement.entities.Users;
import com.hust.interviewmanagement.repository.UserRepository;
import com.hust.interviewmanagement.service.UserService;
import com.hust.interviewmanagement.utils.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;


@Controller
@RequiredArgsConstructor
public class IndexControllers {

    private final UserRepository userRepository;

    @GetMapping({"/login","/",""})
    public String getLogin() {
        return "ui/login";
    }

    @PostMapping("/login")
    public String postLogin() {
        return "ui/login";
    }

    @PostMapping("/")
    public String getHome(HttpServletRequest req, RedirectAttributes ra) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users user = userRepository.findByAccount_Email(authentication.getName()).orElseThrow();
        String url = "";
        if(Objects.equals(user.getAccount().isCheckPassword(), true)){
            HttpSession httpSession = req.getSession();
            httpSession.setAttribute("user", user);
            url = "ui/index";
        }else {
            ra.addFlashAttribute("userEmail", user.getAccount().getEmail());
            url = "redirect:/user/changePassword";
        }
        return url;
    }



    @GetMapping("/forgot-password")
    public String getForgotPassword() {
        return "ui/forgot-password";
    }

    @GetMapping("/403")
    public String error() {
        return "ui/403";
    }
}
