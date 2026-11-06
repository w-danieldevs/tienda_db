package com.tienda.demo.service;

import org.springframework.stereotype.Service;

import com.tienda.demo.DTO.products.ProductRequest;
import com.tienda.demo.DTO.products.ProductResponse;
import com.tienda.demo.entity.Categories;
import com.tienda.demo.entity.Products;
import com.tienda.demo.entity.Users;
import com.tienda.demo.repository.CategoriesRepository;
import com.tienda.demo.repository.ProductsRepository;
import com.tienda.demo.repository.UsersRepository;

import org.springframework.http.HttpStatus;
import com.tienda.demo.exception.ApiException;
import lombok.AllArgsConstructor;
import jakarta.transaction.Transactional;
import java.util.List;

@Service 
@AllArgsConstructor 
 public class ProductsServicie {

     private final ProductsRepository productsRepository;
    private final CategoriesRepository categoriesRepository;
    private final UsersRepository usersRepository;


    // Catálogo público
    public List<ProductResponse> listarProductos(Long categoryId) {

        List<Products> products;

        if (categoryId != null) {
            products = productsRepository
                    .findByCategory_Id(categoryId);
        } else {
            products = productsRepository.findAll();
        }

        return products.stream()
                .map(this::convertirDTO)
                .toList();
    }

    // Buscar producto
    public ProductResponse buscarProducto(Long id) {

        Products product = obtenerProducto(id);

        return convertirDTO(product);
    }

    // Productos del vendedor autenticado
    public List<ProductResponse> listarMisProductos(Long sellerId) {

        return productsRepository.findBySeller_Id(sellerId)
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    // Crear producto
    public ProductResponse crearProducto(
            ProductRequest request,
            Long sellerId) {

        Categories category = categoriesRepository
                .findById(request.getCategoryId())
                .orElseThrow(() ->
                        new ApiException(
                                HttpStatus.NOT_FOUND,
                                "Categoría no encontrada"));

        Users seller = usersRepository.findById(sellerId)
                .orElseThrow(() ->
                        new ApiException(
                                HttpStatus.NOT_FOUND,
                                "Vendedor no encontrado"));

        Products product = new Products();

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());

        product.setCategory(category);
        product.setSeller(seller);

        Products guardado = productsRepository.save(product);

        return convertirDTO(guardado);
    }

    // Editar producto
    @Transactional
    public ProductResponse editarProducto(
            Long id,
            ProductRequest request,
            Long userId,
            boolean isAdmin) {

        Products product = obtenerProducto(id);

        // Validar propiedad
        validarPropiedad(product, userId, isAdmin);

        Categories category = categoriesRepository
                .findById(request.getCategoryId())
                .orElseThrow(() ->
                        new ApiException(
                                HttpStatus.NOT_FOUND,
                                "Categoría no encontrada"));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategory(category);

        Products actualizado = productsRepository.save(product);

        return convertirDTO(actualizado);
    }

    // Eliminar producto
    public String eliminarProducto(
            Long id,
            Long userId,
            boolean isAdmin) {

        Products product = obtenerProducto(id);

        validarPropiedad(product, userId, isAdmin);

        productsRepository.delete(product);

        return "Producto eliminado correctamente";
    }

    // Buscar producto en la base de datos
    private Products obtenerProducto(Long id) {

        return productsRepository.findById(id)
                .orElseThrow(() ->
                        new ApiException(
                                HttpStatus.NOT_FOUND,
                                "Producto no encontrado"));
    }

    // Validar que el vendedor sea el dueño
    private void validarPropiedad(
            Products product,
            Long userId,
            boolean isAdmin) {

        if (!isAdmin &&
                !product.getSeller().getId().equals(userId)) {

            throw new ApiException(
                    HttpStatus.FORBIDDEN,
                    "No puedes modificar productos de otro vendedor");
        }
    }

    // Convertir Entity a DTO
    private ProductResponse convertirDTO(Products product) {

        ProductResponse dto = new ProductResponse();

        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());

        dto.setCategoryId(product.getCategory().getId());
        dto.setCategoryName(product.getCategory().getNombre());

        dto.setSellerId(product.getSeller().getId());
        dto.setSellerUsername(product.getSeller().getUsername());

        return dto;
    }


}