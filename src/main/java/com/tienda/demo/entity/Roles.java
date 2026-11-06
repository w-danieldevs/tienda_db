package com.tienda.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.GenerationType;
import com.tienda.demo.enums.RoleName;

@Entity 
@Data 
@Table ( name = "roles")
public class Roles {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column (name = "name")
    private RoleName name;
}
