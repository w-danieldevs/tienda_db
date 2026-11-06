package com.tienda.demo.DTO.orders;

import com.tienda.demo.enums.OrderStatus;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data 
public class OrderResponse {

    private Long id;
    private Long clientId;
    private String clientUsername;
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal total;
    private OrderStatus status;
    private LocalDateTime createdAt;

}
