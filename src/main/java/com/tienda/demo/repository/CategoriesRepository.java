package com.tienda.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tienda.demo.entity.Categories;

public interface CategoriesRepository  extends  JpaRepository<Categories, Long>{

      boolean existsByNombre(String nombre);
}
