package com.hust.interviewmanagement.controllerAdvice;

import com.hust.interviewmanagement.utils.MessageList;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;


public class CustomAuthenticationFailureHandler
        implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception)
            throws IOException, ServletException {
        if (exception.getMessage().equals("User is disabled")) {
            request.setAttribute(MessageList.Common.MESSAGE, MessageList.Common.LOGIN_NO_ACTIVATED);
        } else if (exception.getMessage().equals("Bad credentials")) {
            request.setAttribute(MessageList.Common.MESSAGE, MessageList.Common.LOGIN_FAIL);
        } else {
            request.setAttribute(MessageList.Common.MESSAGE, exception.getMessage());
        }
        request.getRequestDispatcher("/login").forward(request, response);
    }


}