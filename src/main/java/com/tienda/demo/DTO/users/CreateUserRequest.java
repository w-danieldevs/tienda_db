package com.tienda.demo.DTO.users;

import com.tienda.demo.enums.RoleName;
import lombok.Data;

@Data 
public class CreateUserRequest {

    private Long id;
    private String username;
    private String email;
    private String password;
    private RoleName role;

}
