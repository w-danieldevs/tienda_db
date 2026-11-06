package com.tienda.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tienda.demo.entity.Products;
import java.util.List;

public interface ProductsRepository  extends  JpaRepository<Products, Long>{

 List<Products> findByCategory_Id(Long categoryId);

    List<Products> findBySeller_Id(Long sellerId);

    boolean existsByCategory_Id(Long categoryId);
}
    

