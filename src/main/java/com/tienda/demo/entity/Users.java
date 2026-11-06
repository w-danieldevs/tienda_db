package com.tienda.demo.entity;

import jakarta.persistence.*;
import lombok.Data;



@Entity
@Data 
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "active")
    private Boolean active = true;

    @ManyToOne
    @JoinColumn(name = "rol_id")
    private Roles role;

    
}
 