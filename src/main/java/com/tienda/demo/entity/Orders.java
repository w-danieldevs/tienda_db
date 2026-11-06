
package com.tienda.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.tienda.demo.enums.OrderStatus;

@Entity
@Data
@Table(name = "orders")
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "total")
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Users client;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Products product;
}
