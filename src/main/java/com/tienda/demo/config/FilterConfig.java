package com.tienda.demo.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tienda.demo.filter.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilterRegistration() {

        FilterRegistrationBean<JwtAuthenticationFilter> registration =
                new FilterRegistrationBean<>();

        registration.setFilter(jwtAuthenticationFilter);

        registration.addUrlPatterns("/*");

        registration.setOrder(1);

        registration.setName("jwtAuthenticationFilter");

        return registration;
    }
}
