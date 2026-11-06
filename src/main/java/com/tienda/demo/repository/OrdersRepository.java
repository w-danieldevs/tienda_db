package com.tienda.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tienda.demo.entity.Orders;
import java.util.List;

public interface  OrdersRepository  extends  JpaRepository<Orders, Long>{
    List<Orders> findByClient_Id(Long clientId);

    List<Orders> findByProduct_Seller_Id(Long sellerId);
}
