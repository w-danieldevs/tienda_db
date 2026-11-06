package com.tienda.demo.DTO.orders;

import com.tienda.demo.enums.OrderStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data 
public class UpdateOrderStatusRequest {

    @NotNull (message = "El estado del pedido es obligatorio")
    private OrderStatus status;

}
