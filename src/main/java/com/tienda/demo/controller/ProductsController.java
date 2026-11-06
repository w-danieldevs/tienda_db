
package com.tienda.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tienda.demo.DTO.HttpGlobalResponseDTO;
import com.tienda.demo.DTO.products.ProductRequest;
import com.tienda.demo.DTO.products.ProductResponse;
import com.tienda.demo.annotations.RequiresRole;
import com.tienda.demo.service.ProductsServicie;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductsController {

    private final ProductsServicie productsService;

    // Consultar todos los productos.
    @GetMapping
    public ResponseEntity<HttpGlobalResponseDTO<List<ProductResponse>>> getAllProducts(
            @RequestParam(required = false) Long categoryId) {

        List<ProductResponse> response =
                productsService.listarProductos(categoryId);

        return ResponseEntity.ok(
                new HttpGlobalResponseDTO<>(
                        "Productos consultados correctamente",
                        response));
    }

    // Consultar un producto por su ID.
    @GetMapping("/{id}")
    public ResponseEntity<HttpGlobalResponseDTO<ProductResponse>> getProductById(
            @PathVariable Long id) {

        ProductResponse response =
                productsService.buscarProducto(id);

        return ResponseEntity.ok(
                new HttpGlobalResponseDTO<>(
                        "Producto consultado correctamente",
                        response));
    }

    // Consultar los productos del vendedor.
    @GetMapping("/mine")
    @RequiresRole("VENDEDOR")
    public ResponseEntity<HttpGlobalResponseDTO<List<ProductResponse>>> getMyProducts(
            HttpServletRequest request) {

        Long userId = Long.valueOf(
                request.getAttribute("userId").toString());

        List<ProductResponse> response =
                productsService.listarMisProductos(userId);

        return ResponseEntity.ok(
                new HttpGlobalResponseDTO<>(
                        "Tus productos fueron consultados correctamente",
                        response));
    }

    // Crear producto. Solo VENDEDOR.
    @PostMapping
    @RequiresRole("VENDEDOR")
    public ResponseEntity<HttpGlobalResponseDTO<ProductResponse>> createProduct(
            @RequestBody ProductRequest request,
            HttpServletRequest httpRequest) {

        Long userId = Long.valueOf(
                httpRequest.getAttribute("userId").toString());

        ProductResponse response =
                productsService.crearProducto(request, userId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new HttpGlobalResponseDTO<>(
                        "Producto creado correctamente",
                        response));
    }

    // Editar producto. VENDEDOR dueño o ADMIN.
    @PutMapping("/{id}")
    @RequiresRole({"VENDEDOR", "ADMIN"})
    public ResponseEntity<HttpGlobalResponseDTO<ProductResponse>> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductRequest request,
            HttpServletRequest httpRequest) {

        Long userId = Long.valueOf(
                httpRequest.getAttribute("userId").toString());

        String role = (String) httpRequest.getAttribute("role");

        boolean isAdmin = "ADMIN".equals(role);

        ProductResponse response =
                productsService.editarProducto(
                        id, request, userId, isAdmin);

        return ResponseEntity.ok(
                new HttpGlobalResponseDTO<>(
                        "Producto actualizado correctamente",
                        response));
    }

// Eliminar producto. VENDEDOR dueño o ADMIN.
@DeleteMapping("/{id}")
@RequiresRole({"VENDEDOR", "ADMIN"})
public ResponseEntity<HttpGlobalResponseDTO<String>> deleteProduct(
        @PathVariable Long id,
        HttpServletRequest request) {

    Long userId = Long.valueOf(
            request.getAttribute("userId").toString());

    String role = (String) request.getAttribute("role");

    boolean isAdmin = "ADMIN".equals(role);

    String response = productsService.eliminarProducto(
            id, userId, isAdmin);

    return ResponseEntity.ok(
            new HttpGlobalResponseDTO<>(
                    "Producto eliminado correctamente",
                    response));
}
}
