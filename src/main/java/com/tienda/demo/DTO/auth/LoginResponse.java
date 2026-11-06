package com.tienda.demo.DTO.auth;

import com.tienda.demo.DTO.users.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {

    private String jwt;
    private UserResponse user;

}