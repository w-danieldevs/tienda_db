package com.tienda.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tienda.demo.DTO.HttpGlobalResponseDTO;
import com.tienda.demo.DTO.users.CreateUserRequest;
import com.tienda.demo.DTO.users.UpdateUserRoleRequest;
import com.tienda.demo.DTO.users.UpdateUserStatusRequest;
import com.tienda.demo.service.UsersService;
import com.tienda.demo.DTO.users.UserResponse;
import com.tienda.demo.annotations.RequiresRole;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import java.util.List;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    // Consultar perfil del usuario que inició sesión.
    @GetMapping("/me")
    public ResponseEntity<HttpGlobalResponseDTO<UserResponse>> getMyProfile(
            HttpServletRequest request) {

        Long userId = Long.valueOf(
                request.getAttribute("userId").toString());

        UserResponse response = usersService.buscarUsuario(userId);

        return ResponseEntity.ok(
                new HttpGlobalResponseDTO<UserResponse>(
                        "Perfil consultado correctamente", response));
    }

    // Listar todos los usuarios. Solo ADMIN.
    @GetMapping 
    @RequiresRole("ADMIN")
    public ResponseEntity<HttpGlobalResponseDTO<List<UserResponse>>> getAllUsers() {

        List<UserResponse> response = usersService.listarUsuarios();

        return ResponseEntity.ok(
                new HttpGlobalResponseDTO<List<UserResponse>>(
                        "Usuarios consultados correctamente", response));
    }

    // Crear usuarios con cualquier rol. Solo ADMIN.
    @PostMapping
    @RequiresRole("ADMIN")
    public ResponseEntity<HttpGlobalResponseDTO<UserResponse>> createUser(
            @RequestBody CreateUserRequest request) {

        UserResponse response = usersService.crearUsuario(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new HttpGlobalResponseDTO<UserResponse>(
                        "Usuario creado correctamente", response));
    }

    // Cambiar el rol de un usuario.
    @PatchMapping("/{id}/role")
    @RequiresRole("ADMIN")
    public ResponseEntity<HttpGlobalResponseDTO<UserResponse>> updateRole(
            @PathVariable Long id,
            @RequestBody UpdateUserRoleRequest request) {

        UserResponse response = usersService.cambiarRol(id, request);

        return ResponseEntity.ok(
                new HttpGlobalResponseDTO<UserResponse>(
                        "Rol actualizado correctamente", response));
    }

    // Activar o desactivar un usuario.
    @PatchMapping("/{id}/status")
    @RequiresRole("ADMIN")
    public ResponseEntity<HttpGlobalResponseDTO<UserResponse>> updateStatus(
            @PathVariable Long id,
            @RequestBody UpdateUserStatusRequest request) {

        UserResponse response = usersService.cambiarEstado(id, request);

        return ResponseEntity.ok(
                new HttpGlobalResponseDTO<UserResponse>(
                        "Estado actualizado correctamente", response));
    }
}