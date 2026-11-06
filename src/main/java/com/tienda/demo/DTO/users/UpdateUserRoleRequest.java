package com.tienda.demo.DTO.users;

import com.tienda.demo.enums.RoleName;

import lombok.Data;

@Data 
public class UpdateUserRoleRequest {

    private RoleName role;

}
