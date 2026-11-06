
package com.tienda.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tienda.demo.DTO.HttpGlobalResponseDTO;
import com.tienda.demo.DTO.orders.OrderRequest;
import com.tienda.demo.DTO.orders.OrderResponse;
import com.tienda.demo.DTO.orders.UpdateOrderStatusRequest;
import com.tienda.demo.annotations.RequiresRole;
import com.tienda.demo.service.OrdersService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrdersController {

    private final OrdersService ordersService;
    


    // Crear un pedido. Solo CLIENTE.
    @PostMapping
    @RequiresRole("CLIENTE")
    public ResponseEntity<HttpGlobalResponseDTO<OrderResponse>> createOrder(
            @Valid @RequestBody OrderRequest request,
            HttpServletRequest httpRequest) {

        // Obtener el ID del cliente desde el JWT.
        Long clientId = Long.valueOf(
                httpRequest.getAttribute("userId").toString());

        OrderResponse response =
                ordersService.crearPedido(request, clientId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new HttpGlobalResponseDTO<>(
                        "Pedido creado correctamente",
                        response));
    }

    // Consultar los pedidos del cliente.
    @GetMapping("/mine")
    @RequiresRole("CLIENTE")
    public ResponseEntity<HttpGlobalResponseDTO<List<OrderResponse>>> getMyOrders(
            HttpServletRequest request) {

        Long clientId = Long.valueOf(
                request.getAttribute("userId").toString());

        List<OrderResponse> response =
                ordersService.listarMisPedidos(clientId);

        return ResponseEntity.ok(
                new HttpGlobalResponseDTO<>(
                        "Tus pedidos fueron consultados correctamente",
                        response));
    }

    // Consultar las ventas del vendedor.
    @GetMapping("/sales")
    @RequiresRole("VENDEDOR")
    public ResponseEntity<HttpGlobalResponseDTO<List<OrderResponse>>> getMySales(
            HttpServletRequest request) {

        Long sellerId = Long.valueOf(
                request.getAttribute("userId").toString());

        List<OrderResponse> response =
                ordersService.listarVentas(sellerId);

        return ResponseEntity.ok(
                new HttpGlobalResponseDTO<>(
                        "Tus ventas fueron consultadas correctamente",
                        response));
    }

    // Consultar todos los pedidos. Solo ADMIN.
    @GetMapping
    @RequiresRole("ADMIN")
    public ResponseEntity<HttpGlobalResponseDTO<List<OrderResponse>>> getAllOrders() {

        List<OrderResponse> response =
                ordersService.listarTodos();

        return ResponseEntity.ok(
                new HttpGlobalResponseDTO<>(
                        "Pedidos consultados correctamente",
                        response));
    }

    // Cambiar el estado de un pedido.
    // VENDEDOR propietario o ADMIN.
    @PatchMapping("/{id}/status")
    @RequiresRole({"VENDEDOR", "ADMIN"})
    public ResponseEntity<HttpGlobalResponseDTO<OrderResponse>> updateOrderStatus(
            @PathVariable Long id,
           @Valid  @RequestBody UpdateOrderStatusRequest request,
            HttpServletRequest httpRequest) {

        Long userId = Long.valueOf(
                httpRequest.getAttribute("userId").toString());

        String role = (String) httpRequest.getAttribute("role");

        boolean isAdmin = "ADMIN".equals(role);

        OrderResponse response =
                ordersService.cambiarEstado(
                        id, request, userId, isAdmin);

        return ResponseEntity.ok(
                new HttpGlobalResponseDTO<>(
                        "Estado del pedido actualizado correctamente",
                        response));
    }

    // Cancelar un pedido. Solo CLIENTE propietario.
    @PatchMapping("/{id}/cancel")
    @RequiresRole("CLIENTE")
    public ResponseEntity<HttpGlobalResponseDTO<OrderResponse>> cancelOrder(
            @PathVariable Long id,
            HttpServletRequest request) {

        Long clientId = Long.valueOf(
                request.getAttribute("userId").toString());

        OrderResponse response =
                ordersService.cancelarPedido(id, clientId);

        return ResponseEntity.ok(
                new HttpGlobalResponseDTO<>(
                        "Pedido cancelado correctamente",
                        response));
    }
}
