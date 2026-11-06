package com.tienda.demo.DTO.auth;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Data;

@Data
public class LoginRequest {

    @JsonAlias("username")
    private String user;
    private String password;

}