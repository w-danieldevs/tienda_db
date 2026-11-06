package com.tienda.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tienda.demo.DTO.HttpGlobalResponseDTO;
import com.tienda.demo.DTO.auth.LoginRequest;
import com.tienda.demo.DTO.auth.LoginResponse;
import com.tienda.demo.DTO.auth.RefreshTokenResponse;
import com.tienda.demo.DTO.auth.RegisterRequest;
import com.tienda.demo.DTO.users.UserResponse;
import com.tienda.demo.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // Registrar usuario
    @PostMapping("/register")
    public ResponseEntity<HttpGlobalResponseDTO<UserResponse>> register(
            @RequestBody RegisterRequest request) {

        UserResponse response = authService.registrar(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new HttpGlobalResponseDTO<UserResponse>(
                        "Usuario registrado correctamente", response));
    }

    // Iniciar sesión
    @PostMapping("/login")
    public ResponseEntity<HttpGlobalResponseDTO<LoginResponse>> login(
            @RequestBody LoginRequest request) {

        LoginResponse response = authService.login(request);

        return ResponseEntity.ok(
                new HttpGlobalResponseDTO<LoginResponse>(
                        "Inicio de sesión exitoso", response));
    }

    // Renovar token
    @PostMapping("/refreshtoken")
    public ResponseEntity<HttpGlobalResponseDTO<RefreshTokenResponse>> refreshToken(
            HttpServletRequest request) {

        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new RuntimeException("Token no proporcionado");
        }

        String token = authorization.substring(7);

        RefreshTokenResponse response =
                authService.refrescarToken(token);

        return ResponseEntity.ok(
                new HttpGlobalResponseDTO<RefreshTokenResponse>(
                        "Token renovado correctamente", response));
    }
}
