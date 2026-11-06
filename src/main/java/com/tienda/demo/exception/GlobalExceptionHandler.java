
package com.tienda.demo.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tienda.demo.DTO.HttpGlobalResponseDTO;

import org.springframework.validation.FieldError;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Manejar nuestras excepciones personalizadas
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<HttpGlobalResponseDTO<Void>> manejarApiException(
            ApiException ex) {

        return ResponseEntity.status(ex.getStatus())
                .body(new HttpGlobalResponseDTO<>(
                        ex.getMessage(),
                        null));
    }

    // Manejar errores de validación de los DTO
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HttpGlobalResponseDTO<Map<String, String>>> manejarValidaciones(
            MethodArgumentNotValidException ex) {

        Map<String, String> errores = new LinkedHashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errores.putIfAbsent(
                    error.getField(),
                    error.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new HttpGlobalResponseDTO<>(
                        "Error de validación",
                        errores));
    }

    // Manejar JSON incorrecto o valores de enum inválidos
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<HttpGlobalResponseDTO<Void>> manejarJsonIncorrecto(
            HttpMessageNotReadableException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new HttpGlobalResponseDTO<>(
                        "El cuerpo de la petición no es válido",
                        null));
    }

    // Manejar conflictos con la base de datos
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<HttpGlobalResponseDTO<Void>> manejarConflictosBD(
            DataIntegrityViolationException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new HttpGlobalResponseDTO<>(
                        "Existe un conflicto con los datos de la base de datos",
                        null));
    }

    // Manejar cualquier error inesperado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpGlobalResponseDTO<Void>> manejarErrorGeneral(
            Exception ex) {

        log.error("Error inesperado en la API", ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new HttpGlobalResponseDTO<>(
                        "Ocurrió un error interno en el servidor",
                        null));
    }
}

