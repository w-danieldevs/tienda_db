
package com.tienda.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@Table(name = "products")
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "stock")
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categories category;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Users seller;

    @OneToMany(mappedBy = "product")
    private List<Orders> orders;
}
