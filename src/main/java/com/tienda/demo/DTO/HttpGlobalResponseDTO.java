package com.tienda.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data 
@AllArgsConstructor 
public class HttpGlobalResponseDTO<T> {

    private String message;
    private T data;
}
