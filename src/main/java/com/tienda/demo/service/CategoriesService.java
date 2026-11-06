package com.tienda.demo.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.tienda.demo.repository.ProductsRepository;
import com.tienda.demo.repository.CategoriesRepository;
import com.tienda.demo.DTO.categories.CategoryRequest;
import com.tienda.demo.DTO.categories.CategoryResponse;
import com.tienda.demo.entity.Categories;
import com.tienda.demo.exception.ApiException;

import lombok.AllArgsConstructor;
import jakarta.transaction.Transactional;


@Service 
@AllArgsConstructor 
public class CategoriesService {
    
    private final CategoriesRepository categoriesRepository;
    private final ProductsRepository productsRepository;

    public List<CategoryResponse> listarCategorias() {
        return categoriesRepository.findAll().stream()
                .map(this:: convertirDTO)
                .toList();
    }

    public CategoryResponse crearCategoria(CategoryRequest request) {

        if (categoriesRepository.existsByNombre(request.getName())) {
            // Categoría duplicada
throw new ApiException(
        HttpStatus.CONFLICT,
        "La categoría ya existe"
);
        }

        Categories category = new Categories();

        category.setNombre(request.getName());

        Categories guardada = categoriesRepository.save(category);

        return convertirDTO(guardada);
    }

    // Eliminar categoría
    @Transactional
    public void eliminarCategoria(Long id) {

        Categories category = categoriesRepository.findById(id)
                .orElseThrow(() ->
                        new ApiException(
                                HttpStatus.NOT_FOUND,
                                "Categoría no encontrada"));

        boolean tieneProductos =
                productsRepository.existsById(id);

        if (tieneProductos) {
            // Categoría con productos
throw new ApiException(
        HttpStatus.CONFLICT,
        "No se puede eliminar una categoría con productos"
);
        }

        categoriesRepository.delete(category);
    }

    // Convertir Entity a DTO
    private CategoryResponse convertirDTO(Categories category) {

        CategoryResponse dto = new CategoryResponse();

        dto.setId(category.getId());
        dto.setName(category.getNombre());

        return dto;
    }

    
}
