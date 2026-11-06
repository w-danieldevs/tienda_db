package com.tienda.demo.DTO.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data 
@AllArgsConstructor 
public class RefreshTokenResponse {

    private String accessToken;
    private String tokenType = "Bearer";

}
