package com.tienda.demo.DTO.products;

import lombok.Data;
import java.math.BigDecimal;

@Data 
public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Long categoryId;
    private String categoryName;
    private Long sellerId;
    private String sellerUsername;

  
}
