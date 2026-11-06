package com.tienda.demo.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tienda.demo.DTO.auth.LoginRequest;
import com.tienda.demo.DTO.auth.LoginResponse;
import com.tienda.demo.DTO.auth.RefreshTokenResponse;
import com.tienda.demo.DTO.auth.RegisterRequest;
import com.tienda.demo.DTO.users.UserResponse;
import com.tienda.demo.entity.Roles;
import com.tienda.demo.entity.Users;
import com.tienda.demo.enums.RoleName;
import com.tienda.demo.exception.ApiException;
import com.tienda.demo.repository.RolesRepository;
import com.tienda.demo.repository.UsersRepository;

import lombok.AllArgsConstructor;

@Service 
@AllArgsConstructor 
public class AuthService {
    private final UsersRepository usersRepository;
    private final RolesRepository rolesRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    // Registrar usuario
    public UserResponse registrar(RegisterRequest request) {

        // Validar username
        if (usersRepository.existsByUsername(
                request.getUsername())) {

            // Username o email repetido
         throw new ApiException(
        HttpStatus.CONFLICT,
        "El username ya está registrado" 
        );
        }

        // Validar email
        if (usersRepository.existsByEmail(
                request.getEmail())) {

            throw new RuntimeException(
                    "El email ya está registrado");
        }

        // Buscar rol CLIENTE
        Roles role = rolesRepository
                .findByName(RoleName.CLIENTE)
                .orElseThrow(() ->
                        new RuntimeException(
                                "No existe el rol CLIENTE"));

        // Crear usuario
        Users user = new Users();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        // Encriptar contraseña
        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()));

        user.setActive(true);
        user.setRole(role);

        Users guardado = usersRepository.save(user);

        return convertirDTO(guardado);
    }

    // Login
    public LoginResponse login(LoginRequest request) {

        // Buscar por username o email
        Users user = usersRepository
                .findByUsernameOrEmail(
                        request.getUser(),
                        request.getUser())
                .orElseThrow(() ->
                        new ApiException(
                                HttpStatus.UNAUTHORIZED,
                                "Credenciales incorrectas"));

        // Validar usuario activo
        if (!user.getActive()) {

           // Usuario desactivado
throw new ApiException(
        HttpStatus.UNAUTHORIZED,
        "El usuario está desactivado"
);

        }

        // Verificar contraseña
        boolean passwordCorrecta =
                passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword());

        if (!passwordCorrecta) {

            // Credenciales incorrectas
throw new ApiException(
        HttpStatus.UNAUTHORIZED,
        "Credenciales incorrectas" 
        );
        }

        // Generar token
        String jwt = jwtService.generateToken(user);

        // Devolver token y usuario
        UserResponse userDTO = convertirDTO(user);

        return new LoginResponse(jwt, userDTO);
    }

    // Renovar token
   public RefreshTokenResponse refrescarToken(
        String token) {

    if (!jwtService.validateToken(token)) {

        throw new ApiException(
        HttpStatus.UNAUTHORIZED,
        "Token inválido o expirado" 
        );
    }

    String nuevoToken =
            jwtService.refreshToken(token);

    return new RefreshTokenResponse(
            nuevoToken,
            "Bearer"
    );
}

    // Convertir Entity a DTO
    private UserResponse convertirDTO(Users user) {

        UserResponse dto = new UserResponse();

        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setActive(user.getActive());

        dto.setRolId(user.getRole().getId());

        dto.setRoleName(
                user.getRole().getName().name());

        return dto;
    }
}
