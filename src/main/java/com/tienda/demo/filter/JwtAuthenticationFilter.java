package com.tienda.demo.filter;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tienda.demo.service.JwtService;

import com.tienda.demo.enums.RoleName;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // Obtener la ruta y el método HTTP.
        String path = request.getServletPath();
        String method = request.getMethod();

        // Comprobar si la ruta es pública.
        boolean publicRoute = isPublicRoute(path, method);

        // Leer el encabezado Authorization.
        String authorization = request.getHeader("Authorization");

        // Si no hay token y la ruta es pública, continuar.
        if (authorization == null || authorization.isBlank()) {

            if (publicRoute) {
                filterChain.doFilter(request, response);
                return;
            }

            // Las rutas protegidas necesitan token.
            sendUnauthorized(response, "Debes iniciar sesión para acceder");
            return;
        }

        // Comprobar que el encabezado tenga el formato Bearer.
        if (!authorization.startsWith("Bearer ")) {
            sendUnauthorized(response, "Formato de token incorrecto");
            return;
        }

        // Extraer el token, quitando la palabra Bearer.
        String token = authorization.substring(7);

   Claims claims;

try {

    claims = jwtService.getClaims(token);

    Long userId = ((Number) claims.get("userId")).longValue();
    Long rolId = ((Number) claims.get("rolId")).longValue();

    String username = claims.getSubject();
    String role = claims.get("role", String.class);

    // Comprobar que el token tenga los datos necesarios.
if (username == null || username.isBlank()
        || role == null || role.isBlank()) {
    throw new IllegalArgumentException(
            "El token no contiene los datos necesarios");
}

RoleName.valueOf(role);

    request.setAttribute("userId", userId);
    request.setAttribute("rolId", rolId);
    request.setAttribute("username", username);
    request.setAttribute("role", role);

} catch (Exception e) {

    sendUnauthorized(response, "El token es inválido o ha expirado");
    return;
}

// Continuar después de validar el token.
filterChain.doFilter(request, response);
        }
    

    // Determinar cuáles rutas no necesitan autenticación.
    private boolean isPublicRoute(String path, String method) {

        // Todas las rutas de autenticación son públicas.
        if (path.startsWith("/auth/") || path.equals("/auth")) {
            return true;
        }

        // El catálogo y el detalle de productos son públicos.
        if (method.equals("GET")) {

            if (path.equals("/products")) {
                return true;
            }

            // Solo permite rutas como /products/5.
            if (path.matches("/products/[0-9]+")) {
                return true;
            }
        }

        return false;
    }

    // Responder cuando el usuario no está autenticado.
    private void sendUnauthorized(
            HttpServletResponse response,
            String message) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(
                "{\"message\":\"" + message + "\",\"data\":null}");
    }
}
