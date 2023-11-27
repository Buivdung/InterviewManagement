package com.hust.interviewmanagement.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;

@Controller
public class SessionUtil {
    public void setTitle(HttpServletRequest req, String title) {
        HttpSession httpSession = req.getSession();
        httpSession.setAttribute("title", title);
    }
}
