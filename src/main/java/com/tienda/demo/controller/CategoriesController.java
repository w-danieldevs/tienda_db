
package com.tienda.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tienda.demo.DTO.HttpGlobalResponseDTO;
import com.tienda.demo.DTO.categories.CategoryRequest;
import com.tienda.demo.DTO.categories.CategoryResponse;
import com.tienda.demo.annotations.RequiresRole;
import com.tienda.demo.service.CategoriesService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoriesController {

    private final CategoriesService categoriesService;

    // Consultar todas las categorías.
    @GetMapping
    public ResponseEntity<HttpGlobalResponseDTO<List<CategoryResponse>>> getAllCategories() {

        List<CategoryResponse> response = categoriesService.listarCategorias();

        return ResponseEntity.ok(
                new HttpGlobalResponseDTO<List<CategoryResponse>>(
                        "Categorías consultadas correctamente", response));
    }

    // Crear una categoría. Solo ADMIN.
    @PostMapping
    @RequiresRole("ADMIN")
    public ResponseEntity<HttpGlobalResponseDTO<CategoryResponse>> createCategory(
            @RequestBody CategoryRequest request) {

        CategoryResponse response = categoriesService.crearCategoria(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new HttpGlobalResponseDTO<CategoryResponse>(
                        "Categoría creada correctamente", response));
    }

    // Eliminar una categoría. Solo ADMIN.
    @DeleteMapping("/{id}")
    @RequiresRole("ADMIN")
    public ResponseEntity<HttpGlobalResponseDTO<Void>> deleteCategory(
            @PathVariable Long id) {

        categoriesService.eliminarCategoria(id);

        return ResponseEntity.ok(
                new HttpGlobalResponseDTO<Void>(
                        "Categoría eliminada correctamente", null));
    }
}
