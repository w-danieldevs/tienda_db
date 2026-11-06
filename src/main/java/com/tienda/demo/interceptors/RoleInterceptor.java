package com.tienda.demo.interceptors;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import com.tienda.demo.DTO.HttpGlobalResponseDTO;
import com.tienda.demo.annotations.RequiresRole;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RoleInterceptor implements HandlerInterceptor {

    private final tools.jackson.databind.ObjectMapper objectMapper;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws IOException {

        // Comprobar que la petición corresponde a un método de un Controller.
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        // Buscar si el método tiene la anotación RequiresRole.
        RequiresRole requiresRole =
                handlerMethod.getMethodAnnotation(RequiresRole.class);

        // Si no tiene anotación, no necesita esta validación.
        if (requiresRole == null) {
            return true;
        }

        // Obtener el rol que el filtro JWT guardó en la petición.
        String userRole = (String) request.getAttribute("role");

        // Si no hay rol, el usuario no está autenticado.
        if (userRole == null) {
            sendError(
                    response,
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "No estás autenticado");

            return false;
        }

        // Obtener los roles permitidos por la anotación.
        String[] allowedRoles = requiresRole.value();

        // Comprobar si el rol del usuario está permitido.
        boolean hasPermission = Arrays.asList(allowedRoles)
                .contains(userRole);

        // Si no tiene permiso, responder 403.
        if (!hasPermission) {
            sendError(
                    response,
                    HttpServletResponse.SC_FORBIDDEN,
                    "No tienes permisos para realizar esta operación");

            return false;
        }

        // Si tiene permiso, continúa hacia el Controller.
        return true;
    }

    // Construir la respuesta de error.
    private void sendError(
            HttpServletResponse response,
            int status,
            String message) throws IOException {

        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpGlobalResponseDTO<Void> error =
                new HttpGlobalResponseDTO<>(message, null);

        response.getWriter().write(
                objectMapper.writeValueAsString(error));
    }
}
