package com.hust.interviewmanagement.config;

import com.hust.interviewmanagement.controllerAdvice.CustomAuthenticationFailureHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        a -> a
                                .requestMatchers("/offer/**","/offer").hasAnyRole("MANAGER","RECRUITER")
                                .requestMatchers("/candidate/**","/candidate").authenticated()
                                .requestMatchers("/job/**","/job").authenticated()
                                .requestMatchers("/interview/**","/interview").authenticated()
                                .requestMatchers("/user/**","/user").authenticated()
                                .requestMatchers("/home").authenticated()
                                .anyRequest().permitAll())
                .formLogin(login -> login
                        .loginPage("/login")
                        .loginProcessingUrl("/signIn")
                        .successForwardUrl("/")
                        .failureHandler(authenticationFailureHandler())
                        .permitAll()
                )
                .logout(logout -> logout.logoutSuccessUrl("/login").permitAll())
                .exceptionHandling(err -> err.accessDeniedPage("/403"));
//        config tam thời
        return http.build();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }
}
