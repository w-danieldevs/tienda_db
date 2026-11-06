package com.tienda.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import java.util.List;

@Entity 
@Data 
@Table ( name = "categories")
public class Categories {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private Long id; 

    @Column (name = "name")
    private String nombre;

    @OneToMany(mappedBy = "category")
    private List<Products> products;

}
