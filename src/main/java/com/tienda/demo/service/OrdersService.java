package com.tienda.demo.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.tienda.demo.DTO.orders.OrderRequest;
import com.tienda.demo.DTO.orders.OrderResponse;
import com.tienda.demo.DTO.orders.UpdateOrderStatusRequest;
import com.tienda.demo.entity.Orders;
import com.tienda.demo.entity.Products;
import com.tienda.demo.entity.Users;
import com.tienda.demo.enums.OrderStatus;
import com.tienda.demo.repository.OrdersRepository;
import com.tienda.demo.repository.ProductsRepository;
import com.tienda.demo.repository.UsersRepository;

import org.springframework.http.HttpStatus;
import com.tienda.demo.exception.ApiException;
import lombok.AllArgsConstructor;
import jakarta.transaction.Transactional;
import java.util.List;


@Service 
@AllArgsConstructor 
public class OrdersService {
    
    private final OrdersRepository ordersRepository;
    private final ProductsRepository productsRepository;
    private final UsersRepository usersRepository;


    // Crear pedido
    @Transactional
    public OrderResponse crearPedido(
            OrderRequest request,
            Long clientId) {

        if (request.getQuantity() == null ||
                request.getQuantity() <= 0) {

            throw new RuntimeException(
                    "La cantidad debe ser mayor que cero");
        }

        Products product = productsRepository
                .findById(request.getProductId())
                .orElseThrow(() ->
                        new ApiException(
                                HttpStatus.NOT_FOUND,
                                "Producto no encontrado"));

        // Verificar stock
        if (product.getStock() < request.getQuantity()) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "No hay suficiente stock disponible");
        }

        Users client = usersRepository.findById(clientId)
                .orElseThrow(() ->
                        new RuntimeException("Cliente no encontrado"));

        // Calcular total en el servidor
        BigDecimal total = product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity()));
        // Descontar stock
        product.setStock(
                product.getStock() - request.getQuantity());

        productsRepository.save(product);

        // Crear pedido
        Orders order = new Orders();

        order.setClient(client);
        order.setProduct(product);
        order.setQuantity(request.getQuantity());
        order.setTotal(total);
        order.setStatus(OrderStatus.PENDIENTE);

        Orders guardado = ordersRepository.save(order);

        return convertirDTO(guardado);
    }

    // Pedidos del cliente
    public List<OrderResponse> listarMisPedidos(Long clientId) {

        return ordersRepository.findByClient_Id(clientId)
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    // Pedidos de los productos del vendedor
    public List<OrderResponse> listarVentas(Long sellerId) {

        return ordersRepository
                .findByProduct_Seller_Id(sellerId)
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    // Todos los pedidos: ADMIN
    public List<OrderResponse> listarTodos() {

        return ordersRepository.findAll()
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    // Cambiar estado de pedido
    @Transactional
    public OrderResponse cambiarEstado(
            Long orderId,
            UpdateOrderStatusRequest request,
            Long userId,
            boolean isAdmin) {

        Orders order = obtenerPedido(orderId);

        // Si no es ADMIN, debe ser vendedor dueño del producto
        if (!isAdmin &&
                !order.getProduct().getSeller()
                        .getId().equals(userId)) {

            throw new ApiException(
                    HttpStatus.FORBIDDEN,
                    "No puedes modificar pedidos de otro vendedor");
        }

        order.setStatus(request.getStatus());

        Orders actualizado = ordersRepository.save(order);

        return convertirDTO(actualizado);
    }

    // Cancelar pedido por el cliente
    @Transactional
    public OrderResponse cancelarPedido(
            Long orderId,
            Long clientId) {

        Orders order = obtenerPedido(orderId);

        // Validar que sea el pedido del cliente
        if (!order.getClient().getId().equals(clientId)) {

            throw new ApiException(
                    HttpStatus.FORBIDDEN,
                    "No puedes cancelar pedidos de otro cliente");
        }

        // Solo se cancela si está pendiente
        if (order.getStatus() != OrderStatus.PENDIENTE) {

            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "Solo puedes cancelar pedidos pendientes");
        }

        // Devolver stock
        Products product = order.getProduct();

        product.setStock(
                product.getStock() + order.getQuantity());

        productsRepository.save(product);

        // Cambiar estado
        order.setStatus(OrderStatus.CANCELADA);

        Orders actualizado = ordersRepository.save(order);

        return convertirDTO(actualizado);
    }

    // Buscar pedido
    private Orders obtenerPedido(Long id) {

        return ordersRepository.findById(id)
                .orElseThrow(() ->
                        new ApiException(
                                HttpStatus.NOT_FOUND,
                                "Pedido no encontrado"));
    }

    // Convertir Entity a DTO
    private OrderResponse convertirDTO(Orders order) {

        OrderResponse dto = new OrderResponse();

        dto.setId(order.getId());

        dto.setClientId(order.getClient().getId());
        dto.setClientUsername(
                order.getClient().getUsername());

        dto.setProductId(order.getProduct().getId());
        dto.setProductName(
                order.getProduct().getName());

        dto.setQuantity(order.getQuantity());
        dto.setTotal(order.getTotal());

        dto.setStatus(order.getStatus());
        dto.setCreatedAt(order.getCreatedAt());

        return dto;
    }
}
