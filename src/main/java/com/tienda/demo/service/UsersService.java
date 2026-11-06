package com.tienda.demo.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tienda.demo.DTO.users.CreateUserRequest;
import com.tienda.demo.DTO.users.UpdateUserRoleRequest;
import com.tienda.demo.DTO.users.UpdateUserStatusRequest;
import com.tienda.demo.DTO.users.UserResponse;
import com.tienda.demo.entity.Roles;
import com.tienda.demo.entity.Users;
import com.tienda.demo.repository.RolesRepository;
import com.tienda.demo.repository.UsersRepository;
import org.springframework.http.HttpStatus;
import com.tienda.demo.exception.ApiException;
import lombok.AllArgsConstructor;

import java.util.List;

@Service 
@AllArgsConstructor 
public class UsersService {

    private final UsersRepository usersRepository;
    private final RolesRepository rolesRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    // Consultar todos los usuarios
    public List<UserResponse> listarUsuarios() {

        return usersRepository.findAll()
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    // Consultar perfil por ID
    public UserResponse buscarUsuario(Long id) {

        Users user = usersRepository.findById(id)
                .orElseThrow(() ->
                        new ApiException(
                                HttpStatus.NOT_FOUND,
                                "Usuario no encontrado"));

        return convertirDTO(user);
    }

    // Crear usuario desde el administrador
    public UserResponse crearUsuario(CreateUserRequest request) {

        if (usersRepository.existsByUsername(request.getUsername())) {
            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "El username ya existe"
            );
        }

        if (usersRepository.existsByEmail(request.getEmail())) {
            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "El email ya existe"
            );
        }

        Roles role = rolesRepository.findByName(request.getRole())
                .orElseThrow(() ->
                        new ApiException(
                                HttpStatus.NOT_FOUND,
                                "Rol no encontrado"));

        Users user = new Users();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        user.setPassword(
                passwordEncoder.encode(request.getPassword()));

        user.setActive(true);
        user.setRole(role);

        Users guardado = usersRepository.save(user);

        return convertirDTO(guardado);
    }

    // Cambiar rol
    public UserResponse cambiarRol(
            Long id,
            UpdateUserRoleRequest request) {

        Users user = usersRepository.findById(id)
                .orElseThrow(() ->
                        new ApiException(
                                HttpStatus.NOT_FOUND,
                                "Usuario no encontrado"));

        Roles role = rolesRepository.findByName(request.getRole())
                .orElseThrow(() ->
                        new ApiException(
                                HttpStatus.NOT_FOUND,
                                "Rol no encontrado"));

        user.setRole(role);

        Users actualizado = usersRepository.save(user);

        return convertirDTO(actualizado);
    }

    // Activar o desactivar usuario
    public UserResponse cambiarEstado(
            Long id,
            UpdateUserStatusRequest request) {

        Users user = usersRepository.findById(id)
                .orElseThrow(() ->
                        new ApiException(
                                HttpStatus.NOT_FOUND,
                                "Usuario no encontrado"));

        user.setActive(request.getActive());

        Users actualizado = usersRepository.save(user);

        return convertirDTO(actualizado);
    }

    // Convertir Entity a DTO
    private UserResponse convertirDTO(Users user) {

        UserResponse dto = new UserResponse();

        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setActive(user.getActive());

        dto.setRolId(user.getRole().getId());
        dto.setRoleName(user.getRole().getName().name());

        return dto;
    }
    
}
