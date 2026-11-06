package com.tienda.demo.DTO.users;

import lombok.Data;

@Data
public class UserResponse {

    private Long id;
    private String username;
    private String email;
    private Boolean active;
    private Long rolId;
    private String roleName;

}
