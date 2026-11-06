package com.tienda.demo.DTO.orders;

import lombok.Data;

@Data 
public class CreateOrderRequest {

    private Long productId;
    private Integer quantity;

   
}
